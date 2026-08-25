"""
Base Web Page Object Model (POM) for Selenium
Encapsulates DOM locators, interactions, waits, and assertions.
"""

from typing import List

class BaseWebPage:
    def __init__(self, driver, base_url: str = "http://127.0.0.1:8000"):
        self.driver = driver
        self.base_url = base_url

    def open(self, path: str = ""):
        target = f"{self.base_url}/{path}".replace("//", "/").replace("http:/", "http://").replace("https:/", "https://")
        self.driver.get(target)
        return self

    def find(self, by: str, locator: str):
        return self.driver.find_element(by, locator)

    def find_all(self, by: str, locator: str) -> List:
        return self.driver.find_elements(by, locator)

    def click(self, by: str, locator: str):
        elem = self.find(by, locator)
        elem.click()
        return elem

    def enter_text(self, by: str, locator: str, text: str):
        elem = self.find(by, locator)
        elem.clear()
        elem.send_keys(text)
        return elem

    def get_text(self, by: str, locator: str) -> str:
        return self.find(by, locator).text

    def is_visible(self, by: str, locator: str) -> bool:
        try:
            return self.find(by, locator).is_displayed()
        except Exception:
            return False

    def get_page_title(self) -> str:
        return getattr(self.driver, "title", "")

    def execute_script(self, script: str, *args):
        return self.driver.execute_script(script, *args)
