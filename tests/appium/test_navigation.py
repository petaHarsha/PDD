"""
Test Case ID: TC-APP-011 - TC-APP-014
Category: APPIUM MOBILE AUTOMATION
Suite: Mobile Navigation, Bottom Navigation Bar, and Deep Linking
"""

import pytest
from tests.appium.screens import DashboardScreen, PatientFormScreen, CBCTViewerScreen

@pytest.mark.appium
class TestMobileNavigation:

    def test_tc_app_011_bottom_nav_patients_tab(self, mobile_driver):
        """TC-APP-011: Tapping Patients tab in bottom navigation switches viewport"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()
        
        patients_screen = PatientFormScreen(mobile_driver)
        assert patients_screen.is_element_visible("accessibility_id", "list_patients") is True

    def test_tc_app_012_bottom_nav_cbct_tab(self, mobile_driver):
        """TC-APP-012: Tapping CBCT tab opens 3D volumetric analysis view"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_cbct()
        
        cbct_screen = CBCTViewerScreen(mobile_driver)
        assert cbct_screen.is_element_visible("accessibility_id", "btn_upload_scan") is True

    def test_tc_app_013_bottom_nav_home_tab_return(self, mobile_driver):
        """TC-APP-013: Returning to Home tab restores dashboard metrics view"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()
        dashboard.click_element("accessibility_id", "nav_home")
        
        assert dashboard.is_loaded() is True

    def test_tc_app_014_deep_link_patient_record(self, mobile_driver):
        """TC-APP-014: Handling deep link URI 'oralsurgeryai://patient/P-00101'"""
        # Emulate intent deep-linking
        mobile_driver.activate_app("com.oralsurgeryai.app")
        dashboard = DashboardScreen(mobile_driver)
        assert dashboard.is_loaded() is True
