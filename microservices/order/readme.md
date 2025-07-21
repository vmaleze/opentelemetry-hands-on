# Order Service

This service is a simple rest api that allows to create an order
It requires the shopping-cart service to be up.

Upon order creation, the service will send a message to the `order` topic.

## Run

A docker-compose companion file is at your disposal to easily run this application.  
Run it using `docker compose up -d` and then start your application through your IDE.

The database will be automatically filled with some data.

The swagger is available [here](http://localhost:8082/swagger-ui/index.html)

