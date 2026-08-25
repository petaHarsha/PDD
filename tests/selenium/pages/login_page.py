"""
Selenium Page Object: Login Page
"""

from ..base_web_page import BaseWebPage

class LoginPage(BaseWebPage):
    ID_EMAIL = "email"
    ID_PASSWORD = "password"
    ID_LOGIN_BTN = "btn-login"
    ID_ERROR_MSG = "error-message"
    ID_SUCCESS_MSG = "success-message"
    ID_NAV_LOGIN = "nav-login"

    def navigate_to_login(self):
        self.open()
        self.click("id", self.ID_NAV_LOGIN)
        return self

    def login(self, email: str, password: str):
        self.enter_text("id", self.ID_EMAIL, email)
        self.enter_text("id", self.ID_PASSWORD, password)
        self.click("id", self.ID_LOGIN_BTN)
        return self

    def get_error_text(self) -> str:
        return self.get_text("id", self.ID_ERROR_MSG)

    def get_success_text(self) -> str:
        return self.get_text("id", self.ID_SUCCESS_MSG)
