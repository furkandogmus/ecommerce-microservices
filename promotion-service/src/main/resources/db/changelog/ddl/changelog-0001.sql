--liquibase formatted sql

--changeset vonhu:issue-promotion-0003-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.table_constraints where constraint_name = 'promotion_usage_promotion_id_fkey' and table_schema = current_schema()
--precondition-sql-check expectedResult:0 select count(*) from information_schema.table_constraints where constraint_name = 'promotion_apply_promotion_id_fkey' and table_schema = current_schema()
drop table if exists promotion;

--changeset vonhu:issue-promotion-0004-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'promotion' and table_schema = current_schema()
create table promotion(
    id bigserial not null,
    name varchar(255) not null,
    slug varchar(255) not null,
    description varchar(255) null,
    coupon_code varchar(255),
    discount_percentage bigint not null,
    discount_amount bigint not null,
    is_active boolean not null,
    start_date timestamp with time zone null,
    end_date timestamp with time zone null,
    created_by varchar(255),
    created_on timestamp with time zone,
    last_modified_by varchar(255),
    last_modified_on timestamp with time zone,
    primary key (id)
);
