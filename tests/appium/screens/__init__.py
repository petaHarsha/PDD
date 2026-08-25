"""Appium Mobile Page Objects"""
from .login_screen import LoginScreen
from .dashboard_screen import DashboardScreen
from .patient_form_screen import PatientFormScreen
from .cbct_viewer_screen import CBCTViewerScreen

__all__ = ["LoginScreen", "DashboardScreen", "PatientFormScreen", "CBCTViewerScreen"]
