CREATE DATABASE warehouse;

CREATE USER user1 WITH PASSWORD'user';

GRANT ALL PRIVILEGES ON DATABASE "warehouse" to user1;

CREATE SCHEMA Task_4;

GRANT ALL PRIVILEGES ON SCHEMA Task_4 to user1;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA Task_4 TO user1;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA Task_4 TO user1;