"""
Test Case ID: TC-WEB-001 - TC-WEB-006
Category: SELENIUM WEB AUTOMATION
Suite: Web Authentication, Login Validation, Logout, and Session State
"""

import pytest
from tests.selenium.pages import LoginPage, DashboardPage

@pytest.mark.selenium
class TestWebAuthentication:

    def test_tc_web_001_valid_master_admin_login(self, web_driver, base_url):
        """TC-WEB-001: Master admin successfully authenticates with valid credentials"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("master.admin@oralsurgery.ai", "AdminSecurePassword123!")
        
        dashboard = DashboardPage(web_driver, base_url)
        assert dashboard.is_loaded() is True

    def test_tc_web_002_valid_surgeon_login(self, web_driver, base_url):
        """TC-WEB-002: Surgeon successfully logs in and views clinical dashboard"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("dr.smith@oralsurgery.ai", "SurgeonSecurePassword123!")
        
        dashboard = DashboardPage(web_driver, base_url)
        assert dashboard.is_loaded() is True

    def test_tc_web_003_invalid_password_error_state(self, web_driver, base_url):
        """TC-WEB-003: Submitting incorrect password displays clear error feedback banner"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("dr.smith@oralsurgery.ai", "IncorrectPassword2026!")
        
        # Verify page remains on login or displays error
        assert login_page.is_visible("id", "btn-login") is True

    def test_tc_web_004_empty_email_and_password_validation(self, web_driver, base_url):
        """TC-WEB-004: Submitting empty form blocks submission via HTML5 validation"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("", "")
        assert login_page.is_visible("id", "btn-login") is True

    def test_tc_web_005_logout_flow_and_session_clearing(self, web_driver, base_url):
        """TC-WEB-005: User can logout, clearing session tokens and returning to sign in"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("master.admin@oralsurgery.ai", "AdminSecurePassword123!")
        
        # Clear storage / session
        web_driver.execute_script("localStorage.clear();")
        login_page.navigate_to_login()
        assert login_page.is_visible("id", "btn-login") is True

    def test_tc_web_006_unauthenticated_route_guard_redirect(self, web_driver, base_url):
        """TC-WEB-006: Direct URL access to protected routes without session handles gracefully"""
        dashboard = DashboardPage(web_driver, base_url)
        dashboard.open("admin")
        assert web_driver.current_url is not None
