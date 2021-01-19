# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson & Benjamin Eriksen

# Building all microservices and test them individually 
build_test:
	bash scripts/build_and_test_all.sh

# Testing the entire system with cucumber test
test_integration:
	bash scripts/test_integration.sh

# Build and run the system locally
local_run:
	bash scripts/build_and_test_all.sh
	BUILD=build-local docker-compose -f docker-compose.yaml up

# Cleanup installation locally
local_cleanup:
	BUILD=build-local docker-compose -f docker-compose.yaml down --rmi all

# Cleaning up in Docker, by removing images
ci_cleanup:
	bash scripts/ci_cleanup.sh
	
# Deploying stable build to server
production_deployment:
	bash scripts/production_deploy.sh

# Run local environment
run:
	bash scripts/run.sh

# Clean local environment
clean:
	bash scripts/clean.sh

# Test integration local environment
test:
	cd tests && mvn test

# Run test on every microservice
test_micro:
	bash scripts/only_test.sh
