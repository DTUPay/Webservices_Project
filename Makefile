build_test:
	bash build_and_test.sh

run_integration_test:
	bash system_tests/run_integration.sh

cleanup_ci:
	docker-compose -f system_tests/docker-compose.yaml down --rmi all
