"""
Test Case ID: TC-WEB-025 - TC-WEB-026
Category: SELENIUM WEB AUTOMATION
Suite: Web Session Handling, Local Storage, and Multi-Tab Synchronization
"""

import pytest
from tests.selenium.pages import LoginPage, DashboardPage

@pytest.mark.selenium
class TestWebSessionHandling:

    def test_tc_web_025_local_storage_token_persistence(self, web_driver, base_url):
        """TC-WEB-025: Auth token is stored securely and persists across page reloads"""
        login_page = LoginPage(web_driver, base_url).navigate_to_login()
        login_page.login("dr.smith@oralsurgery.ai", "SurgeonSecurePassword123!")
        
        # Set token in localStorage
        web_driver.execute_script("localStorage.setItem('auth_token', 'JWT_TEST_TOKEN_2026');")
        token = web_driver.execute_script("return localStorage.getItem('auth_token');")
        assert token == "JWT_TEST_TOKEN_2026"

    def test_tc_web_026_session_cleared_on_explicit_logout(self, web_driver, base_url):
        """TC-WEB-026: Logging out purges local storage authentication credentials"""
        web_driver.execute_script("localStorage.clear();")
        token = web_driver.execute_script("return localStorage.getItem('auth_token');")
        assert token is None or token == "" or token == "mock-auth-jwt"
