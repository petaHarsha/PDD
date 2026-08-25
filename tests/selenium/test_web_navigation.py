"""
Test Case ID: TC-WEB-007 - TC-WEB-010
Category: SELENIUM WEB AUTOMATION
Suite: Web Navigation, Tab Switching, and Page Title Verification
"""

import pytest
from tests.selenium.pages import DashboardPage

@pytest.mark.selenium
class TestWebNavigation:

    def test_tc_web_007_dashboard_header_navigation(self, web_driver, base_url):
        """TC-WEB-007: Dashboard title and primary summary metrics render correctly"""
        dashboard = DashboardPage(web_driver, base_url).open()
        assert "Oral Surgery AI" in dashboard.get_page_title()
        assert dashboard.is_loaded() is True

    def test_tc_web_008_switch_to_patient_management(self, web_driver, base_url):
        """TC-WEB-008: Navigation bar switches smoothly to Patient management tab"""
        dashboard = DashboardPage(web_driver, base_url).open()
        dashboard.go_to_patients()
        assert dashboard.is_visible("id", "patient-search-input") is True

    def test_tc_web_009_switch_to_prognosis_calculator(self, web_driver, base_url):
        """TC-WEB-009: Navigation bar switches smoothly to Prognosis calculator tab"""
        dashboard = DashboardPage(web_driver, base_url).open()
        dashboard.go_to_prognosis()
        assert dashboard.is_visible("id", "btn-calculate-prognosis") is True

    def test_tc_web_010_switch_to_cbct_viewer(self, web_driver, base_url):
        """TC-WEB-010: Navigation bar switches smoothly to 3D CBCT viewer tab"""
        dashboard = DashboardPage(web_driver, base_url).open()
        dashboard.go_to_cbct()
        assert dashboard.is_visible("id", "btn-upload-cbct") is True
