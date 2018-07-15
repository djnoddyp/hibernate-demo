run docker postgres:
docker run -d -p 5432:5432 --name hibernate-demo postgres

or using tmpfs (supposedly faster)
docker run -d -p 5432:5432 --name hibernate-demo-tmpfs --tmpfs /var/lib/postgresql/data:rw postgres

connect to container:
docker exec -it {CONTAINER ID} bash

connect to postgres cli (password = postgres):
psql -d postgres -U postgres -W

http://www.postgresqltutorial.com/psql-commands/


ARQUILLIAN:

need to add following datasource, use standalone-full.xml:

    <datasource jta="true" jndi-name="java:jboss/datasources/PostgresDS" pool-name="PostgresDS" enabled="true" use-ccm="false">
        <connection-url>jdbc:postgresql://localhost:5432/postgres</connection-url>
        <driver-class>org.postgresql.Driver</driver-class>
        <driver>postgresql-42.2.2.jar</driver>
        <security>
            <user-name>postgres</user-name>
            <password>postgres</password>
        </security>
        <validation>
            <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChec$
            <background-validation>true</background-validation>
            <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
        </validation>
    </datasource>

