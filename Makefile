# Building all microservices and test them individually 
build_test:
	bash build_and_test.sh

# Testing the entire system with cucumber test
run_integration_test:
	bash system_tests/run_integration.sh

# Running the system locally
local_run:
	bash build_and_test.sh
	BUILD_NUMBER=local docker-compose -f system_tests/docker-compose.yaml up

# Cleanup installation locally
local_cleanup:
	BUILD_NUMBER=local docker-compose -f system_tests/docker-compose.yaml down --rmi all

# Cleaning up in Docker, by removing images
ci_cleanup:
	docker-compose -f system_tests/docker-compose.yaml down --rmi all
	
# Deploying stable build to server
run_deployment:
	echo "implement me"
