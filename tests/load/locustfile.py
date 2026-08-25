"""
Locust Performance & Load Testing Suite for Oral Surgery AI Platform
Simulates concurrent clinician personas (Surgeons, Admins, Intake Staff) under realistic operational load profiles.
"""

import json
import random
from locust import HttpUser, task, between, events

class OralSurgeryClinicianUser(HttpUser):
    wait_time = between(1, 3)
    auth_token = None
    patient_ids = ["P-00101", "P-00102", "P-00103", "P-00104"]

    def on_start(self):
        """Authenticate user on start of virtual user session"""
        response = self.client.post("/auth/login", json={
            "email": "dr.smith@oralsurgery.ai",
            "password": "SurgeonSecurePassword123!"
        }, name="/auth/login [Session Init]")
        
        if response.status_code == 200:
            data = response.json()
            self.auth_token = data.get("token", "mock-auth-jwt")

    @task(5)
    def test_health_check(self):
        """P0: Baseline system health check"""
        self.client.get("/health", name="/health [System Heartbeat]")

    @task(10)
    def test_get_patient_roster(self):
        """P1: Retrieve patient list with latency SLA < 250ms"""
        headers = {"Authorization": f"Bearer {self.auth_token}"} if self.auth_token else {}
        self.client.get("/patients", headers=headers, name="/patients [Fetch Roster]")

    @task(8)
    def test_search_patients(self):
        """P1: Search patient roster with query parameter"""
        search_terms = ["Johnathan", "Eleanor", "Arthur", "P-0010", "Low", "Medium"]
        query = random.choice(search_terms)
        self.client.get(f"/patients?search={query}", name="/patients?search=[Search]")

    @task(12)
    def test_prognosis_prediction(self):
        """P0: AI Machine Learning Prognosis Recurrence Risk Calculation"""
        payload = {
            "age": random.randint(25, 80),
            "smoking_history": random.choice([0, 1]),
            "alcohol_history": random.choice([0, 1]),
            "tumor_size_cm": round(random.uniform(0.5, 9.0), 1),
            "lymph_node_involvement": random.choice([0, 1]),
            "hpv_status": random.choice([0, 1]),
            "ian_invasion_detected": random.choice([0, 1])
        }
        self.client.post("/predict/prognosis", json=payload, name="/predict/prognosis [AI Inference]")

    @task(3)
    def test_create_patient(self):
        """P2: High frequency patient creation transaction"""
        first_names = ["James", "Emma", "Lucas", "Sophia", "Oliver", "Ava", "Liam", "Mia"]
        last_names = ["Harrison", "Chen", "Patel", "Miller", "O'Connor", "Rossi", "Taylor"]
        name = f"{random.choice(first_names)} {random.choice(last_names)}"
        
        payload = {
            "name": name,
            "age": random.randint(18, 90),
            "notes": "Automated load test patient admission."
        }
        self.client.post("/patients", json=payload, name="/patients [Create Record]")

    @task(2)
    def test_toggle_patient_status(self):
        """P2: Patient archive status toggle"""
        pid = random.choice(self.patient_ids)
        self.client.post(f"/patients/{pid}/toggle-status", name="/patients/{id}/toggle-status")

    @task(2)
    def test_view_audit_logs(self):
        """P3: Admin audit log query"""
        headers = {"Authorization": f"Bearer {self.auth_token}"} if self.auth_token else {}
        self.client.get("/admin/audit-logs", headers=headers, name="/admin/audit-logs [Audit Query]")

    @task(4)
    def test_model_training_status_poll(self):
        """P1: Micro-polling AI training state"""
        self.client.get("/train/status", name="/train/status [Telemetry Poll]")


class AdminPowerUser(HttpUser):
    wait_time = between(2, 5)

    @task(5)
    def test_admin_user_listing(self):
        self.client.get("/admin/users", name="/admin/users [Admin Staff Query]")

    @task(2)
    def test_request_registration_otp(self):
        test_email = f"staff_{random.randint(100, 9999)}@oralsurgery.ai"
        self.client.post("/auth/request-registration-otp", json={"email": test_email}, name="/auth/request-registration-otp")
