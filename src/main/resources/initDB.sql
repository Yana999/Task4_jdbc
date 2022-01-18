GRANT ALL PRIVILEGES ON SCHEMA Task_4 to user1;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA Task_4 TO user1;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA Task_4 TO user1;


CREATE SCHEMA Task_4;
SET search_path TO Task_4;

CREATE TABLE store(
                             id 		bigserial 	PRIMARY KEY,
                             name 	text 		NOT NULL);

CREATE TABLE product(
                               id 		 bigserial 	PRIMARY KEY,
                               name	 text		NOT NULL,
                               weight 	 numeric(3) 		NOT NULL,
                               cost 	 numeric(2) 		NOT NULL,
                               store_id integer	REFERENCES store (id));
