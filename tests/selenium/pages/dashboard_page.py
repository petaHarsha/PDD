"""
Selenium Page Object: Dashboard Page
"""

from ..base_web_page import BaseWebPage

class DashboardPage(BaseWebPage):
    ID_DASHBOARD_TITLE = "dashboard-title"
    ID_USER_DISPLAY = "user-display"
    ID_NAV_PATIENTS = "nav-patients"
    ID_NAV_PROGNOSIS = "nav-prognosis"
    ID_NAV_CBCT = "nav-cbct"

    def is_loaded(self) -> bool:
        return self.is_visible("id", self.ID_DASHBOARD_TITLE)

    def get_logged_in_user(self) -> str:
        return self.get_text("id", self.ID_USER_DISPLAY)

    def go_to_patients(self):
        self.click("id", self.ID_NAV_PATIENTS)

    def go_to_prognosis(self):
        self.click("id", self.ID_NAV_PROGNOSIS)

    def go_to_cbct(self):
        self.click("id", self.ID_NAV_CBCT)
