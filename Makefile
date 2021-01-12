build_test:
	bash build_and_test.sh

run_integration_test:
	bash system_tests/run_integration.sh

cleanup_ci:
	docker-compose down --rmi all -f system_tests/docker-compose.yaml
