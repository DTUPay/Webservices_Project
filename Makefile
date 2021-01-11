wsdl:
	wsimport -s payment/src -keep -Xnocompile BankService.wsdl

build_test:
	sh build_and_test.sh

run_integration_test:
	sh system_tests/build_and_test.sh
