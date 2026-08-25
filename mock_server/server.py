"""
Mock & Test API Server for Automated Testing Framework
Provides full API endpoints and lightweight Web UI for Selenium, Appium, Vulnerability, and Load tests.
Runs on port 8000.
"""

import os
import time
import base64
import random
import datetime
from typing import Optional, List, Dict
from fastapi import FastAPI, UploadFile, File, Form, HTTPException, Depends, Header, Request, status, Response
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import HTMLResponse, JSONResponse
from pydantic import BaseModel, Field

app = FastAPI(
    title="Oral Surgery AI - Mock Test Server",
    description="Automated Test Target System for Selenium, Appium, Security, and 300+ Load Test Cases",
    version="2.0.0-test"
)

# CORS Middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Custom Security Headers Middleware
@app.middleware("http")
async def add_security_headers(request: Request, call_next):
    # Simulated rate limiting tracker
    client_ip = request.client.host if request.client else "127.0.0.1"
    
    # Specific brute-force trip for rate-limiting tests
    if "/auth/verify-otp-bruteforce" in request.url.path:
        rate_store[client_ip] = rate_store.get(client_ip, 0) + 1
        if rate_store[client_ip] > 5:
            return JSONResponse(
                status_code=429,
                content={"detail": "Too Many Requests. Rate limit exceeded."},
                headers={"Retry-After": "60"}
            )
            
    response = await call_next(request)
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["X-Frame-Options"] = "DENY"
    response.headers["Content-Security-Policy"] = "default-src 'self' https: data: 'unsafe-inline' 'unsafe-eval';"
    response.headers["Strict-Transport-Security"] = "max-age=31536000; includeSubDomains"
    response.headers["Referrer-Policy"] = "strict-origin-when-cross-origin"
    return response

# In-Memory Test State
rate_store: Dict[str, int] = {}
patients_db = [
    {"id": "P-00101", "name": "Johnathan Doe", "age": 52, "date": "Jan 15, 2026", "risk": "Medium", "is_active": True, "notes": "Mandibular osteolytic lesion suspect."},
    {"id": "P-00102", "name": "Eleanor Vance", "age": 44, "date": "Feb 03, 2026", "risk": "Low", "is_active": True, "notes": "Routine pre-op 3D CBCT nerve mapping."},
    {"id": "P-00103", "name": "Arthur Pendelton", "age": 68, "date": "Feb 20, 2026", "risk": "High", "is_active": False, "notes": "Recurrent squamous cell carcinoma follow-up."},
    {"id": "P-00104", "name": "Maria Gonzalez", "age": 31, "date": "Mar 11, 2026", "risk": "Low", "is_active": True, "notes": "Impacted third molar proximity to IAN."},
    {"id": "P-00105", "name": "David Kim", "age": 58, "date": "Apr 02, 2026", "risk": "Medium", "is_active": True, "notes": "Bone graft site evaluation."}
]

users_db = [
    {"id": 1, "email": "master.admin@oralsurgery.ai", "full_name": "Dr. Master Administrator", "role": "MasterAdmin", "is_active": 1, "last_login": "2026-08-15T10:00:00"},
    {"id": 2, "email": "dr.smith@oralsurgery.ai", "full_name": "Dr. Sarah Smith MD", "role": "Surgeon", "is_active": 1, "last_login": "2026-08-15T11:30:00"},
    {"id": 3, "email": "patient.john@example.com", "full_name": "Johnathan Doe", "role": "User", "is_active": 1, "last_login": "2026-08-14T09:15:00"}
]

audit_logs = [
    {"id": 1, "user_email": "dr.smith@oralsurgery.ai", "action": "LOGIN", "timestamp": "2026-08-15 11:30:00", "details": "Successful Web Portal Login"},
    {"id": 2, "user_email": "dr.smith@oralsurgery.ai", "action": "PROGNOSIS_RUN", "timestamp": "2026-08-15 11:35:12", "details": "Calculated risk for patient P-00101"}
]

# Pydantic Request Models
class LoginModel(BaseModel):
    email: str
    password: str

class OTPRequestModel(BaseModel):
    email: str

class RegisterModel(BaseModel):
    email: str
    password: str
    full_name: str
    otp: str

class PasswordResetModel(BaseModel):
    email: str
    otp: str
    new_password: str

class PatientCreateModel(BaseModel):
    name: str = Field(..., min_length=2, max_length=100)
    age: int = Field(..., ge=1, le=120)
    patient_id: Optional[str] = None
    notes: Optional[str] = ""

class ClinicalDataModel(BaseModel):
    age: int = Field(..., ge=18, le=100)
    smoking_history: int = Field(..., ge=0, le=1)
    alcohol_history: int = Field(..., ge=0, le=1)
    tumor_size_cm: float = Field(..., ge=0.1, le=15.0)
    lymph_node_involvement: int = Field(..., ge=0, le=1)
    hpv_status: int = Field(..., ge=0, le=1)
    ian_invasion_detected: int = Field(..., ge=0, le=1)

# Health & Root
@app.get("/health")
def health_check():
    return {"status": "healthy", "service": "Oral Surgery AI Mock Backend", "timestamp": datetime.datetime.utcnow().isoformat()}

# Authentication Endpoints
@app.post("/auth/login")
def login(data: LoginModel):
    # SQL Injection Detection Simulation
    sqli_markers = ["'", "--", "OR 1=1", "UNION SELECT", "SLEEP(", "WAITFOR"]
    if any(marker.lower() in data.email.lower() or marker.lower() in data.password.lower() for marker in sqli_markers):
        # Securely sanitize and reject invalid logins instead of executing dynamic SQL
        raise HTTPException(status_code=401, detail="Invalid email or password")
        
    for user in users_db:
        if user["email"] == data.email and ("Password" in data.password or "admin" in data.password.lower() or data.password == "AdminSecurePassword123!" or data.password == "SurgeonSecurePassword123!" or data.password == "PatientSecurePassword123!" or data.password == "CIAdminPassword123!"):
            token = f"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test-session-token-{user['role']}.{base64.b64encode(user['email'].encode()).decode()}"
            return {
                "status": "success",
                "token": token,
                "user": {
                    "id": user["id"],
                    "name": user["full_name"],
                    "email": user["email"],
                    "role": user["role"]
                }
            }
    raise HTTPException(status_code=401, detail="Invalid email or password")

@app.post("/auth/request-registration-otp")
def request_otp(data: OTPRequestModel):
    if "@" not in data.email:
        raise HTTPException(status_code=400, detail="Invalid email format")
    return {"status": "success", "message": "Verification code sent to email (Mock: 123456)"}

@app.post("/auth/register")
def register(data: RegisterModel):
    if data.otp != "123456":
        raise HTTPException(status_code=400, detail="Invalid verification code")
    new_user = {
        "id": len(users_db) + 1,
        "email": data.email,
        "full_name": data.full_name,
        "role": "User",
        "is_active": 1,
        "last_login": datetime.datetime.utcnow().isoformat()
    }
    users_db.append(new_user)
    return {"status": "success", "message": "Registration complete. You can now login."}

@app.post("/auth/forgot-password")
def forgot_password(data: OTPRequestModel):
    return {"status": "success", "message": "OTP sent to email (Mock: 654321)"}

@app.post("/auth/reset-password")
def reset_password(data: PasswordResetModel):
    if data.otp != "654321":
        raise HTTPException(status_code=400, detail="Invalid OTP")
    return {"status": "success", "message": "Password updated successfully"}

@app.get("/auth/verify-otp-bruteforce")
def verify_otp_bruteforce(code: str):
    if code == "998877":
        return {"status": "success"}
    return JSONResponse(status_code=400, content={"detail": "Incorrect OTP"})

import html

@app.options("/{rest_of_path:path}")
def handle_options(rest_of_path: str):
    return Response(status_code=200, headers={
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers": "Content-Type, Authorization, X-Requested-With"
    })

# Patient Management Endpoints
@app.get("/patients")
def get_patients(search: Optional[str] = None):
    if search:
        filtered = [p for p in patients_db if search.lower() in p["name"].lower() or search.lower() in p["id"].lower()]
        return filtered
    return patients_db

@app.post("/patients")
def create_patient(data: PatientCreateModel):
    new_id = f"P-{random.randint(10000, 99999)}"
    sanitized_name = html.escape(data.name)
    sanitized_notes = html.escape(data.notes) if data.notes else ""
    patient = {
        "id": new_id,
        "name": sanitized_name,
        "age": data.age,
        "date": datetime.date.today().strftime("%b %d, %Y"),
        "risk": "High" if data.age > 60 else "Medium" if data.age > 40 else "Low",
        "is_active": True,
        "notes": sanitized_notes
    }
    patients_db.append(patient)
    return {"status": "success", "id": new_id, "patient": patient}

@app.get("/patients/{patient_id}")
def get_patient_detail(patient_id: str):
    for p in patients_db:
        if p["id"] == patient_id:
            return p
    raise HTTPException(status_code=404, detail="Patient not found")

@app.delete("/patients/{patient_id}")
def delete_patient(patient_id: str):
    global patients_db
    initial_len = len(patients_db)
    patients_db = [p for p in patients_db if p["id"] != patient_id]
    if len(patients_db) == initial_len:
        raise HTTPException(status_code=404, detail="Patient not found")
    return {"status": "success", "message": f"Patient {patient_id} deleted"}

@app.post("/patients/{patient_id}/toggle-status")
def toggle_patient_status(patient_id: str):
    for p in patients_db:
        if p["id"] == patient_id:
            p["is_active"] = not p["is_active"]
            return {"status": "success", "is_active": p["is_active"]}
    raise HTTPException(status_code=404, detail="Patient not found")

# Prognosis Prediction AI Endpoint
@app.post("/predict/prognosis")
def predict_prognosis(data: ClinicalDataModel):
    # Simulated Machine Learning inference calculation
    base_prob = 0.15
    if data.smoking_history == 1: base_prob += 0.20
    if data.alcohol_history == 1: base_prob += 0.15
    if data.tumor_size_cm > 4.0: base_prob += 0.25
    if data.lymph_node_involvement == 1: base_prob += 0.20
    if data.ian_invasion_detected == 1: base_prob += 0.15
    if data.hpv_status == 1: base_prob -= 0.10
    
    prob = min(max(base_prob, 0.05), 0.95)
    pred_class = 1 if prob >= 0.50 else 0
    
    if prob < 0.35:
        risk = "Low"
        plan = "Monitoring & Standard Follow-Up"
    elif prob < 0.65:
        risk = "Medium"
        plan = "Surgical Resection"
    else:
        risk = "High"
        plan = "Radical Surgery + Adjuvant Radiotherapy"
        
    return {
        "prediction_class": pred_class,
        "probability": round(prob, 4),
        "survival_2yr": f"{round((1.0 - prob) * 100, 1)}%",
        "risk_stratification": risk,
        "decision_support": {
            "suggested_plan": plan,
            "confidence": "High" if prob > 0.75 or prob < 0.25 else "Moderate",
            "note": "AI clinical decision support prediction based on trained multimodal model."
        },
        "top_features": [
            {"feature": "tumor_size_cm", "impact": "high"},
            {"feature": "lymph_node_involvement", "impact": "medium"},
            {"feature": "ian_invasion_detected", "impact": "medium"}
        ],
        "shap_image": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=="
    }

# CBCT Upload Endpoint
@app.post("/upload_cbct")
async def upload_cbct(file: UploadFile = File(...), demo_mode: str = Form("false")):
    filename = file.filename.lower()
    allowed_exts = [".nii", ".nii.gz", ".png", ".jpg", ".jpeg", ".bmp", ".dcm"]
    if not any(filename.endswith(ext) for ext in allowed_exts):
        raise HTTPException(status_code=400, detail="Unsupported file format")
        
    content = await file.read()
    if len(content) > 150 * 1024 * 1024:
        raise HTTPException(status_code=413, detail="File too large (max 150MB)")
        
    return {
        "status": "success",
        "file_name": file.filename,
        "mode": "3D CBCT Volumetric",
        "message": "Nerve localization segmentation completed successfully.",
        "slices_raw": ["dummy_base64_raw_slice"],
        "slices_right": ["dummy_base64_nerve_right"],
        "slices_left": ["dummy_base64_nerve_left"],
        "time_metrics": {
            "ai_time_seconds": 1.45,
            "manual_time": "5 minutes",
            "efficiency_gain": "≈95%"
        },
        "clinical_interpretation": f"Mandibular nerve canal trace identified cleanly with 0.89 Dice score for {file.filename}."
    }

# Admin & Audit Logs
@app.get("/admin/users")
def list_users():
    return users_db

@app.post("/admin/users/{user_id}/toggle-active")
def toggle_user(user_id: int):
    for u in users_db:
        if u["id"] == user_id:
            u["is_active"] = 0 if u["is_active"] == 1 else 1
            return {"status": "success", "is_active": u["is_active"]}
    raise HTTPException(status_code=404, detail="User not found")

@app.get("/admin/audit-logs")
def get_audit():
    return audit_logs

# Training Status Endpoints
@app.get("/train/status")
def training_status():
    return {"status": "idle", "last_run": "2026-08-15T08:00:00", "current_epoch": 0, "total_epochs": 50}

# Web Mock UI for Selenium Test Execution
@app.get("/", response_class=HTMLResponse)
def index_page():
    return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Oral Surgery AI - Clinical Portal</title>
        <style>
            :root { --primary: #0284c7; --bg: #0f172a; --card: #1e293b; --text: #f8fafc; }
            body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: var(--bg); color: var(--text); margin: 0; padding: 20px; }
            .container { max-width: 1000px; margin: 0 auto; }
            .card { background: var(--card); border-radius: 12px; padding: 24px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.3); }
            h1, h2 { color: #38bdf8; }
            input, select, button { padding: 10px 14px; border-radius: 8px; border: 1px solid #334155; background: #0f172a; color: white; margin: 6px 0; width: 100%; box-sizing: border-box; }
            button { background: var(--primary); cursor: pointer; font-weight: 600; border: none; transition: background 0.2s; }
            button:hover { background: #0369a1; }
            table { width: 100%; border-collapse: collapse; margin-top: 15px; }
            th, td { padding: 12px; text-align: left; border-bottom: 1px solid #334155; }
            th { color: #94a3b8; }
            .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; }
            .badge-low { background: #065f46; color: #6ee7b7; }
            .badge-med { background: #854d0e; color: #fde047; }
            .badge-high { background: #991b1b; color: #fca5a5; }
            .nav-bar { display: flex; gap: 15px; margin-bottom: 25px; border-bottom: 1px solid #334155; padding-bottom: 12px; }
            .nav-link { color: #94a3b8; text-decoration: none; font-weight: 500; cursor: pointer; }
            .nav-link.active { color: #38bdf8; border-bottom: 2px solid #38bdf8; }
            .hidden { display: none; }
            #error-message { color: #f87171; font-weight: 500; margin-top: 8px; }
            #success-message { color: #4ade80; font-weight: 500; margin-top: 8px; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="nav-bar">
                <a id="nav-dashboard" class="nav-link active" onclick="showTab('dashboard')">Dashboard</a>
                <a id="nav-patients" class="nav-link" onclick="showTab('patients')">Patients</a>
                <a id="nav-prognosis" class="nav-link" onclick="showTab('prognosis')">Prognosis AI</a>
                <a id="nav-cbct" class="nav-link" onclick="showTab('cbct')">CBCT Viewer</a>
                <a id="nav-login" class="nav-link" onclick="showTab('login')">Login</a>
            </div>

            <!-- Login View -->
            <div id="tab-login" class="card hidden">
                <h2 id="login-title">Clinical Portal Login</h2>
                <form id="login-form" onsubmit="handleLogin(event)">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" required placeholder="doctor@oralsurgery.ai">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required placeholder="••••••••">
                    <button type="submit" id="btn-login">Sign In</button>
                    <div id="error-message"></div>
                    <div id="success-message"></div>
                </form>
            </div>

            <!-- Dashboard View -->
            <div id="tab-dashboard" class="card">
                <h1 id="dashboard-title">Oral Surgery AI Dashboard</h1>
                <p>Welcome, <span id="user-display">Dr. Sarah Smith</span></p>
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-top: 20px;">
                    <div class="card" style="background:#0f172a;"><h3>124</h3><p>Total Scans Processed</p></div>
                    <div class="card" style="background:#0f172a;"><h3>98.2%</h3><p>Nerve Tracing Accuracy</p></div>
                    <div class="card" style="background:#0f172a;"><h3>5</h3><p>High Risk Recurrence Alerts</p></div>
                </div>
            </div>

            <!-- Patients CRUD View -->
            <div id="tab-patients" class="card hidden">
                <h2>Patient Management</h2>
                <input type="text" id="patient-search-input" placeholder="Search by name or ID..." onkeyup="filterPatients()">
                <button id="btn-open-patient-modal" onclick="toggleAddPatientForm()" style="width: auto; margin-bottom: 15px;">+ Add New Patient</button>
                
                <div id="new-patient-form-container" class="hidden" style="background:#0f172a; padding: 15px; border-radius: 8px; margin-bottom: 15px;">
                    <h3>New Patient Admission</h3>
                    <input type="text" id="new-patient-name" placeholder="Patient Full Name">
                    <input type="number" id="new-patient-age" placeholder="Age (1-120)">
                    <input type="text" id="new-patient-notes" placeholder="Clinical Notes">
                    <button id="btn-submit-patient" onclick="addPatient()">Save Patient Record</button>
                </div>

                <table id="patients-table">
                    <thead>
                        <tr>
                            <th>Patient ID</th>
                            <th>Name</th>
                            <th>Age</th>
                            <th>Risk Tier</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="patients-table-body">
                        <!-- Rendered by JS -->
                    </tbody>
                </table>
            </div>

            <!-- Prognosis Calculator View -->
            <div id="tab-prognosis" class="card hidden">
                <h2>Prognosis & Recurrence Risk Prediction</h2>
                <form id="prognosis-form" onsubmit="calculatePrognosis(event)">
                    <label for="prog-age">Age</label>
                    <input type="number" id="prog-age" value="55" min="18" max="100" required>
                    
                    <label for="prog-smoking">Smoking History</label>
                    <select id="prog-smoking">
                        <option value="1">Yes (Active / Former)</option>
                        <option value="0">No</option>
                    </select>

                    <label for="prog-alcohol">Alcohol Consumption</label>
                    <select id="prog-alcohol">
                        <option value="1">Yes</option>
                        <option value="0">No</option>
                    </select>

                    <label for="prog-tumor">Tumor Size (cm)</label>
                    <input type="number" step="0.1" id="prog-tumor" value="3.5" min="0.1" max="15.0" required>

                    <label for="prog-lymph">Lymph Node Involvement</label>
                    <select id="prog-lymph">
                        <option value="1">Positive (N+)</option>
                        <option value="0">Negative (N0)</option>
                    </select>

                    <label for="prog-hpv">HPV Status</label>
                    <select id="prog-hpv">
                        <option value="0">Negative</option>
                        <option value="1">Positive</option>
                    </select>

                    <label for="prog-ian">Inferior Alveolar Nerve (IAN) Invasion</label>
                    <select id="prog-ian">
                        <option value="1">Detected</option>
                        <option value="0">None</option>
                    </select>

                    <button type="submit" id="btn-calculate-prognosis">Run AI Risk Model</button>
                </form>

                <div id="prognosis-result" class="card hidden" style="background:#0f172a; margin-top:20px;">
                    <h3>Prediction Outcome: <span id="res-risk-tier" class="badge">Medium</span></h3>
                    <p>Recurrence Probability: <strong id="res-probability">48.5%</strong></p>
                    <p>2-Year Survival Expectancy: <strong id="res-survival">51.5%</strong></p>
                    <p>Recommended Clinical Action: <span id="res-plan">Surgical Resection</span></p>
                </div>
            </div>

            <!-- CBCT Viewer View -->
            <div id="tab-cbct" class="card hidden">
                <h2>3D CBCT Volumetric Analyzer</h2>
                <input type="file" id="cbct-file-input" accept=".nii,.nii.gz,.png,.jpg">
                <button id="btn-upload-cbct" onclick="simulateUpload()">Upload & Localize Nerve Canal</button>
                <div id="cbct-status" style="margin-top: 15px;"></div>
            </div>
        </div>

        <script>
            let patients = [
                {id: "P-00101", name: "Johnathan Doe", age: 52, risk: "Medium", status: "Active"},
                {id: "P-00102", name: "Eleanor Vance", age: 44, risk: "Low", status: "Active"},
                {id: "P-00103", name: "Arthur Pendelton", age: 68, risk: "High", status: "Archived"},
                {id: "P-00104", name: "Maria Gonzalez", age: 31, risk: "Low", status: "Active"}
            ];

            function showTab(name) {
                ['dashboard', 'patients', 'prognosis', 'cbct', 'login'].forEach(t => {
                    document.getElementById('tab-' + t).classList.add('hidden');
                    document.getElementById('nav-' + t).classList.remove('active');
                });
                document.getElementById('tab-' + name).classList.remove('hidden');
                document.getElementById('nav-' + name).classList.add('active');
                if (name === 'patients') renderPatientsTable();
            }

            function renderPatientsTable(list = patients) {
                const tbody = document.getElementById('patients-table-body');
                tbody.innerHTML = list.map(p => `
                    <tr id="row-${p.id}">
                        <td><strong>${p.id}</strong></td>
                        <td class="patient-name">${p.name}</td>
                        <td>${p.age}</td>
                        <td><span class="badge badge-${p.risk.toLowerCase()}">${p.risk}</span></td>
                        <td><span id="status-${p.id}">${p.status}</span></td>
                        <td>
                            <button onclick="toggleStatus('${p.id}')" style="width:auto; padding:4px 8px; font-size:12px;">Toggle</button>
                            <button onclick="deletePatient('${p.id}')" style="width:auto; padding:4px 8px; font-size:12px; background:#ef4444;">Delete</button>
                        </td>
                    </tr>
                `).join('');
            }

            function filterPatients() {
                const query = document.getElementById('patient-search-input').value.toLowerCase();
                const filtered = patients.filter(p => p.name.toLowerCase().includes(query) || p.id.toLowerCase().includes(query));
                renderPatientsTable(filtered);
            }

            function toggleAddPatientForm() {
                const form = document.getElementById('new-patient-form-container');
                form.classList.toggle('hidden');
            }

            function addPatient() {
                const name = document.getElementById('new-patient-name').value;
                const age = parseInt(document.getElementById('new-patient-age').value);
                if (!name || !age) return alert('Please enter name and age');
                const newP = {
                    id: 'P-' + Math.floor(10000 + Math.random() * 90000),
                    name,
                    age,
                    risk: age > 60 ? 'High' : (age > 40 ? 'Medium' : 'Low'),
                    status: 'Active'
                };
                patients.unshift(newP);
                renderPatientsTable();
                toggleAddPatientForm();
                document.getElementById('new-patient-name').value = '';
                document.getElementById('new-patient-age').value = '';
            }

            function toggleStatus(id) {
                const p = patients.find(x => x.id === id);
                if (p) {
                    p.status = p.status === 'Active' ? 'Archived' : 'Active';
                    renderPatientsTable();
                }
            }

            function deletePatient(id) {
                patients = patients.filter(x => x.id !== id);
                renderPatientsTable();
            }

            function handleLogin(e) {
                e.preventDefault();
                const email = document.getElementById('email').value;
                const pass = document.getElementById('password').value;
                const errMsg = document.getElementById('error-message');
                const succMsg = document.getElementById('success-message');
                
                errMsg.innerText = '';
                succMsg.innerText = '';

                if (email.includes('admin') || pass.includes('Password') || pass === 'AdminSecurePassword123!') {
                    succMsg.innerText = 'Login successful! Redirecting...';
                    setTimeout(() => showTab('dashboard'), 600);
                } else {
                    errMsg.innerText = 'Invalid email or password';
                }
            }

            function calculatePrognosis(e) {
                e.preventDefault();
                const age = parseInt(document.getElementById('prog-age').value);
                const smoking = parseInt(document.getElementById('prog-smoking').value);
                const tumor = parseFloat(document.getElementById('prog-tumor').value);
                
                let prob = 0.2 + (smoking * 0.2) + (tumor > 4 ? 0.3 : 0.1);
                prob = Math.min(prob, 0.92);
                
                document.getElementById('res-probability').innerText = (prob * 100).toFixed(1) + '%';
                document.getElementById('res-survival').innerText = ((1 - prob) * 100).toFixed(1) + '%';
                
                const tier = prob > 0.65 ? 'High' : (prob > 0.35 ? 'Medium' : 'Low');
                const badge = document.getElementById('res-risk-tier');
                badge.innerText = tier;
                badge.className = 'badge badge-' + tier.toLowerCase();
                
                document.getElementById('res-plan').innerText = tier === 'High' ? 'Radical Surgery + Adjuvant Radiotherapy' : (tier === 'Medium' ? 'Surgical Resection' : 'Monitoring & Follow-Up');
                document.getElementById('prognosis-result').classList.remove('hidden');
            }

            function simulateUpload() {
                const statusDiv = document.getElementById('cbct-status');
                statusDiv.innerHTML = '<span style="color:#38bdf8;">Analyzing scan with AI model...</span>';
                setTimeout(() => {
                    statusDiv.innerHTML = '<span style="color:#4ade80;">Success: Mandibular nerve canal trace identified (Dice Score: 0.91).</span>';
                }, 800);
            }

            // Initialize
            renderPatientsTable();
        </script>
    </body>
    </html>
    """

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)
