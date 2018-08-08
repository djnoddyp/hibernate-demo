Set of unit tests demonstrating jOOQ features.

1. Connect to postgres and create database jooq_demo, connect to db and execute DDL in create-tables.sql
2. Run org.jooq.codegen.GenerationTool.java to generate table classes.
3. Run tests in JooqTest.java


run docker postgres:
docker run -d -p 5432:5432 --name hibernate-demo postgres

or using tmpfs (supposedly faster)
docker run -d -p 5432:5432 --name hibernate-demo-tmpfs --tmpfs /var/lib/postgresql/data:rw postgres

connect to container:
docker exec -it {CONTAINER ID} bash

connect to postgres cli (password = postgres):
psql -d postgres -U postgres -W

http://www.postgresqltutorial.com/psql-commands/