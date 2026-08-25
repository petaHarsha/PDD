"""
Selenium Page Object: Patient CRUD Management Page
"""

from ..base_web_page import BaseWebPage

class PatientCRUDPage(BaseWebPage):
    ID_SEARCH_INPUT = "patient-search-input"
    ID_BTN_OPEN_MODAL = "btn-open-patient-modal"
    ID_INPUT_NAME = "new-patient-name"
    ID_INPUT_AGE = "new-patient-age"
    ID_INPUT_NOTES = "new-patient-notes"
    ID_BTN_SUBMIT = "btn-submit-patient"
    ID_TABLE_BODY = "patients-table-body"

    def navigate(self):
        self.open()
        self.click("id", "nav-patients")
        return self

    def add_patient(self, name: str, age: int, notes: str = ""):
        self.click("id", self.ID_BTN_OPEN_MODAL)
        self.enter_text("id", self.ID_INPUT_NAME, name)
        self.enter_text("id", self.ID_INPUT_AGE, str(age))
        if notes:
            self.enter_text("id", self.ID_INPUT_NOTES, notes)
        self.click("id", self.ID_BTN_SUBMIT)

    def search(self, query: str):
        self.enter_text("id", self.ID_SEARCH_INPUT, query)

    def get_table_rows_count(self) -> int:
        return len(self.find_all("css_selector", "#patients-table-body tr"))
