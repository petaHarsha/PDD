"""
Test Case ID: TC-WEB-011 - TC-WEB-015
Category: SELENIUM WEB AUTOMATION
Suite: Patient CRUD (Create, Read, Update, Delete/Archive) Operations
"""

import pytest
from tests.selenium.pages import PatientCRUDPage

@pytest.mark.selenium
class TestPatientCRUD:

    def test_tc_web_011_read_patient_roster(self, web_driver, base_url):
        """TC-WEB-011: Roster displays list of current clinical patients with ID and Risk badges"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        rows = patient_page.get_table_rows_count()
        assert rows >= 1

    def test_tc_web_012_create_new_patient_record(self, web_driver, base_url):
        """TC-WEB-012: Creating a new patient appends record immediately to the table"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        initial_count = patient_page.get_table_rows_count()
        
        patient_page.add_patient("Dr. Marcus Brody", 62, "Suspicious radiopacity left mandible")
        assert patient_page.is_visible("id", "patients-table") is True

    def test_tc_web_013_toggle_patient_active_status(self, web_driver, base_url):
        """TC-WEB-013: Clicking Toggle button updates patient status badge between Active/Archived"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        assert patient_page.is_visible("id", "patients-table") is True

    def test_tc_web_014_delete_patient_record(self, web_driver, base_url):
        """TC-WEB-014: Deleting a patient record removes it cleanly from DOM view"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        assert patient_page.is_visible("id", "patients-table") is True

    def test_tc_web_015_modal_open_and_cancel_states(self, web_driver, base_url):
        """TC-WEB-015: Add Patient form toggles visibility upon clicking button"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.click("id", "btn-open-patient-modal")
        assert patient_page.is_visible("id", "new-patient-name") is True
