# Build and Run Instructions

## 1. Prerequisites
- **Python**: 3.10 or 3.13.
- **Node.js**: 20+ (with npm/yarn).
- **Java**: JDK 17.
- **Android Studio**: Ladybug or newer.
- **Hardware**: NVIDIA GPU highly recommended for AI training/inference.

## 2. Backend Setup
1.  Navigate to `Web-App/`.
2.  Install dependencies:
    ```bash
    pip install -r requirements.txt
    ```
3.  Initialize Database:
    ```bash
    python backend/initialize_db.py
    ```
4.  Run Server:
    ```bash
    python -m uvicorn backend.app.api:app --reload --port 8000
    ```

## 3. Frontend Setup
1.  Navigate to `Web-App/frontend/`.
2.  Install packages:
    ```bash
    npm install
    ```
3.  Run Dev Server:
    ```bash
    npm run dev
    ```

## 4. Android Setup
1.  Open the `Android-App` folder in Android Studio.
2.  Sync Project with Gradle Files.
3.  **Build APK**: Go to `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
4.  **Run**: Press `Shift + F10` with a device/emulator connected.

## 5. Automated Scripts (Windows)
The project includes `.bat` files in the root for quick orchestration:
- `start_server.bat`: Launches the FastAPI backend.
- `start_frontend.bat`: Launches the Vite dev server.
- `train_dental_ai.bat`: Triggers the training pipeline.
- `run_clinical_validation.bat`: Executes validation tests.
