wsdl:
	wsimport -s payment/src -keep -Xnocompile BankService.wsdl

build_run:
	sh build_and_run

run_tests:
	cd system_tests; mvn test; cd ..
