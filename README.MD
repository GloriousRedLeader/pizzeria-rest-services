# Arturo's Pizzeria

The goal here is twofold:

1. Design an entity relationship map for our pizza service architecture. You can find that design diagrams in the doc folder.
2. Implement some features of a Spring Boot app (read on!)

### Web Services Overview

This project is a complete backend for a fictitious online pizza ordering service. It contains the following services:

* Spring Boot - This is our web service container
* MySQL - Data store
* Redis - Application layer cache

These are the required competencies demonstrated (I hope anyway):

* A handful of REST controllers with the standard methods (GET, POST, DELETE, PUT), would have liked to do PATCH!
* Issuing and validation of JWT using the /auth/token API
* Ended up creating several auto configuration classes for things like Redis, Swagger and Security
* Actuator is enabled and several endpoints were enabled (abritrarily chose metrics, health, env, and caches)
* Using lombok for simple log instantiation, and Log4j2 since i'm most familiar with it.
* Application service layer caching via Redis
* All endpoints documented via Swagger [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* Several unit tests demonstrating various use cases (controllers, services, reposotiries), and a handful of integration tests exist for testing secure login. Used AssertJ.

Bonus features:

* Has code coverage reporting. I think we're currently at about 70% coverage. You can view coverage reports in target/site/jacoco/index.html after build.
* Pub / Sub messaginig 
* Fully dockerized ecosystem


### Running

There are two ways to really run the project and I'll describe both of them here. The only requirements are:

* Java 8+
* Docker (compose spec 3.7+)

We use the Maven wrapper so you don't have to worry about having maven installed. You can even forego Docker if you've got a MySQL and Redis server handy. Just update the application properties.

##### Run Everything!

To run the application in its entirety within the docker ecosystem simply build the project and run docker-compose. This will start up all services, including our Spring Boot app, and will populate the database with some menu items and users. 

This will also create a bridged network in Docker so that our services can easily communicate with each other. Notice in the spring boot app there is a configuration with a **docker** profile that modifes connection strings such that they align with the docker universe.

```
cd pizzeria-rest-services
./mvnw clean package && docker-compose up --build
```

Now you can query live metrics from Actuator:

```
curl -i -XGET localhost:8080/actuator/metrics
```

Feel free to hit a REST endpoint, this will select 1 item from the menu table:

```
curl -i http://localhost:8080/menu/item/1
```

And you can visit the Swagger API docs [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

You can stop everything with:

```
docker-compose down
```

##### Run Individual Components

For development its convenient to start dependent services individually. 

Run **redis** container:

```
docker run -it --rm --publish 6379:6379 --name pizza_redis redis:6.0.9
```

Run **redis-cli** if you want to see what's going on with our application caching:

```
docker run -it --rm redis:6.0.9 redis-cli -h pizza_redis
```

Build our **mysql** image:

```
cd pizzeria-rest-services/mysql
docker image build -t pizza/mysql:dev ./
```

Run our **mysql** container:

```
docker container run -it --rm --publish 3306:3306 --name="pizza_mysql" pizza/mysql:dev
```

At this point you can connect to MySQL using your favorite client with this connection string:

```
jdbc:mysql://localhost
```

The development credentials are **root/cookie**.

We have built our own mysql Docker image for two reasons:

* So we can easily set root password (though you can do this in docker's cmd arguments)
* Set the bind address to 0.0.0.0 to allow remote connections (not sure if this is possible in docker)

Build our **pizzeria-rest-services** Sprinig Boot app:

```
cd pizzeria-rest-services
./mvnw clean package
docker build -t pizza/services:dev .
docker run -it --rm --network="pizza-net" -p 8080:8080 --name="pizza_services" pizza/services:dev
```

Note that you'll most likely need to muck around with Docker networks if you are doing all of the servies inidividually. The docker-compose script manages the network creation for us. If you get stuck, just add *--network="pizza-net"* to all of the container run commands, and be sure to create that network with:

```
docker network create -d bridge pizza-net
```

### Testing

We are using AssertJ for this project, for no other reason than it was on the classpath. We make use of a couple of important Spring test annotations for different use cases. In the end though, all testing is self-contained with no outside dependencies so all you need is Java and Maven to compile, test and package the application. We've also got Jacoco code coverage enabled. You can view the results in *target/site/jacoco/index.html*. I think we're crusing around 70% coverage at the moment.



### Example API Calls

Some REST examples using curl.

##### /menu/item

This will attempt to add an item to the menu, but fail:

```
curl -i -X POST http://localhost:8080/menu/item --header 'Content-Type: application/json' -d '{}'
```

This will successfully add an item to the menu:

```
curl -i -X POST http://localhost:8080/menu/item --header 'Content-Type: application/json' -d \
'{
    "price": 1.99,
    "category": "PIZZA_SIZE",
    "name": "x-large",
    "description": "Extra large pizza",
    "available": true
}'
```

This will retrieve a MenuItem by id:

```
curl -i http://localhost:8080/menu/item/1

```

This will get all MenuItems by category:

```
curl -i http://localhost:8080/menu/category/PIZZA_SIZE

```

This will get all menu items:

```
curl -i http://localhost:8080/menu/items
```

Delete a MenuItem:

```
curl -i -XDELETE http://localhost:8080/menu/item/1
```

Update a menu item:

```
curl -i -XPUT http://localhost:8080/menu/item --header 'Content-Type: application/json' -d \
'{
    "id":1,
    "price":17.99,
    "name":"X-Large Pizza",
    "available":false,
    "category":"PIZZA_SIZE",
    "description":"Feeds 4 adults."
}'
```


##### /auth/token

```
curl -i -XPOST http://localhost:8080/auth/token --header 'Content-Type: application/json' -d \
'{
    "username":"muffinman",
    "password":"muffin"
}'
```

### Known Issues

* Needs a thorough security evaluation, from HTTP response text to log text - Do not want to accidentally include PII or credentials
* Outside of some generic logging messages we aren't doing anything too useful here. We should for example make an effort to log brute force login attempts
* Could certainly use more thorough test coverage
* Consolidate endpoint response errors into a single, uniform response object (I still think unathorized exceptions return a response that is not formatted like our other errors).
* Docker-compose up is a little wonkie and needs to be configured to wait until the mysql service is ready or spring will terminate, current workaround (and this may be common practice) is to configure our MySQL connection to automatically retry. 
* Actuator is exposing misc. endpoints (for fun). In a production environment this should most definitely not be the case.
* Redis warning (on Mac) asking if it is OK to allow incoming connections for embedded redis server for integration testing. This is most likely my firewall on mac.