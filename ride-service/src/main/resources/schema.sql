-- Runs on startup (before Hibernate DDL). Ensures schema exists on TA / fresh DB without Docker init.
CREATE SCHEMA IF NOT EXISTS ride;
