"""
Mobile Login Screen Page Object (Jetpack Compose / UiAutomator)
"""

from ..base_mobile_page import BaseMobilePage

class LoginScreen(BaseMobilePage):
    # Locators (Appium / UiAutomator Accessibility IDs & XPaths)
    EMAIL_INPUT = "accessibility_id:login_input_email"
    PASSWORD_INPUT = "accessibility_id:login_input_password"
    LOGIN_BUTTON = "accessibility_id:login_button_submit"
    FORGOT_PASSWORD_BUTTON = "accessibility_id:login_button_forgot_password"
    ERROR_MESSAGE_LABEL = "accessibility_id:login_text_error"
    WELCOME_HEADER = "accessibility_id:login_header_title"
    LOGOUT_BUTTON = "accessibility_id:profile_button_logout"

    def is_loaded(self) -> bool:
        return self.is_element_visible("accessibility_id", "login_header_title")

    def login(self, email: str, password: str):
        self.enter_text("accessibility_id", "login_input_email", email)
        self.enter_text("accessibility_id", "login_input_password", password)
        self.click_element("accessibility_id", "login_button_submit")

    def click_forgot_password(self):
        self.click_element("accessibility_id", "login_button_forgot_password")

    def get_error_message(self) -> str:
        return self.get_element_text("accessibility_id", "login_text_error")

    def logout(self):
        self.click_element("accessibility_id", "profile_button_logout")
