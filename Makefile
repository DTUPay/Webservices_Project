wsdl:
	wsimport -s payment/src -keep -Xnocompile BankService.wsdl

build_run:
	sh build_and_test.sh

run_tests:
	sh system_tests/build_and_test.sh
