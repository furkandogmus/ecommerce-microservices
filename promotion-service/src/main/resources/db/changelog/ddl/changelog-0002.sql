--liquibase formatted sql

--changeset nguyenvanhadncntt:issue-981-1-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name = 'promotion' and column_name = 'discount_type' and table_schema = current_schema()
ALTER TABLE promotion ADD COLUMN discount_type VARCHAR(25) NOT NULL;
ALTER TABLE promotion ADD COLUMN usage_limit INT;
ALTER TABLE promotion ADD COLUMN usage_count INT DEFAULT 0 NOT NULL;
ALTER TABLE promotion ADD COLUMN usage_type VARCHAR(25) DEFAULT 'UNLIMITED' NOT NULL ;
ALTER TABLE promotion ADD COLUMN apply_to VARCHAR(25) NOT NULL;
ALTER TABLE promotion ADD COLUMN minimum_order_purchase_amount bigint;

--changeset nguyenvanhadncntt:issue-981-2-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'promotion_usage' and table_schema = current_schema()
CREATE TABLE promotion_usage (
    id SERIAL PRIMARY KEY,
    promotion_id bigint NOT NULL,
    user_id varchar(255) NOT NULL,
    order_id bigint NOT NULL ,
    product_id bigint NOT NULL,
    created_by varchar(255),
    created_on timestamp with time zone,
    last_modified_by varchar(255),
    last_modified_on timestamp with time zone,
    foreign key (promotion_id) references promotion(id)
);

--changeset nguyenvanhadncntt:issue-981-3-v2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'promotion_apply' and table_schema = current_schema()
CREATE TABLE promotion_apply (
    id SERIAL PRIMARY KEY,
    promotion_id bigint NOT NULL,
    brand_id bigint,
    category_id bigint,
    product_id bigint,
    created_by varchar(255),
    created_on timestamp with time zone,
    last_modified_by varchar(255),
    last_modified_on timestamp with time zone,
    foreign key (promotion_id) references promotion(id)
);
