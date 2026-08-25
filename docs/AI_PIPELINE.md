# AI Pipeline Documentation

## 1. Pipeline Overview
The Oral Surgery AI system employs a hierarchical diagnostic strategy, ranging from 1D tabular prognosis to 3D volumetric segmentation.

## 2. Models & Task Types
| Task | Model Architecture | Framework | Data Format |
| :--- | :--- | :--- | :--- |
| **IAN Nerve Segmentation** | nnU-Net v2 (3D Fullres) | PyTorch | NIfTI (.nii.gz) |
| **Fast Nerve Tracing** | AttentionUNet (2.5D) | MONAI / PyTorch | Multi-slice Stacks |
| **Prognosis Prediction** | RandomForest / XGBoost | Scikit-learn | Clinical Tabular |
| **Texture Verification** | Laplacian Variance | OpenCV | JPG/PNG |

## 3. Inference Pipelines
### A. The Penta-Planar Pipeline (2.5D)
To achieve near-instant inference, the system extracts five strategic views from the 3D volume:
1.  **Axial**: Horizontal cross-section.
2.  **Coronal**: Front-back cross-section.
3.  **Sagittal (Center)**: Left-right cross-section.
4.  **Sagittal (Left Offset)**: Offset for left-side verification.
5.  **Sagittal (Right Offset)**: Offset for right-side verification.
These are concatenated into a 5-channel 2D tensor and processed by a research-grade ensembled 2D model.

### B. The Multimodal Verification Gate
When a user uploads both a CBCT and a 2D clinical photo:
- The system computes the **Laplacian Variance** of the photo.
- Low scores (smooth textures like tongue) are used to filter out false positive tumor segmentations in the 3D space.

## 4. Preprocessing Logic
- **Intensity Normalization**: Hounsfield Units (HU) are clipped and scaled.
- **Resampling**: Automatic voxel spacing adjustment via MONAI.
- **Windowing**: Clinical windowing optimized for bone density.

## 5. Postprocessing & Metrics
- **Skeletonization**: Converts broad masks into 1px centerlines for surgical tracing.
- **Spline Calculation**: Uses Catmull-Rom or Cubic splines for visual smoothing.
- **Length Metric**: 3D Euclidean distance between trace points, adjusted by voxel spacing.
- **Dice Score**: Used during validation to ensure IAN localization accuracy (>75%).

## 6. Training Instructions
Training is triggered via the `/train/segmentation` endpoint or manually using `backend/ml/train_dental_ai.py`.
- **Hardware Requirement**: NVIDIA GPU (8GB+ VRAM recommended for nnU-Net).
- **Dataset**: `Dataset101_IAN` (stored in `Web-App/data/nnUNet_raw`).
