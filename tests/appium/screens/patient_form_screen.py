"""
Mobile Patient Form & List Screen Page Object
"""

from ..base_mobile_page import BaseMobilePage

class PatientFormScreen(BaseMobilePage):
    NAME_INPUT = "accessibility_id:input_patient_name"
    AGE_INPUT = "accessibility_id:input_patient_age"
    NOTES_INPUT = "accessibility_id:input_patient_notes"
    SAVE_BUTTON = "accessibility_id:btn_save_patient"
    VALIDATION_ERROR_LABEL = "accessibility_id:text_validation_error"
    PATIENT_LIST_CONTAINER = "accessibility_id:list_patients"
    SEARCH_INPUT = "accessibility_id:input_search_patient"

    def fill_and_submit(self, name: str, age: str, notes: str = ""):
        self.enter_text("accessibility_id", "input_patient_name", name)
        self.enter_text("accessibility_id", "input_patient_age", str(age))
        if notes:
            self.enter_text("accessibility_id", "input_patient_notes", notes)
        self.click_element("accessibility_id", "btn_save_patient")

    def search_patient(self, query: str):
        self.enter_text("accessibility_id", "input_search_patient", query)

    def get_validation_error(self) -> str:
        return self.get_element_text("accessibility_id", "text_validation_error")
