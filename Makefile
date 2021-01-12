# Building all microservices and test them individually 
build_test:
	bash bin/build_and_test_all.sh

# Testing the entire system with cucumber test
test_integration:
	bash bin/test_integration.sh

# Build and run the system locally
local_run:
	bash bin/build_and_test_all.sh
	BUILD=build-local docker-compose -f docker-compose.yaml up

# Cleanup installation locally
local_cleanup:
	BUILD=build-local docker-compose -f docker-compose.yaml down --rmi all

# Cleaning up in Docker, by removing images
ci_cleanup:
	docker-compose -f docker-compose.ci.yaml down --rmi all
	
# Deploying stable build to server
run_deployment:
	echo "implement me"
