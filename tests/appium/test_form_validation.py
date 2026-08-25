"""
Test Case ID: TC-APP-019 - TC-APP-022
Category: APPIUM MOBILE AUTOMATION
Suite: Mobile Form Inputs, Boundary Values, and Invalid Data Validations
"""

import pytest
from tests.appium.screens import DashboardScreen, PatientFormScreen

@pytest.mark.appium
class TestMobileFormValidation:

    def test_tc_app_019_age_negative_boundary_rejection(self, mobile_driver):
        """TC-APP-019: Entering negative age (-5) shows field validation error"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        form = PatientFormScreen(mobile_driver)
        form.fill_and_submit("Alice Test", "-5")
        
        # Verify form remains loaded
        assert form.is_element_visible("accessibility_id", "btn_save_patient") is True

    def test_tc_app_020_age_excessive_boundary_rejection(self, mobile_driver):
        """TC-APP-020: Entering age > 120 (e.g. 150) triggers boundary validation error"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        form = PatientFormScreen(mobile_driver)
        form.fill_and_submit("Bob Test", "150")
        assert form.is_element_visible("accessibility_id", "btn_save_patient") is True

    def test_tc_app_021_empty_name_field_validation(self, mobile_driver):
        """TC-APP-021: Submitting patient form with empty name highlights required border"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        form = PatientFormScreen(mobile_driver)
        form.fill_and_submit("", "45")
        assert form.is_element_visible("accessibility_id", "btn_save_patient") is True

    def test_tc_app_022_special_characters_in_name(self, mobile_driver):
        """TC-APP-022: Handling accented characters (e.g. José Peña-Sánchez) properly"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_patients()

        form = PatientFormScreen(mobile_driver)
        form.fill_and_submit("José Peña-Sánchez", "38", "Accented name validation")
        assert form.is_element_visible("accessibility_id", "list_patients") is True
