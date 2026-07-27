#!/bin/bash
# Banking API Automation - Newman Runner Script
# Usage: ./run-newman.sh

COLLECTION="Banking_API_Automation.postman_collection.json"
ENVIRONMENT="Banking-Dev.postman_environment.json"
REPORT_DIR="./reports"

mkdir -p $REPORT_DIR

echo "=============================================="
echo "  Banking API Automation - Newman Execution"
echo "=============================================="

# Full suite with HTML + JUnit reports
newman run "$COLLECTION" \
  -e "$ENVIRONMENT" \
  --delay-request 300 \
  --reporters cli,htmlextra,junit \
  --reporter-htmlextra-export "$REPORT_DIR/banking-api-report.html" \
  --reporter-junit-export "$REPORT_DIR/junit-results.xml" \
  --reporter-htmlextra-title "Banking API Regression Report" \
  --reporter-htmlextra-browserTitle "Banking API Tests"

echo ""
echo "Reports generated in: $REPORT_DIR/"
echo "  - HTML Report : $REPORT_DIR/banking-api-report.html"
echo "  - JUnit XML   : $REPORT_DIR/junit-results.xml"
echo "=============================================="
