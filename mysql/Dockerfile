FROM mysql:5.7.26

ENV MYSQL_ROOT_PASSWORD=cookie

RUN echo "bind-address = 0.0.0.0" >> /etc/mysql/conf.d/mysql.cnf

#COPY startup.sql /docker-entrypoint-initdb.d/startup.sql