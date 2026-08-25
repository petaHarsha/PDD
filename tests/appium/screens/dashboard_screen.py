"""
Mobile Dashboard Screen Page Object
"""

from ..base_mobile_page import BaseMobilePage

class DashboardScreen(BaseMobilePage):
    DASHBOARD_TITLE = "accessibility_id:dashboard_title"
    BOTTOM_NAV_HOME = "accessibility_id:nav_home"
    BOTTOM_NAV_PATIENTS = "accessibility_id:nav_patients"
    BOTTOM_NAV_PREDICT = "accessibility_id:nav_predict"
    BOTTOM_NAV_CBCT = "accessibility_id:nav_cbct"
    STAT_CARD_SCANS = "accessibility_id:stat_total_scans"
    STAT_CARD_ACCURACY = "accessibility_id:stat_accuracy"
    OFFLINE_BANNER = "accessibility_id:banner_offline_mode"

    def is_loaded(self) -> bool:
        return self.is_element_visible("accessibility_id", "dashboard_title")

    def navigate_to_patients(self):
        self.click_element("accessibility_id", "nav_patients")

    def navigate_to_predict(self):
        self.click_element("accessibility_id", "nav_predict")

    def navigate_to_cbct(self):
        self.click_element("accessibility_id", "nav_cbct")

    def is_offline_banner_shown(self) -> bool:
        return self.is_element_visible("accessibility_id", "banner_offline_mode")
