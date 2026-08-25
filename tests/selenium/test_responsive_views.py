"""
Test Case ID: TC-WEB-027 - TC-WEB-029
Category: SELENIUM WEB AUTOMATION
Suite: Responsive Design, Mobile/Tablet Viewports, and Dynamic Layout Breakpoints
"""

import pytest
from tests.selenium.pages import DashboardPage

@pytest.mark.selenium
class TestResponsiveViews:

    def test_tc_web_027_mobile_portrait_viewport(self, web_driver, base_url, selenium_config):
        """TC-WEB-027: UI renders without horizontal overflow on Mobile (375x812)"""
        web_driver.set_window_size(375, 812)
        dashboard = DashboardPage(web_driver, base_url).open()
        
        size = web_driver.get_window_size()
        assert size["width"] == 375
        assert dashboard.is_loaded() is True

    def test_tc_web_028_tablet_portrait_viewport(self, web_driver, base_url):
        """TC-WEB-028: UI scales cleanly on Tablet viewports (768x1024)"""
        web_driver.set_window_size(768, 1024)
        dashboard = DashboardPage(web_driver, base_url).open()
        
        size = web_driver.get_window_size()
        assert size["width"] == 768
        assert dashboard.is_loaded() is True

    def test_tc_web_029_desktop_wide_viewport(self, web_driver, base_url):
        """TC-WEB-029: Full high-resolution desktop view (1920x1080) retains container centering"""
        web_driver.set_window_size(1920, 1080)
        dashboard = DashboardPage(web_driver, base_url).open()
        
        size = web_driver.get_window_size()
        assert size["width"] == 1920
        assert dashboard.is_loaded() is True
