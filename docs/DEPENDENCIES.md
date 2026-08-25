# Project Dependencies

## 1. Android Dependencies (Kotlin/Gradle)
Exracted from `app/build.gradle`:
| Dependency | Version | Type |
| :--- | :--- | :--- |
| `androidx.core:core-ktx` | 1.13.1 | Core |
| `androidx.compose.ui:ui` | BOM 2024.06.00 | UI |
| `androidx.navigation:navigation-compose` | 2.7.7 | Navigation |
| `com.squareup.retrofit2:retrofit` | 2.11.0 | Networking |
| `com.squareup.okhttp3:logging-interceptor` | 4.12.0 | Networking |
| `io.coil-kt:coil-compose` | 2.7.0 | Image Loading |
| `junit:junit` | 4.13.2 | Test |

## 2. Python Dependencies (FastAPI/AI)
Extracted from `requirements.txt`:
| Package | Purpose |
| :--- | :--- |
| `fastapi` | Web Framework |
| `uvicorn` | Server |
| `sqlalchemy` | ORM |
| `torch` | Deep Learning |
| `monai` | Medical Imaging |
| `nibabel` | NIfTI Handling |
| `scipy` | Math/Splines |
| `pandas` | Data Science |
| `scikit-learn` | Tabular ML |
| `shap` | Explainable AI |

## 3. Node.js Dependencies (React/Vite)
Extracted from `package.json`:
| Package | Version | Usage |
| :--- | :--- | :--- |
| `react` | ^19.2.5 | UI |
| `zustand` | ^5.0.12 | State |
| `framer-motion` | ^12.4.7 | Animation |
| `lucide-react` | ^1.11.0 | Icons |
| `axios` | ^1.15.2 | HTTP |
| `tailwindcss` | ^3.4.19 | CSS |

## 4. Internal Components
- **nnU-Net v2**: Self-contained within the `Web-App/nnUNet` directory.
- **Custom Research Models**: Located in `backend/ml/research/`.
- **Pre-trained Weights**: `.pth` and `.pkl` files in `outputs/models/`.
