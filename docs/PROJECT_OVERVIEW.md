# Project Overview: Oral Surgery AI

## 1. Project Name
**Oral Surgery AI**

## 2. Purpose
Oral Surgery AI is a multimodal diagnostic and decision-support platform designed to assist surgeons in identifying critical anatomical structures (specifically the Inferior Alveolar Nerve - IAN), detecting pathologies (tumors), and predicting cancer recurrence risks using advanced 3D and 2D AI pipelines.

## 3. Key Features
- **Autonomous Nerve Tracing**: AI-driven bilateral IAN localization from CBCT volumes.
- **Multimodal Inference**: Cross-verification of 3D volumetric data against 2D clinical photographs to reduce false positives.
- **2.5D Penta-Planar Viewer**: High-speed multi-planar reconstruction (MPR) and visualization optimized for both web and mobile.
- **Clinical Decision Support**: AI-recommended treatment plans and prognosis stratification based on patient clinical data.
- **Explainable AI (XAI)**: SHAP-based feature impact analysis for prognosis and visual saliency for segmentation.
- **Admin & Surgical Portals**: Role-based access control (Admin/Surgeon) with audit trailing and training capabilities.

## 4. Modules
- **Android Application**: Mobile surgical portal for on-the-go diagnostic review.
- **Web Application**: Desktop-optimized diagnostic hub for high-fidelity reconstruction and administration.
- **FastAPI Backend**: Shared AI engine and data persistence layer serving both platforms.
- **AI Pipeline**: Integrated nnU-Net framework and custom research architectures (Attention UNet).

## 5. Architecture Overview
The project follows a **Client-Server Architecture** with a centralized **RESTful API** backend.
- **Android**: MVVM (Model-View-ViewModel) with Jetpack Compose.
- **Web**: Component-based React with Vite and Zustand for state management.
- **Backend**: FastAPI with SQLAlchemy ORM, supporting both SQLite and PostgreSQL.

## 6. Technology Summary
- **Languages**: Kotlin (Android), Python (Backend/AI), JavaScript/React (Web).
- **AI Frameworks**: PyTorch, MONAI, nnU-Net v2, Scikit-learn.
- **Infrastructure**: FastAPI, Uvicorn, Vite, TailwindCSS.

## 7. Folder Structure (Major)
- `Android-App/`: Android source code and build configurations.
- `Web-App/`: Web project root.
    - `backend/`: Python API and ML logic.
    - `frontend/`: React source code.
    - `data/`: Dataset storage and AI workspace.
    - `nnUNet/`: Cloned nnU-Net v2 framework.
- `docs/`: Technical documentation.

## 8. Platform Support
- **Mobile**: Android 7.0 (API 24) and above.
- **Web**: Modern Evergreen Browsers (Chrome, Edge, Firefox, Safari).

## 9. Development Environment
- **IDE**: Android Studio, Visual Studio Code.
- **Versions**: Java 17, Python 3.10+, Node 20+, Gradle 9.0.
