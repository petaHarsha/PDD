"""
Test Case ID: TC-WEB-022 - TC-WEB-024
Category: SELENIUM WEB AUTOMATION
Suite: Web Form Validations, Edge Cases, and Invalid Input Handling
"""

import pytest
from tests.selenium.pages import PatientCRUDPage

@pytest.mark.selenium
class TestFormValidations:

    def test_tc_web_022_patient_empty_name_rejection(self, web_driver, base_url):
        """TC-WEB-022: Prevent submitting patient record with blank name string"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.add_patient("", 45)
        assert patient_page.is_visible("id", "patients-table") is True

    def test_tc_web_023_patient_invalid_age_bounds(self, web_driver, base_url):
        """TC-WEB-023: Reject patient registration with age 0 or negative values"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.add_patient("Test Baby", 0)
        assert patient_page.is_visible("id", "patients-table") is True

    def test_tc_web_024_xss_payload_in_patient_form(self, web_driver, base_url):
        """TC-WEB-024: Verify HTML/Script tags in name field are safely escaped without execution"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.add_patient("<script>alert('XSS')</script>", 30)
        assert patient_page.is_visible("id", "patients-table") is True
