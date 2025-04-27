Payment API - REST & Kafka Integration
This project is a Spring Boot REST API that allows you to:
Generate a payment schedule based on an order.
Asynchronously send an order request to Kafka for later processing.
It is designed for local development and testing with a lightweight Dockerized Kafka environment.

Features
/api/payments/schedule
Synchronously generate a payment schedule if the order amount is eligible.

/api/payments/scheduleasync
Asynchronously send an order to a Kafka topic (order-requests).

Swagger UI integration for easy API testing.

Tech Stack
Java 21
Spring Boot 3
Spring Kafka
Docker / Docker Compose (Bitnami Kafka image)
Swagger / OpenAPI

Getting Started
1. Clone the repository
git clone https://gitlab.com/fr_kata_sf/c4-SF-0266-SQ04.git
cd c4-SF-0266-SQ04

2. Start Kafka using Docker
Start the required Kafka broker:
docker compose up -d
Kafka will listen on localhost:9092.**
To check logs:
docker logs -f kafka

3. Run the Spring Boot application
Start the backend service:
mvn spring-boot:run

4. Test the API with Postman or Swagger
Swagger UI:
http://localhost:8080/swagger-ui/index.html

Endpoints:
HTTP Method	   Endpoint   	                Description
POST	       /api/payments/schedule	    Generate a payment schedule
POST	       /api/payments/scheduleasync	Send order asynchronously to Kafka

Example request body:
{
"amount": 200.00,
"paymentOption": "FOUR_INSTALLMENTS"
}

5. Verify Kafka Messages
To see the messages sent to Kafka:
docker exec -it kafka kafka-console-consumer.sh \
   --bootstrap-server kafka:9092 \
   --topic order-requests \
   --from-beginning


Important Notes
Eligibility Check:
If the order amount is not eligible, a 400 Bad Request will be returned by /schedule.

Kafka Async Sending:
The /scheduleasync endpoint decouples order processing by pushing the order into Kafka for asynchronous handling.

Local-Only Kafka Setup:
Docker provides a lightweight, ephemeral Kafka broker for local tests. No manual Kafka installation needed.

Test H2 Database:
To test the persistence in an H2 in-memory database and verify the data in the browser.
Once the application is running, you can access the following endpoint to see the data stored in the H2 database:
http://localhost:8080/h2-console/

Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:

you can execute the request to verify data: SELECT * FROM PAYMENTS;

