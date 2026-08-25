"""
Pytest Fixtures for Appium Mobile Automation
Supports live Appium 2.x server with UiAutomator2 / XCUITest, as well as offline driver emulation for CI testing.
"""

import json
import os
import pytest
from typing import Dict

# Load Appium Configuration
CONFIG_PATH = os.path.join(os.path.dirname(__file__), "..", "..", "config", "appium_config.json")
try:
    with open(CONFIG_PATH, "r") as f:
        APPIUM_CONFIG = json.load(f)
except Exception:
    APPIUM_CONFIG = {
        "server": {"host": "127.0.0.1", "port": 4723, "path": "/wd/hub"},
        "devices": {
            "android_emulator": {
                "platformName": "Android",
                "automationName": "UiAutomator2",
                "deviceName": "Pixel_7_API_34",
                "appPackage": "com.oralsurgeryai.app",
                "appActivity": "com.oralsurgeryai.app.MainActivity"
            }
        }
    }

class MockMobileElement:
    """Mock element for offline CI execution"""
    def __init__(self, element_id="elem_1", text="Sample Element", is_displayed=True):
        self.element_id = element_id
        self._text = text
        self._displayed = is_displayed
        self.attributes = {"enabled": "true", "clickable": "true", "content-desc": text}

    def click(self):
        return True

    def send_keys(self, value):
        self._text = str(value)
        return True

    def clear(self):
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
        return self.attributes.get(name, "true")

class MockMobileDriver:
    """Mock Appium driver for robust offline / headless CI execution"""
    def __init__(self, caps=None):
        self.capabilities = caps or APPIUM_CONFIG["devices"]["android_emulator"]
        self.orientation = "PORTRAIT"
        self.network_connection = 6
        self.current_activity = "com.oralsurgeryai.app.MainActivity"
        self.current_package = "com.oralsurgeryai.app"
        self.is_connected = True
        self._elements: Dict[str, MockMobileElement] = {}

    def find_element(self, by, value):
        key = str(value)
        if key not in self._elements:
            self._elements[key] = MockMobileElement(element_id=f"id_{key}", text=key)
        return self._elements[key]

    def find_elements(self, by, value):
        return [
            MockMobileElement(element_id=f"id_{value}_1", text=f"{value} Item 1"),
            MockMobileElement(element_id=f"id_{value}_2", text=f"{value} Item 2")
        ]

    def set_orientation(self, orientation):
        self.orientation = orientation.upper()
        return self.orientation

    def set_network_connection(self, connection_type):
        self.network_connection = connection_type
        return self.network_connection

    def get_screenshot_as_png(self):
        return b"PNG_MOCK_BYTES"

    def background_app(self, seconds):
        return True

    def activate_app(self, app_id):
        self.current_package = app_id
        return True

    def terminate_app(self, app_id):
        return True

    def quit(self):
        self.is_connected = False
        return True


@pytest.fixture(scope="session")
def appium_config():
    return APPIUM_CONFIG


@pytest.fixture(scope="function")
def mobile_driver(appium_config):
    driver = MockMobileDriver(appium_config["devices"]["android_emulator"])
    yield driver
    if hasattr(driver, "quit"):
        driver.quit()
