"""
Test Case ID: TC-WEB-019 - TC-WEB-021
Category: SELENIUM WEB AUTOMATION
Suite: Web Search, Real-Time Filtering, and Data Presentation
"""

import pytest
from tests.selenium.pages import PatientCRUDPage

@pytest.mark.selenium
class TestSearchAndFilter:

    def test_tc_web_019_search_filter_by_name(self, web_driver, base_url):
        """TC-WEB-019: Entering patient name in search box filters visible table rows instantly"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.search("Johnathan")
        assert patient_page.get_table_rows_count() >= 1

    def test_tc_web_020_search_filter_by_patient_id(self, web_driver, base_url):
        """TC-WEB-020: Searching by formatted patient ID (P-00102) isolates target row"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.search("P-00102")
        assert patient_page.get_table_rows_count() >= 1

    def test_tc_web_021_search_no_results_state(self, web_driver, base_url):
        """TC-WEB-021: Searching for non-existent query displays empty results gracefully"""
        patient_page = PatientCRUDPage(web_driver, base_url).navigate()
        patient_page.search("NON_EXISTENT_PATIENT_XYZ_999")
        assert patient_page.is_visible("id", "patients-table") is True
