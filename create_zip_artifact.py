"""
Packages the complete Automated Testing Framework and AppSec Deliverables into a standalone downloadable ZIP artifact.
"""

import os
import zipfile
import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
ZIP_NAME = "automated_testing_project.zip"
ZIP_OUTPUT_PATH = os.path.join(PROJECT_ROOT, ZIP_NAME)

INCLUDE_PATHS = [
    "tests",
    "selenium-tests",
    "appium-tests",
    "Vulnerability Test Results",
    ".github",
    "config",
    "docs",
    "reports",
    "mock_server",
    "requirements.txt",
    "run_tests.py",
    "README.md",
    "create_zip_artifact.py"
]

EXCLUDE_PATTERNS = [
    "__pycache__",
    ".pytest_cache",
    ".git",
    ".gradle",
    ".idea",
    "build",
    "node_modules",
    "clinical_data.db",
    "test.db",
    ".pyc",
    "automated_testing_project.zip"
]

def should_exclude(file_path: str) -> bool:
    for pattern in EXCLUDE_PATTERNS:
        if pattern in file_path:
            return True
    return False

def create_artifact():
    print(f"Creating automated testing project artifact: {ZIP_OUTPUT_PATH}...")
    file_count = 0

    with zipfile.ZipFile(ZIP_OUTPUT_PATH, "w", zipfile.ZIP_DEFLATED) as zipf:
        for item in INCLUDE_PATHS:
            item_path = os.path.join(PROJECT_ROOT, item)
            if not os.path.exists(item_path):
                continue

            if os.path.isfile(item_path):
                if not should_exclude(item_path):
                    zipf.write(item_path, arcname=item)
                    file_count += 1
            elif os.path.isdir(item_path):
                for root, dirs, files in os.walk(item_path):
                    dirs[:] = [d for d in dirs if not should_exclude(d)]
                    for file in files:
                        full_path = os.path.join(root, file)
                        if not should_exclude(full_path):
                            rel_path = os.path.relpath(full_path, PROJECT_ROOT)
                            zipf.write(full_path, arcname=rel_path)
                            file_count += 1

    size_mb = round(os.path.getsize(ZIP_OUTPUT_PATH) / (1024 * 1024), 2)
    print(f"\n========================================================")
    print(f"  AUTOMATED TESTING ZIP ARTIFACT CREATED SUCCESSFULLY")
    print(f"  Artifact File: {ZIP_OUTPUT_PATH}")
    print(f"  Total Files Packaged: {file_count}")
    print(f"  Archive Size: {size_mb} MB")
    print(f"  Timestamp: {datetime.datetime.now(datetime.timezone.utc).isoformat()}")
    print(f"========================================================\n")

if __name__ == "__main__":
    create_artifact()
