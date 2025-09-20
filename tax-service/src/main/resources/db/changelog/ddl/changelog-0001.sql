--liquibase formatted sql

--changeset dieunguyen:issue-tax-001-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'tax_class' and table_schema = current_schema()
create table tax_class (id bigserial not null, name varchar(255) not null, created_by varchar(255), created_on timestamp(6), last_modified_by varchar(255), last_modified_on timestamp(6), primary key (id));

--changeset dieunguyen:issue-tax-002-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'tax_rate' and table_schema = current_schema()
create table tax_rate (id bigserial not null, rate float(53) not null, zip_code varchar(25), tax_class_id bigserial not null, state_or_province_id bigserial, country_id bigserial not null , created_by varchar(255), created_on timestamp(6), last_modified_by varchar(255), last_modified_on timestamp(6), primary key (id));

--changeset dieunguyen:issue-tax-003-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.table_constraints where constraint_name = 'fkkud35ls1d40wpjb5htpp14yua' and table_schema = current_schema()
alter table if exists tax_rate add constraint FKkud35ls1d40wpjb5htpp14yua foreign key (tax_class_id) references tax_class;
