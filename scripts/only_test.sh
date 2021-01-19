#!/bin/bash
#
# @author Mikkel Rosenfeldt Anderson
#

set -e

# Find all build_and_test.sh files in subdirectories
build_and_tests=$(find . -mindepth 2 -maxdepth 2 -name build_and_test.sh)

# For each build_and_test.sh file, do the following
for build_and_test in $build_and_tests
do
 # Push current directory to our directory stack
 pushd .

 # Go into the subdirectory, where build_and_test.sh resides
 cd "$(dirname "$build_and_test")"

 # Execute maven test command
 mvn clean test

 # Go back to our main directory
 popd
done

echo "Finished all testing of micro services"
