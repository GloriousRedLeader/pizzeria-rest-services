# MySQL Image

We are building our own MySQL image for two reasons:

1. So we can set the development password for root (this is **cookie** btw)
2. And so we can bind the host address to all interfaces

We can achieve the first item by passing environment variables to docker and docker compose, but that could be burdensome for a user. I'm not sure about the second option, this might be the only way. 

### Build and Run

Build our **mysql** image:

```
cd pizzeria-rest-services/mysql
docker image build -t pizza/mysql:dev ./
```

Run our **mysql** container:

```
docker container run -it --rm --publish 3306:3306 --name="pizza_mysql" pizza/mysql:dev
```