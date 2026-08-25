"""Selenium Page Objects Package"""
from .login_page import LoginPage
from .dashboard_page import DashboardPage
from .patient_crud_page import PatientCRUDPage
from .prognosis_page import PrognosisPage
from .admin_page import AdminPage

__all__ = ["LoginPage", "DashboardPage", "PatientCRUDPage", "PrognosisPage", "AdminPage"]
