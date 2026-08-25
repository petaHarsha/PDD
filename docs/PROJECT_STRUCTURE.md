# Project Structure

## 1. Root Directory Tree
```text
C:\USERS\DELL\DOCUMENTS\WEBAPP PDD
|   build.gradle              # Top-level Android build script
|   settings.gradle           # Root project settings
|   clinical_data.db          # Local SQLite Database
|   tree_output.txt           # Generated directory tree
|   
+---Android-App               # Android Application Module
|   +---app                   # Core Android source code
|   |   +---src/main/java     # Kotlin source (ui, data, viewmodel)
|   |   \---src/main/res      # Android resources (icons, themes)
|   \---gradle                # Android build wrapper
|                       
+---Web-App                   # Web Application Module
|   +---backend               # FastAPI Python Server
|   |   +---app               # API & DB Logic
|   |   +---ml                # AI/ML Inference & Post-processing
|   |   +---research          # Custom model architectures
|   |   \---demosnerve        # Static tracing for demos
|   +---frontend              # React 19 / Vite UI
|   |   +---src/pages         # Screen definitions
|   |   \---src/components    # Reusable UI library
|   +---data                  # nnU-Net dataset structures
|   \---nnUNet                # Integrated nnU-Net framework
|           
+---docs                      # Technical Documentation (Current)
\---outputs                   # AI artifacts and results
    \---models                # Trained weights (.pth, .pkl)
```

## 2. Major Folder Descriptions

### `Android-App/app`
Contains the Kotlin/Compose implementation. Divided into:
- `ui/screens`: High-level page composables.
- `ui/nerve`: Specialized canvas rendering for clinical traces.
- `data`: API client (Retrofit) and data models.

### `Web-App/backend`
Asynchronous server logic.
- `ml/inference.py`: Logic to switch between fast (Penta-Planar) and deep (nnU-Net) inference.
- `app/api.py`: RESTful endpoint definitions and Pydantic validation.

### `Web-App/frontend`
Responsive React application.
- `src/store`: Unified state for managing heavy volumetric slice navigation.
- `src/pages`: Diagnostic viewers and patient management portals.

### `Web-App/data`
Adheres to the nnU-Net v2 directory structure (`nnUNet_raw`, `nnUNet_preprocessed`, `nnUNet_results`) for seamless model updates.
