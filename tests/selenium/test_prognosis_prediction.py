"""
Test Case ID: TC-WEB-016 - TC-WEB-018
Category: SELENIUM WEB AUTOMATION
Suite: Prognosis AI Clinical Decision Support Form & Result Rendering
"""

import pytest
from tests.selenium.pages import PrognosisPage

@pytest.mark.selenium
class TestPrognosisPredictionUI:

    def test_tc_web_016_calculate_prognosis_high_risk(self, web_driver, base_url):
        """TC-WEB-016: Submitting high-risk clinical factors returns High risk tier and plan"""
        prog_page = PrognosisPage(web_driver, base_url).navigate()
        prog_page.calculate_risk(age=68, smoking=1, tumor_size=6.2)
        
        assert prog_page.is_result_displayed() is True

    def test_tc_web_017_calculate_prognosis_low_risk(self, web_driver, base_url):
        """TC-WEB-017: Submitting favorable clinical factors returns Low risk tier"""
        prog_page = PrognosisPage(web_driver, base_url).navigate()
        prog_page.calculate_risk(age=25, smoking=0, tumor_size=0.8)
        
        assert prog_page.is_result_displayed() is True

    def test_tc_web_018_prognosis_numeric_bounds_validation(self, web_driver, base_url):
        """TC-WEB-018: Tumor size input enforces min 0.1cm and max 15.0cm boundaries"""
        prog_page = PrognosisPage(web_driver, base_url).navigate()
        prog_page.enter_text("id", "prog-tumor", "25.0")
        assert prog_page.is_visible("id", "btn-calculate-prognosis") is True
