"""
Unified Test Suite Execution CLI Runner
Orchestrates Appium Mobile, Selenium Web, Vulnerability / Security, and Load test suites.
Generates comprehensive execution reports and summary statistics.
"""

import os
import sys
import time
import argparse
import subprocess
import json

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
REPORTS_DIR = os.path.join(PROJECT_ROOT, "reports")
os.makedirs(REPORTS_DIR, exist_ok=True)

def print_banner():
    print(r"""
==============================================================================
   ORAL SURGERY AI - AUTOMATED ENTERPRISE TESTING PLATFORM
   Suites: Appium Mobile | Selenium Web | Vulnerability | Load (300+ Scenarios)
==============================================================================
    """)

def check_html_plugin():
    try:
        res = subprocess.run([sys.executable, "-m", "pytest", "--help"], capture_output=True, text=True)
        return "--html" in res.stdout
    except Exception:
        return False

HAS_HTML_PLUGIN = check_html_plugin()

def run_suite(name: str, target_dir: str, report_filename: str):
    print(f"\n[+] RUNNING {name.upper()}...")
    cmd = [sys.executable, "-m", "pytest", target_dir, "-v"]
    if HAS_HTML_PLUGIN:
        cmd.extend([f"--html={os.path.join(REPORTS_DIR, report_filename)}", "--self-contained-html"])
    res = subprocess.run(cmd, cwd=PROJECT_ROOT)
    return res.returncode == 0

def run_load_tests(limit=50, category=None):
    print(f"\n[+] RUNNING LOAD & PERFORMANCE TEST SUITE (Limit: {limit} scenarios)...")
    load_script = os.path.join(PROJECT_ROOT, "tests", "load", "load_test_runner.py")
    cmd = [
        sys.executable, load_script,
        "--limit", str(limit),
        "--output-html", os.path.join(REPORTS_DIR, "load_test_report.html"),
        "--output-json", os.path.join(REPORTS_DIR, "load_test_results.json")
    ]
    if category:
        cmd.extend(["--category", category])
    res = subprocess.run(cmd, cwd=PROJECT_ROOT)
    return res.returncode == 0

def main():
    print_banner()
    parser = argparse.ArgumentParser(description="Oral Surgery AI Automated Test Runner")
    parser.add_argument("--suite", choices=["all", "appium", "selenium", "security", "load"], default="all", help="Test suite to run")
    parser.add_argument("--limit-load", type=int, default=50, help="Number of load scenarios to execute (default: 50)")
    parser.add_argument("--category", default=None, help="Filter load testing by category name")
    args = parser.parse_args()

    results = {}
    t_start = time.time()

    if args.suite in ["all", "appium"]:
        results["Appium Mobile"] = run_suite("Appium Mobile Automation Suite", "tests/appium", "appium_report.html")

    if args.suite in ["all", "selenium"]:
        results["Selenium Web"] = run_suite("Selenium Web Automation Suite", "tests/selenium", "selenium_report.html")

    if args.suite in ["all", "security"]:
        results["Vulnerability Security"] = run_suite("Vulnerability & Security Suite", "tests/vulnerability", "vulnerability_report.html")

    if args.suite in ["all", "load"]:
        results["Load & Performance"] = run_load_tests(limit=args.limit_load, category=args.category)

    t_elapsed = round(time.time() - t_start, 2)

    print("\n" + "=" * 70)
    print("                    TEST EXECUTION SUMMARY MATRIX")
    print("=" * 70)
    for suite, status in results.items():
        status_text = "[PASSED]" if status else "[FAILED]"
        print(f"  * {suite:<30} : {status_text}")
    print("-" * 70)
    print(f"  Total Duration: {t_elapsed} seconds")
    print(f"  Reports Directory: {REPORTS_DIR}")
    print("=" * 70 + "\n")

    all_passed = all(results.values())
    sys.exit(0 if all_passed else 1)

if __name__ == "__main__":
    main()
