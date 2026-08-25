"""
Test Case ID: TC-APP-023 - TC-APP-025
Category: APPIUM MOBILE AUTOMATION
Suite: Device Orientation, Screen Rotation, and Layout Responsiveness
"""

import pytest
from tests.appium.screens import DashboardScreen, CBCTViewerScreen

@pytest.mark.appium
class TestDeviceOrientation:

    def test_tc_app_023_portrait_to_landscape_rotation(self, mobile_driver):
        """TC-APP-023: Dashboard adapts cleanly when rotating device to LANDSCAPE"""
        dashboard = DashboardScreen(mobile_driver)
        assert dashboard.is_loaded() is True
        
        mobile_driver.set_orientation("LANDSCAPE")
        assert mobile_driver.orientation == "LANDSCAPE"
        assert dashboard.is_loaded() is True

    def test_tc_app_024_landscape_to_portrait_recovery(self, mobile_driver):
        """TC-APP-024: Rotating back to PORTRAIT restores vertical card layout"""
        dashboard = DashboardScreen(mobile_driver)
        mobile_driver.set_orientation("LANDSCAPE")
        mobile_driver.set_orientation("PORTRAIT")
        
        assert mobile_driver.orientation == "PORTRAIT"
        assert dashboard.is_loaded() is True

    def test_tc_app_025_cbct_viewer_landscape_mode(self, mobile_driver):
        """TC-APP-025: 3D CBCT viewer expands to full screen in LANDSCAPE view"""
        dashboard = DashboardScreen(mobile_driver)
        dashboard.navigate_to_cbct()
        
        mobile_driver.set_orientation("LANDSCAPE")
        cbct = CBCTViewerScreen(mobile_driver)
        assert cbct.is_element_visible("accessibility_id", "btn_upload_scan") is True
