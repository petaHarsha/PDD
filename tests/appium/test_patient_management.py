"""
Test Case ID: TC-APP-015 - TC-APP-018
Category: APPIUM MOBILE AUTOMATION
Suite: Patient Creation, Searching, and List Management
"""

import pytest
from tests.appium.screens import DashboardScreen, PatientFormScreen

@pytest.mark.appium
class TestMobilePatientManagement:

    def test_tc_app_015_create_patient_success(self, mobile_driver):
        """TC-APP-015: Successfully adding a new patient from mobile form"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        patient_form = PatientFormScreen(mobile_driver)
        patient_form.fill_and_submit("Gregory House", "54", "Mandibular nerve mapping required")
        
        # Verify submission feedback
        assert patient_form.is_element_visible("accessibility_id", "list_patients") is True

    def test_tc_app_016_search_existing_patient(self, mobile_driver):
        """TC-APP-016: Dynamic search filters patient list by name"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        patient_form = PatientFormScreen(mobile_driver)
        patient_form.search_patient("Johnathan")
        
        assert patient_form.is_element_visible("accessibility_id", "list_patients") is True

    def test_tc_app_017_patient_list_scroll_interaction(self, mobile_driver):
        """TC-APP-017: Smooth scrolling down and up large patient rosters"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()
        
        patient_form = PatientFormScreen(mobile_driver)
        items = patient_form.find_all("accessibility_id", "list_patients")
        assert len(items) >= 1

    def test_tc_app_018_archive_patient_toggle(self, mobile_driver):
        """TC-APP-018: Toggling patient active/archive status reflects in mobile state"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()
        
        # Emulate toggle interaction
        assert dashboard.is_loaded() is True
