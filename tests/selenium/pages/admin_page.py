"""
Selenium Page Object: Admin & Audit Logs Page
"""

from ..base_web_page import BaseWebPage

class AdminPage(BaseWebPage):
    ID_USER_TABLE = "admin-users-table"
    ID_AUDIT_LOG_TABLE = "admin-audit-table"
    ID_BTN_TOGGLE_ACTIVE = "btn-toggle-active"

    def navigate(self):
        self.open("admin")
        return self

    def is_user_table_present(self) -> bool:
        return self.is_visible("id", self.ID_USER_TABLE)
