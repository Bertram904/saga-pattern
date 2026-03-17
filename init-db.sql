CREATE SCHEMA IF NOT EXISTS ride;
CREATE SCHEMA IF NOT EXISTS driver;
CREATE SCHEMA IF NOT EXISTS payment;

GRANT ALL ON SCHEMA ride TO ride_user;
GRANT ALL ON SCHEMA driver TO ride_user;
GRANT ALL ON SCHEMA payment TO ride_user;

-- Logical replication (WAL) required for Debezium / pgoutput CDC
ALTER USER ride_user WITH REPLICATION;
