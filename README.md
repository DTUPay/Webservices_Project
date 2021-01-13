# Webservices Project
[![Build Status](http://g-20.compute.dtu.dk:8282/buildStatus/icon?job=DTUPay-CI_CD&build=0&subject=CI/CD)](http://g-20.compute.dtu.dk:8282/job/DTUPay-CI_CD/lastBuild/)

## Web-service (debug) endpoints
| Name                 | Production                                                                                               |
|----------------------|----------------------------------------------------------------------------------------------------------|
| Management Service   | [http://g-20.compute.dtu.dk:8080/management_service](http://g-20.compute.dtu.dk:8080/management_service) |
| Customer Service     | [http://g-20.compute.dtu.dk:8081/customer_service](http://g-20.compute.dtu.dk:8081/customer_service)     |
| Merchant Service     | [http://g-20.compute.dtu.dk:8082/merchant_service](http://g-20.compute.dtu.dk:8082/merchant_service)     |

## Useful links
| Name                 | Link                                                                                                     |
|----------------------|----------------------------------------------------------------------------------------------------------|
| Rabbit MQ Management | [http://g-20.compute.dtu.dk:15672](http://g-20.compute.dtu.dk:15672)                                     |
| Jenkins.             | [http://g-20.compute.dtu.dk:8282/](http://g-20.compute.dtu.dk:8282/)                                     |

## How to run
Build and test all services
```
bash scripts/build_and_test_all.sh
```

Build and test a specific service
```
bash <service_name>/build_and_test.sh
```

Start all services and run integration test
```
bash scripts/run_integration.sh
```

## Message Queue logic
```
message:
{
  "action": "addTokens",
  "payload": {
    "customerId": 1234,
    "amount": 5
  },
  "callback": {
    "topic": "customer_service",
    "action": "addTokensResponse"
  }
}

reply:
{
  "action": "addTokensResponse",
  "payload": {
    "tokens": [1,2,3,4,5]
  }
}

this.sendMessage("token_service", message); # Actor: customer_service
this.sendMessage("customer_service", reply) # Actor: token_service
```

## Server Ports
![Ports](docs/ports.png)


## UML
![UML Diagram](docs/UML.png)
