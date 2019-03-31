# Spark-bank
How to start the bank application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/bank.jar`
1. To check that your application is running enter url `http://localhost:8080`

Application is initialized with four accounts
---
account id "revolut00001" with balance 200 

account id "revolut00002" with balance 100 

account id "revolut00003" with balance 1000 

account id "revolut00004" with balance 2000


To check accounts details
---
http://localhost:8080/account/revolut00001/details
 
http://localhost:8080/account/revolut00002/details


To Transfer money between accounts
---
http://localhost:8080/account/transferMoney


{
  "sender": "revolut00001",
  "receiver": "revolut00002",
  "amount": "100"
}
