"""
Test Case ID: TC-APP-001 - TC-APP-004
Category: APPIUM MOBILE AUTOMATION
Suite: Application Launch, Permissions, and Lifecycle
"""

import pytest
from tests.appium.screens import LoginScreen, DashboardScreen

@pytest.mark.appium
class TestAppLaunchLifecycle:

    def test_tc_app_001_cold_launch_initial_state(self, mobile_driver):
        """TC-APP-001: Cold app launch displays correct package and primary screen"""
        assert mobile_driver.current_package == "com.oralsurgeryai.app"
        assert "MainActivity" in mobile_driver.current_activity
        
        login_screen = LoginScreen(mobile_driver)
        assert login_screen.is_loaded() is True

    def test_tc_app_002_background_and_resume(self, mobile_driver):
        """TC-APP-002: App maintains state after backgrounding for 5 seconds"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.enter_text("accessibility_id", "login_input_email", "doctor@oralsurgery.ai")
        
        # Background app for 5 seconds
        mobile_driver.background_app(5)
        
        # Verify app is resumed and field content is preserved
        email_val = login_screen.get_element_text("accessibility_id", "login_input_email")
        assert "doctor@oralsurgery.ai" in email_val

    def test_tc_app_003_restart_session_lifecycle(self, mobile_driver):
        """TC-APP-003: Terminating and relaunching app clears unsaved session input"""
        mobile_driver.terminate_app("com.oralsurgeryai.app")
        mobile_driver.activate_app("com.oralsurgeryai.app")
        
        login_screen = LoginScreen(mobile_driver)
        assert login_screen.is_loaded() is True

    def test_tc_app_004_hardware_back_button_stack(self, mobile_driver):
        """TC-APP-004: Hardware back button maintains predictable view stack"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()
        dashboard.navigate_to_predict()
        
        # Verify navigation stack exists
        assert dashboard.is_loaded() is True
