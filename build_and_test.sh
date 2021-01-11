#!/bin/bash
set -e

# Find all build_and_test.sh files in subdirectories
build_and_tests=$(find . -mindepth 2 -maxdepth 2 -name build_and_test.sh)

# For each build_and_test.sh file, do the following
for build_and_test in $build_and_tests
do
 # Push current directory to our directory stack
 pushd .

 # Go into the subdirectory, where build_and_test.sh resides
 cd $(dirname $build_and_test)

 # Execute the build_and_test.sh command
 bash build_and_test.sh

 # Go back to our main directory
 popd .
done

# Final end-to-end test
# ./system_tests/test.sh
