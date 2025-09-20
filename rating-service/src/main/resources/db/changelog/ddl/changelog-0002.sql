--liquibase formatted sql

--changeset khanhtran:raw-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name = 'rating' and column_name = 'product_name' and table_schema = current_schema()
alter table if exists rating add column product_name varchar(255);
