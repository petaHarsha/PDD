# Oral Surgery AI - Enterprise Automated Testing Framework (1,000+ Cases)

A comprehensive, production-grade automated testing suite designed for the **Oral Surgery AI Clinical Decision Support System** (Android Mobile, Web Application, and Backend REST API).

---

## 📋 Executive Overview (1,040 Total Test Cases)

| Category | Test Count | Tool / Framework | Standards & Scenarios Covered |
| :--- | :---: | :--- | :--- |
| **1. Appium Mobile Automation** | **100** | Appium 2.x + UiAutomator2 / XCUITest | Lifecycle, Auth, Navigation, Patient CRUD, Form Validations, Orientation, Network Faults, OWASP Mobile Security (M1–M10). |
| **2. Selenium Web Automation** | **100** | Selenium WebDriver 4.x (Chrome / Firefox) | Page Object Model (POM), Role-based Auth, Patient CRUD, Prognosis AI, CBCT Viewer, Search/Filter, Responsive Viewports (375px–2560px), Cross-Browser. |
| **3. Vulnerability / Security** | **100** | Pytest + Requests + OWASP Rules | SQL Injection, Stored/Reflected XSS, Broken Access Control, IDOR/BOLA, JWT Cryptography, Rate Limiting (429), CSP/HSTS, File Upload Traversal. |
| **4. Load & Performance** | **740** | Locust + k6 + Async Python Load Runner | 22 unique categories, concurrency up to 2,000 VUs, soak, stress, spike (600 VUs), large payloads (5–100MB), micro-polling, connection pool exhaustion, SLA gates. |
| **TOTAL TEST CASES** | **1,040** | **Unified CI/CD Execution** | **100% Automated, Parameterized & Documented** |

---

## 🗂️ Project Directory Structure

```
webapp/
├── tests/
│   ├── appium/                   # 📱 Appium Mobile Automation Suite (100 Test Cases: TC-APP-001 to TC-APP-100)
│   │   ├── conftest.py           # Driver initialization (live & mock CI)
│   │   ├── base_mobile_page.py   # Mobile POM base methods
│   │   ├── screens/              # Page Object Models for Mobile Screens
│   │   ├── test_app_launch.py
│   │   ├── test_auth_flows.py
│   │   ├── test_navigation.py
│   │   ├── test_patient_management.py
│   │   ├── test_form_validation.py
│   │   ├── test_device_orientation.py
│   │   ├── test_network_failures.py
│   │   └── test_security_controls.py
│   ├── selenium/                 # 🌐 Selenium Web Automation Suite (100 Test Cases: TC-WEB-001 to TC-WEB-100)
│   │   ├── conftest.py           # Browser options, headless config, timeouts
│   │   ├── base_web_page.py      # Web POM base methods
│   │   ├── pages/                # Page Object Models for Web Pages
│   │   ├── test_web_auth.py
│   │   ├── test_web_navigation.py
│   │   ├── test_patient_crud.py
│   │   ├── test_prognosis_prediction.py
│   │   ├── test_search_and_filter.py
│   │   ├── test_form_validations.py
│   │   ├── test_session_handling.py
│   │   ├── test_responsive_views.py
│   │   └── test_cross_browser.py
│   ├── vulnerability/            # 🛡️ Security / Vulnerability Suite (100 Test Cases: TC-SEC-001 to TC-SEC-100)
│   │   ├── conftest.py           # API Test Client & security setup
│   │   ├── test_sqli_injection.py
│   │   ├── test_xss_protection.py
│   │   ├── test_auth_bypass_broken_access.py
│   │   ├── test_idor_bola.py
│   │   ├── test_jwt_token_security.py
│   │   ├── test_rate_limiting_bruteforce.py
│   │   ├── test_sensitive_data_exposure.py
│   │   ├── test_security_headers.py
│   │   ├── test_cors_csrf_configuration.py
│   │   ├── test_file_upload_validation.py
│   │   └── test_http_method_tampering.py
│   └── load/                     # ⚡ Load & Performance Suite (740 Unique Scenarios: TC-LOAD-001 to TC-LOAD-740)
│       ├── load_scenarios_definitions.json  # Complete 740 scenarios definition
│       ├── locustfile.py                    # Locust User simulation scripts
│       ├── k6_load_test_suite.js            # k6 JavaScript load runner
│       └── load_test_runner.py              # High-concurrency Async Python runner
├── .github/
│   └── workflows/
│       ├── automated-testing-suites.yml     # Unified matrix CI/CD execution pipeline
│       ├── load-testing-gate.yml            # Nightly k6 performance gate
│       └── security-vulnerability-scan.yml  # Weekly security audit pipeline
├── config/
│   ├── appium_config.json        # Device capabilities & server URLs
│   ├── selenium_config.json      # Browser configs & viewport breakpoints
│   ├── security_config.json      # Attack vectors & security thresholds
│   ├── load_config.json          # SLA thresholds & concurrency profiles
│   └── test_environments.json    # Local, staging, and CI environment settings
├── docs/                         # 📑 Master Test Case Documents (1,040 Cases)
│   ├── MASTER_TEST_CASE_REPOSITORY.md # Full Markdown register
│   ├── master_test_cases.csv          # Jira / TestRail / Xray CSV import
│   ├── master_test_cases.json         # Machine-readable JSON definition
│   └── master_test_dashboard.html     # Interactive search & filter matrix UI (Pagination & Modal details)
├── reports/                      # 📊 Generated Reports
│   ├── sample_summary_report.json
│   ├── sample_appium_report.html
│   ├── sample_selenium_report.html
│   ├── sample_vulnerability_report.html
│   └── load_test_report.html
├── mock_server/                  # 🚀 Lightweight Test Mock & Target Server
│   └── server.py
├── requirements.txt              # All testing dependencies
├── run_tests.py                  # Unified CLI Test Runner
├── create_zip_artifact.py        # Automated ZIP packaging script
└── README.md
```

---

## ⚡ Load & Performance Testing Categories (740 Scenarios)

The load testing suite contains **740 parameterized scenarios** defined in [load_scenarios_definitions.json](tests/load/load_scenarios_definitions.json):

1. **Normal Baseline Traffic (35 scenarios)**
2. **Concurrent User Scalability (35 scenarios)**
3. **Peak Traffic & Flash Crowds (35 scenarios)**
4. **API Endpoint Route Stress (40 scenarios)**
5. **Authentication & Token Storms (35 scenarios)**
6. **High-Throughput Read Operations (40 scenarios)**
7. **High-Frequency Transactional Writes (40 scenarios)**
8. **Large Payload & Volumetric Ingestion (35 scenarios)**
9. **Small Payload Micro-Telemetry Polling (35 scenarios)**
10. **Slow Network & High Latency Links (30 scenarios)**
11. **Burst Traffic & Square-Wave Pulses (35 scenarios)**
12. **Linear & Exponential Ramp-Up (30 scenarios)**
13. **Stepped Scale-Down & Ramp-Down (30 scenarios)**
14. **Sustained Soak & Endurance Testing (30 scenarios)**
15. **Stress Saturation & Breaking Points (35 scenarios)**
16. **Flash Impulse Spike Testing (30 scenarios)**
17. **Fault Inundation & Error Injection (35 scenarios)**
18. **Strict Latency SLA Gate Verification (35 scenarios)**
19. **Database-Heavy Complex Aggregations (35 scenarios)**
20. **Mixed Multi-Persona Clinical Workflows (35 scenarios)**
21. **Memory Pressure & Heap Profiling (25 scenarios)**
22. **Connection Pool Exhaustion Probes (25 scenarios)**

---

## 🚀 Quick Start & Local Execution

### 1. Install Dependencies
```bash
python -m pip install --upgrade pip
pip install -r requirements.txt
```

### 2. Run All Automated Test Suites via CLI Runner
```bash
python run_tests.py --suite all
```

### 3. Run Individual Categories
- **Appium Mobile:** `python run_tests.py --suite appium`
- **Selenium Web:** `python run_tests.py --suite selenium`
- **Security / Vulnerability:** `python run_tests.py --suite security`
- **Load & Performance:** `python run_tests.py --suite load --limit-load 50`

---

## 📦 Download Complete Project Artifact

To package or update the standalone downloadable ZIP archive:

```bash
python create_zip_artifact.py
```

This creates `automated_testing_project.zip` containing all 1,040 test cases, documentation, configs, and reports.
