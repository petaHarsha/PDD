# Android Technical Stack

## 1. Programming Languages
- **Kotlin**: Primary language for app logic and UI (v1.9.23).
- **Java**: Used for auto-generated classes (e.g., `BuildConfig`).
- **Gradle Groovy/KTS**: Build configuration.

## 2. Framework & SDK Versions
- **Compile SDK**: 35
- **Target SDK**: 35
- **Minimum SDK**: 24 (Android 7.0)
- **Compose Compiler Extension**: 1.5.11

## 3. Architecture
The Android application implements the **MVVM (Model-View-ViewModel)** pattern combined with the **Repository Pattern** for data abstraction.
- **Model**: Data classes in `com.oralsurgeryai.app.data` (e.g., `CbctResponse`, `Patient`).
- **View**: Jetpack Compose-based screens in `com.oralsurgeryai.app.ui.screens`.
- **ViewModel**: State management using `CbctViewModel` and `LoginViewModel`.
- **Navigation**: Type-safe navigation using `androidx.navigation:navigation-compose`.

## 4. Key Libraries & Dependencies
| Category | Library | Version |
| :--- | :--- | :--- |
| **Networking** | Retrofit | 2.11.0 |
| **JSON** | GSON | 2.11.0 |
| **Image Loading** | Coil Compose | 2.7.0 |
| **DI / State** | ViewModel Compose | 2.8.4 |
| **Design** | Material 3 | 1.2.1 |
| **Icons** | Material Icons Extended | 1.7.0+ |
| **Concurrency** | Kotlin Coroutines | Integrated |

## 5. Build Tools
- **Gradle**: 9.0.0
- **Android Gradle Plugin (AGP)**: 8.5.0
- **Java**: JDK 17

## 6. App Resources
- **Layouts**: 100% Declarative via Jetpack Compose.
- **Drawables**: Vector-based XML icons and clinical logos.
- **Themes**: Material 3 Surgical Theme (Teal/Dark Mode optimized).
- **ML Integration**: Direct API interaction with the Python AI Backend.

## 7. Android Permissions
Detected in `AndroidManifest.xml`:
- `INTERNET`: Required for API interaction and scan uploads.
- `ACCESS_NETWORK_STATE`: For connectivity monitoring.
- `READ_EXTERNAL_STORAGE`: For file selection (legacy).
- `READ_MEDIA_IMAGES`: For clinical photo selection (modern).

## 8. Visualization Engine
The app contains a custom **Nerve Tracing Engine** (`NerveTracingEngine.kt`) that:
- Decodes Base64 clinical layers.
- Implements **Catmull-Rom Splines** via `SplineCalculator` for smooth anatomical rendering.
- Performs real-time **Implant Safety Analysis** using distance-to-trace calculations.
