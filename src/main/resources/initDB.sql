CREATE SEQUENCE Task_4.gen_store_id AS integer INCREMENT BY 2 START 1;

CREATE TABLE task_4.store(
                             id 		BIGINT 	    PRIMARY KEY,
                             name 	    VARCHAR(50) 	NOT NULL);

CREATE TABLE Task_4.product(
                               id 		 BIGSERIAL 	        PRIMARY KEY,
                               name	     VARCHAR(50)		NOT NULL,
                               weight 	 NUMERIC(30,2) 		NOT NULL CHECK (weight >= 0),
                               cost 	 NUMERIC(30, 3) 	NOT NULL CHECK (cost >= 0),
                               store_id  INTEGER	        REFERENCES Task_4.store (id));