"""
Base Mobile Page Object Model (POM) for Appium
Encapsulates mobile element lookups, gestures, waits, and assertions.
"""

from typing import Tuple, List

class BaseMobilePage:
    def __init__(self, driver):
        self.driver = driver

    def find(self, by: str, locator: str):
        return self.driver.find_element(by, locator)

    def find_all(self, by: str, locator: str) -> List:
        return self.driver.find_elements(by, locator)

    def click_element(self, by: str, locator: str):
        elem = self.find(by, locator)
        elem.click()
        return elem

    def enter_text(self, by: str, locator: str, text: str):
        elem = self.find(by, locator)
        elem.clear()
        elem.send_keys(text)
        return elem

    def get_element_text(self, by: str, locator: str) -> str:
        return self.find(by, locator).text

    def is_element_visible(self, by: str, locator: str) -> bool:
        try:
            return self.find(by, locator).is_displayed()
        except Exception:
            return False

    def change_orientation(self, orientation: str):
        """Set orientation to PORTRAIT or LANDSCAPE"""
        return self.driver.set_orientation(orientation)

    def get_orientation(self) -> str:
        return getattr(self.driver, "orientation", "PORTRAIT")
