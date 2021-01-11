#!/bin/bash
set -e

# DTUPay - Build & Test
./DTUPay/build_and_test.sh

# payment service - Build and Test
./payment/build_and_test.sh

# Final end-to-end test
./system_tests/build_and_test.sh
