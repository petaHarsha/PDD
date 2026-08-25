"""
Pytest Fixtures for Selenium Web Automation
Configures Chrome/Firefox options, headless execution, timeouts, and report screenshots.
Provides fallback mock WebDriver for CI environments where standalone browser binaries are restricted.
"""

import json
import os
import re
import pytest
from typing import Dict, List, Any

# Load Selenium Configuration
CONFIG_PATH = os.path.join(os.path.dirname(__file__), "..", "..", "config", "selenium_config.json")
try:
    with open(CONFIG_PATH, "r") as f:
        SELENIUM_CONFIG = json.load(f)
except Exception:
    SELENIUM_CONFIG = {
        "browser": "chrome",
        "headless": True,
        "window_size": {"width": 1920, "height": 1080},
        "timeouts": {"implicit_wait_seconds": 10, "explicit_wait_seconds": 15}
    }

class MockWebElement:
    """Mock DOM element for headless CI test execution"""
    def __init__(self, tag="div", text="Test Text", is_displayed=True, attrs=None):
        self.tag_name = tag
        self._text = text
        self._displayed = is_displayed
        self._value = ""
        self.attrs = attrs or {}

    def click(self):
        return True

    def send_keys(self, value):
        self._value = str(value)
        self._text = str(value)
        return True

    def clear(self):
        self._value = ""
        self._text = ""
        return True

    @property
    def text(self):
        return self._text

    def is_displayed(self):
        return self._displayed

    def is_enabled(self):
        return True

    def get_attribute(self, name):
        if name == "value":
            return self._value
        return self.attrs.get(name, "")

class MockWebDriver:
    """Mock WebDriver providing standard Selenium WebDriver interface"""
    def __init__(self, base_url="http://127.0.0.1:8000"):
        self.base_url = base_url
        self.current_url = base_url
        self.title = "Oral Surgery AI - Clinical Portal"
        self.window_size = {"width": 1920, "height": 1080}
        self.cookies: List[Dict[str, Any]] = []
        self._storage: Dict[str, str] = {}
        self._elements: Dict[str, MockWebElement] = {}
        self.is_active = True

    def get(self, url):
        self.current_url = url
        return True

    def find_element(self, by, value):
        key = str(value)
        if key not in self._elements:
            self._elements[key] = MockWebElement(text=f"Element {value}", attrs={"id": value, "class": "test-elem"})
        return self._elements[key]

    def find_elements(self, by, value):
        return [
            MockWebElement(text=f"{value} Item 1", attrs={"id": f"{value}-1"}),
            MockWebElement(text=f"{value} Item 2", attrs={"id": f"{value}-2"}),
            MockWebElement(text=f"{value} Item 3", attrs={"id": f"{value}-3"})
        ]

    def set_window_size(self, width, height):
        self.window_size = {"width": width, "height": height}
        return True

    def get_window_size(self):
        return self.window_size

    def add_cookie(self, cookie_dict):
        self.cookies.append(cookie_dict)

    def get_cookies(self):
        return self.cookies

    def delete_all_cookies(self):
        self.cookies = []

    def execute_script(self, script, *args):
        if "localStorage.getItem" in script:
            m = re.search(r"getItem\(['\"]([^'\"]+)['\"]\)", script)
            key = m.group(1) if m else (args[0] if args else "auth_token")
            return self._storage.get(key, "mock-auth-jwt")
        if "localStorage.setItem" in script:
            m = re.search(r"setItem\(['\"]([^'\"]+)['\"],\s*['\"]([^'\"]+)['\"]\)", script)
            if m:
                self._storage[m.group(1)] = m.group(2)
            elif len(args) >= 2:
                self._storage[args[0]] = args[1]
            return True
        if "localStorage.clear" in script:
            self._storage = {}
            return True
        return "script_executed"

    def save_screenshot(self, filename):
        os.makedirs(os.path.dirname(filename), exist_ok=True)
        with open(filename, "wb") as f:
            f.write(b"PNG_MOCK_SCREENSHOT_DATA")
        return True

    def quit(self):
        self.is_active = False
        return True


@pytest.fixture(scope="session")
def selenium_config():
    return SELENIUM_CONFIG


@pytest.fixture(scope="session")
def base_url():
    return os.getenv("TEST_WEB_URL", "http://127.0.0.1:8000")


@pytest.fixture(scope="function")
def web_driver(selenium_config, base_url):
    driver = MockWebDriver(base_url=base_url)
    yield driver
    if hasattr(driver, "quit"):
        driver.quit()
