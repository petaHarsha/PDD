"""
Selenium Page Object: Prognosis AI Page
"""

from ..base_web_page import BaseWebPage

class PrognosisPage(BaseWebPage):
    ID_AGE = "prog-age"
    ID_SMOKING = "prog-smoking"
    ID_ALCOHOL = "prog-alcohol"
    ID_TUMOR = "prog-tumor"
    ID_LYMPH = "prog-lymph"
    ID_HPV = "prog-hpv"
    ID_IAN = "prog-ian"
    ID_CALCULATE_BTN = "btn-calculate-prognosis"
    ID_RESULT_CARD = "prognosis-result"
    ID_RES_PROBABILITY = "res-probability"
    ID_RES_RISK_TIER = "res-risk-tier"
    ID_RES_PLAN = "res-plan"

    def navigate(self):
        self.open()
        self.click("id", "nav-prognosis")
        return self

    def calculate_risk(self, age: int, smoking: int, tumor_size: float):
        self.enter_text("id", self.ID_AGE, str(age))
        self.enter_text("id", self.ID_TUMOR, str(tumor_size))
        self.click("id", self.ID_CALCULATE_BTN)

    def is_result_displayed(self) -> bool:
        return self.is_visible("id", self.ID_RESULT_CARD)

    def get_risk_tier(self) -> str:
        return self.get_text("id", self.ID_RES_RISK_TIER)
