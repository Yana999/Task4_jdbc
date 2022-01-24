CREATE SCHEMA Task_4;
SET search_path TO Task_4;

GRANT ALL PRIVILEGES ON SCHEMA Task_4 to user1;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA Task_4 TO user1;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA Task_4 TO user1;

CREATE TABLE store(
                             id 		bigserial 	    PRIMARY KEY,
                             name 	    VARCHAR(50) 	NOT NULL);

CREATE TABLE product(
                               id 		 bigserial 	        PRIMARY KEY,
                               name	     VARCHAR(50)		NOT NULL,
                               weight 	 NUMERIC(30,2) 		NOT NULL CHECK (weight >= 0),
                               cost 	 NUMERIC(30, 3) 	NOT NULL CHECK (cost >= 0),
                               store_id  INTEGER	        REFERENCES store (id));
