"""
Mobile CBCT Viewer & AI Localization Screen Page Object
"""

from ..base_mobile_page import BaseMobilePage

class CBCTViewerScreen(BaseMobilePage):
    UPLOAD_SCAN_BUTTON = "accessibility_id:btn_upload_scan"
    SLICE_SLIDER = "accessibility_id:slider_slice_index"
    CONTRAST_TOGGLE = "accessibility_id:btn_toggle_contrast"
    NERVE_OVERLAY_CHECKBOX = "accessibility_id:cb_nerve_overlay"
    INFERENCE_STATUS_LABEL = "accessibility_id:text_inference_status"
    CANAL_DISTANCE_LABEL = "accessibility_id:text_canal_distance"

    def upload_scan_file(self, filename: str):
        self.click_element("accessibility_id", "btn_upload_scan")

    def toggle_nerve_overlay(self):
        self.click_element("accessibility_id", "cb_nerve_overlay")

    def get_inference_status(self) -> str:
        return self.get_element_text("accessibility_id", "text_inference_status")
