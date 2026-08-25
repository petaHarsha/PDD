# Web Technical Stack

## 1. Frontend Architecture
- **Framework**: React 19 (Vite-powered).
- **Styling**: TailwindCSS 3.4 for responsive, utility-first design.
- **State Management**: Zustand 5.0 (Lightweight, atomic state).
- **Routing**: React Router 7.
- **Visuals**: Lucide React for iconography, Framer Motion for surgical UI transitions.

## 2. Backend Architecture
- **Framework**: FastAPI (Asynchronous Python).
- **Server**: Uvicorn.
- **Persistence**: SQLAlchemy ORM with a unified database adapter.
- **Multimodal Integration**: Custom logic to correlate 3D CBCT (NIfTI) and 2D Photos (JPEG/PNG).

## 3. Database
- **Primary Engine**: SQLite (for local edge deployment).
- **Cloud Support**: PostgreSQL (Configurable via `DATABASE_URL`).
- **Features**: Automatic migrations, user seeding, and audit logging.

## 4. Python ML/AI Stack
| Package | Version (Approx) | Purpose |
| :--- | :--- | :--- |
| **PyTorch** | 2.x | Model training and inference. |
| **nnUNet** | v2 | 3D Segmentation framework. |
| **MONAI** | 1.x | Medical imaging preprocessing and U-Net blocks. |
| **Nibabel** | Integrated | NIfTI file I/O. |
| **Scikit-learn** | Integrated | Prognosis model (RandomForest/XGBoost). |
| **SHAP** | Integrated | Explainable AI feature impact maps. |
| **Pandas** | Integrated | Clinical data manipulation. |
| **OpenCV** | Integrated | 2D image processing and photo verification. |

## 5. Node.js Dependency Table
Exracted from `package.json`:
| Package | Version | Usage |
| :--- | :--- | :--- |
| `react` | 19.2.5 | UI Framework |
| `vite` | 8.0.10 | Build Tool |
| `tailwindcss` | 3.4.19 | Styling |
| `zustand` | 5.0.12 | State |
| `axios` | 1.15.2 | API Client |
| `framer-motion` | 12.4.7 | Animations |
| `html2canvas` | 1.4.1 | Export |
| `jspdf` | 4.2.1 | PDF Gen |

## 6. AI Integration Layer
The web app communicates with the backend via the `/upload_cbct` and `/analyze/oral-health` endpoints. It receives high-frequency slice data as **Base64-encoded PNG layers** to ensure compatibility across all browsing environments without specialized medical plugins.
