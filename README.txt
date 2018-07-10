run docker postgres:
docker run -d -p 5432:5432 --name hibernate-demo postgres

connect to container:
docker exec -it {CONTAINER ID} bash

connect to postgres cli (password = postgres):
psql -d postgres -U postgres -W

http://www.postgresqltutorial.com/psql-commands/

