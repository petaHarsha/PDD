"""
Test Case ID: TC-WEB-030
Category: SELENIUM WEB AUTOMATION
Suite: Cross-Browser Capabilities & Multi-Driver Support
"""

import pytest
from tests.selenium.pages import DashboardPage

@pytest.mark.selenium
class TestCrossBrowserCompatibility:

    def test_tc_web_030_cross_browser_capability_verification(self, web_driver, base_url):
        """TC-WEB-030: Web application initializes and renders core DOM across headless engines"""
        dashboard = DashboardPage(web_driver, base_url).open()
        assert dashboard.is_loaded() is True
        assert "Oral Surgery AI" in dashboard.get_page_title()
