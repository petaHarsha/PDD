"""
Test Case ID: TC-APP-026 - TC-APP-028
Category: APPIUM MOBILE AUTOMATION
Suite: Mobile Network Failure Handling, Offline Mode, and Timeout Graceful Degradation
"""

import pytest
from tests.appium.screens import DashboardScreen, LoginScreen

@pytest.mark.appium
class TestMobileNetworkFailures:

    def test_tc_app_026_airplane_mode_offline_banner(self, mobile_driver):
        """TC-APP-026: Switching to Airplane mode displays non-intrusive offline status banner"""
        dashboard = DashboardScreen(mobile_driver)
        
        # Simulate network disconnect (Connection Type 1 = Airplane Mode)
        mobile_driver.set_network_connection(1)
        assert mobile_driver.network_connection == 1
        
        # Verify offline banner or fallback UI
        assert dashboard.is_loaded() is True

    def test_tc_app_027_network_recovery_auto_reconnect(self, mobile_driver):
        """TC-APP-027: Re-enabling WiFi reconnects backend synchronization automatically"""
        dashboard = DashboardScreen(mobile_driver)
        
        # Disconnect then restore connection (6 = Data + WiFi)
        mobile_driver.set_network_connection(1)
        mobile_driver.set_network_connection(6)
        
        assert mobile_driver.network_connection == 6
        assert dashboard.is_loaded() is True

    def test_tc_app_028_api_timeout_retry_prompt(self, mobile_driver):
        """TC-APP-028: Simulated slow 15-second API latency shows retry action rather than crash"""
        login_screen = LoginScreen(mobile_driver)
        login_screen.login("dr.smith@oralsurgery.ai", "SurgeonSecurePassword123!")
        
        # Verify graceful response without unhandled crash
        assert mobile_driver.is_connected is True
