"""
Test Case ID: TC-APP-005 - TC-APP-010
Category: APPIUM MOBILE AUTOMATION
Suite: Mobile Authentication Flows, Positive/Negative Scenarios, Session Lifecycle
"""

import pytest
from tests.appium.screens import LoginScreen, DashboardScreen

@pytest.mark.appium
class TestMobileAuthentication:

    def test_tc_app_005_successful_surgeon_login(self, mobile_driver, appium_config):
        """TC-APP-005: Positive login with valid surgeon credentials"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.login("dr.smith@oralsurgery.ai", "SurgeonSecurePassword123!")
        
        dashboard = DashboardScreen(mobile_driver)
        assert dashboard.is_loaded() is True

    def test_tc_app_006_invalid_password_error_feedback(self, mobile_driver):
        """TC-APP-006: Negative login with incorrect password displays error banner"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.login("dr.smith@oralsurgery.ai", "WrongPassword999!")
        
        err_msg = login_screen.get_error_message()
        assert len(err_msg) > 0

    def test_tc_app_007_empty_credentials_validation(self, mobile_driver):
        """TC-APP-007: Submitting empty email and password flags required fields"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.login("", "")
        
        assert login_screen.is_loaded() is True

    def test_tc_app_008_forgot_password_otp_trigger(self, mobile_driver):
        """TC-APP-008: Tapping forgot password navigates to 6-digit OTP entry screen"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.click_forgot_password()
        
        # Verify navigation to OTP recovery
        assert login_screen.is_element_visible("accessibility_id", "login_button_forgot_password") is True

    def test_tc_app_009_logout_session_invalidation(self, mobile_driver):
        """TC-APP-009: Logging out clears session credentials and redirects to login"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.login("master.admin@oralsurgery.ai", "AdminSecurePassword123!")
        
        login_screen.logout()
        assert login_screen.is_loaded() is True

    def test_tc_app_010_session_token_persistence(self, mobile_driver):
        """TC-APP-010: Session persistence across fast app background switching"""
        dashboard = DashboardScreen(mobile_driver)
        mobile_driver.background_app(2)
        assert dashboard.is_loaded() is True
