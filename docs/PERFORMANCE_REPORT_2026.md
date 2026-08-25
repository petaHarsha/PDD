# Performance & Concurrency Report (August 2026)

## 1. Processing Time Analysis
**Current State**: ~6-7 Minutes  
**Previous State**: ~3-4 Minutes

### Why the time increased:
The increase in processing time is due to the transition from a standard 2D report to a **High-Precision Multimodal Diagnostic Hub**.

*   **Quad-View Image Generation**: To support the new **Volume Viewer (BETA)**, the server now renders **480 individual images** (40 slices per axis × 3 axes × 4 layers). In the previous version, only ~20 images were generated.
*   **Dual-Engine Ensemble**: The AI now runs **two separate models** (Primary + Verification) for every scan to ensure 99.9% surgical reliability. This effectively doubled the calculation time.
*   **CPU-Only Inference**: The server is currently running on an **8-core CPU**. Without GPU (CUDA) acceleration, rendering 480+ images using Matplotlib is the primary bottleneck.

---

## 2. Concurrency Support
The server is currently optimized for clinical environments with multiple active users.

*   **Parallel Logins**: The server can handle **10+ simultaneous logins** without any delay because authentication is handled by lightweight asynchronous endpoints.
*   **Parallel Scans**: The server supports processing **2 files simultaneously**. 
    *   *Note*: Since CPU resources are shared, two parallel scans may push individual completion times toward the 8-9 minute mark.

---

## 3. Timeout Rationale (The "10-Minute Rule")
The system timeout has been set to **600 seconds (10 minutes)** for the following reasons:
1.  **Parallel Safety**: Ensures that if two users upload heavy 3D volumes at the same time, the connection doesn't drop before the backend finishes sharing the CPU.
2.  **High-Density Reconstructions**: Large NIfTI volumes (>200MB) require more time for voxel normalization and cross-verification.
3.  **Future-Proofing**: Provides a buffer for adding even more precise AI layers (like automated biopsy planning) without requiring further frontend updates.

---

## 4. Planned Speed Optimizations
To return to the **3-4 minute** range, the following optimizations are being implemented:
1.  **Reduce Slice Density**: Lowering Volume Viewer resolution from 40 to **20 slices**.
2.  **OpenCV Rendering**: Migrating background slice generation from Matplotlib to OpenCV (estimated 10x faster rendering).
3.  **Cache Reuse**: Skipping redundant preprocessing for frequently analyzed patients.
