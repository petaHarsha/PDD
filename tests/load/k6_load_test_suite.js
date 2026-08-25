import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

// Custom Performance Metrics & SLA Trackers
export const errorRate = new Rate('errors');
export const prognosisLatency = new Trend('prognosis_duration');
export const patientRosterLatency = new Trend('patient_roster_duration');

export const options = {
  stages: [
    { duration: '30s', target: 20 },  // Ramp-up to 20 VUs
    { duration: '1m', target: 50 },   // Sustained normal traffic (50 VUs)
    { duration: '30s', target: 150 }, // Peak spike (150 VUs)
    { duration: '1m', target: 150 },  // Sustained peak load
    { duration: '30s', target: 0 },   // Clean ramp-down
  ],
  thresholds: {
    'http_req_duration': ['p(95)<500', 'p(99)<1000'], // 95% of requests must complete under 500ms
    'errors': ['rate<0.01'],                           // Error rate must remain under 1%
    'prognosis_duration': ['p(95)<400'],               // AI prediction P95 under 400ms
    'patient_roster_duration': ['p(95)<250'],          // Roster query P95 under 250ms
  },
};

const BASE_URL = __ENV.TARGET_URL || 'http://127.0.0.1:8000';

export default function () {
  const headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  group('01. Baseline Health Check', function () {
    const res = http.get(`${BASE_URL}/health`);
    const success = check(res, {
      'health status is 200': (r) => r.status === 200,
      'response has status healthy': (r) => JSON.parse(r.body).status === 'healthy',
    });
    errorRate.add(!success);
  });

  group('02. Patient Roster & Search Query', function () {
    const start = new Date();
    const res = http.get(`${BASE_URL}/patients`, { headers });
    patientRosterLatency.add(new Date() - start);

    const success = check(res, {
      'patients status is 200': (r) => r.status === 200,
      'patients returned array': (r) => Array.isArray(JSON.parse(r.body)),
    });
    errorRate.add(!success);
  });

  group('03. AI Prognosis Recurrence Risk Inference', function () {
    const payload = JSON.stringify({
      age: 56,
      smoking_history: 1,
      alcohol_history: 0,
      tumor_size_cm: 3.8,
      lymph_node_involvement: 1,
      hpv_status: 0,
      ian_invasion_detected: 1,
    });

    const start = new Date();
    const res = http.post(`${BASE_URL}/predict/prognosis`, payload, { headers });
    prognosisLatency.add(new Date() - start);

    const success = check(res, {
      'prognosis status is 200': (r) => r.status === 200,
      'response has risk_stratification': (r) => JSON.parse(r.body).risk_stratification !== undefined,
    });
    errorRate.add(!success);
  });

  group('04. Patient Admission Write Operation', function () {
    const payload = JSON.stringify({
      name: `Performance Test Patient ${Math.floor(Math.random() * 10000)}`,
      age: 48,
      notes: 'Load testing patient admission transaction',
    });

    const res = http.post(`${BASE_URL}/patients`, payload, { headers });
    const success = check(res, {
      'create patient status is 200': (r) => r.status === 200,
      'patient ID generated': (r) => JSON.parse(r.body).id !== undefined,
    });
    errorRate.add(!success);
  });

  sleep(1);
}
