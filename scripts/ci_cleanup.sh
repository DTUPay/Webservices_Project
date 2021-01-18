#!/bin/bash
#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Check if BUILD_NUMBER is set, if not lets assume its local
if [ -z ${BUILD_NUMBER+x} ]; then export BUILD="build-local"; else export BUILD="build-${BUILD_NUMBER}"; fi

# Remove CI environment
echo "Removing CI environment"
docker-compose --project-name continous-integration -f docker-compose.ci.yaml down --rmi all
