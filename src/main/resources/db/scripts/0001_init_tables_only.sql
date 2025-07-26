-- ====================================================
-- Main Table: dsa_healthcare_db.Product
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product (
                                           id  VARCHAR(64) PRIMARY KEY,
                                           product_id VARCHAR(64) ,
                                           name VARCHAR(100),
                                           description TEXT,
                                           status VARCHAR(20) DEFAULT 'ACTIVE',
                                           version INT DEFAULT 1,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           created_by VARCHAR(64),
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           updated_by VARCHAR(64),
                                           is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Product_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_History (
                                                   history_id VARCHAR(64)  PRIMARY KEY,
                                                   id  VARCHAR(64) ,
                                                    product_id VARCHAR(64),
                                                   name VARCHAR(100),
                                                   description TEXT,
                                                   status VARCHAR(20),
                                                   version INT,
                                                   created_at TIMESTAMP,
                                                   created_by VARCHAR(64),
                                                   updated_at TIMESTAMP,
                                                   updated_by VARCHAR(64),
                                                   is_deleted BOOLEAN,
                                                   archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Product_Feature
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Feature (
                                                   id VARCHAR(64) PRIMARY KEY,
                                                   feature_id VARCHAR(64) ,
                                                   product_id VARCHAR(64),
                                                   feature_name VARCHAR(100),
                                                   feature_description TEXT,
                                                   status VARCHAR(20) DEFAULT 'ACTIVE',
                                                   version INT DEFAULT 1,
                                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                   created_by VARCHAR(64),
                                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                   updated_by VARCHAR(64),
                                                   is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Product_Feature_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Feature_History (
                                                           history_id BIGINT PRIMARY KEY,
                                                           feature_id VARCHAR(64),
                                                           product_id VARCHAR(64),
                                                           feature_name VARCHAR(100),
                                                           feature_description TEXT,
                                                           status VARCHAR(20),
                                                           version INT,
                                                           created_at TIMESTAMP,
                                                           created_by VARCHAR(64),
                                                           updated_at TIMESTAMP,
                                                           updated_by VARCHAR(64),
                                                           is_deleted BOOLEAN,
                                                           archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Product_Price
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Price (
                                                 id VARCHAR(64) PRIMARY KEY,
                                                 price_id VARCHAR(64),
                                                 product_id VARCHAR(64),
                                                 price_amount DECIMAL(10,2),
                                                 currency VARCHAR(10),
                                                 status VARCHAR(20) DEFAULT 'ACTIVE',
                                                 version INT DEFAULT 1,
                                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 created_by VARCHAR(64),
                                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 updated_by VARCHAR(64),
                                                 is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Product_Price_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Price_History (
                                                         history_id BIGINT PRIMARY KEY,
                                                         price_id VARCHAR(64),
                                                         product_id VARCHAR(64),
                                                         price_amount DECIMAL(10,2),
                                                         currency VARCHAR(10),
                                                         status VARCHAR(20),
                                                         version INT,
                                                         created_at TIMESTAMP,
                                                         created_by VARCHAR(64),
                                                         updated_at TIMESTAMP,
                                                         updated_by VARCHAR(64),
                                                         is_deleted BOOLEAN,
                                                         archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Product_Discount
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Discount (
                                                    discount_id VARCHAR(64) PRIMARY KEY,
                                                    product_id VARCHAR(64),
                                                    discount_amount DECIMAL(10,2),
                                                    discount_type VARCHAR(50),
                                                    status VARCHAR(20) DEFAULT 'ACTIVE',
                                                    version INT DEFAULT 1,
                                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                    created_by VARCHAR(64),
                                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                    updated_by VARCHAR(64),
                                                    is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Product_Discount_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Discount_History (
                                                            history_id BIGINT PRIMARY KEY,
                                                            id VARCHAR(64),
                                                            discount_id VARCHAR(64),
                                                            product_id VARCHAR(64),
                                                            discount_amount DECIMAL(10,2),
                                                            discount_type VARCHAR(50),
                                                            status VARCHAR(20),
                                                            version INT,
                                                            created_at TIMESTAMP,
                                                            created_by VARCHAR(64),
                                                            updated_at TIMESTAMP,
                                                            updated_by VARCHAR(64),
                                                            is_deleted BOOLEAN,
                                                            archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Product_Qualification
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Qualification (
                                                         id VARCHAR(64) PRIMARY KEY,
                                                         qualification_id VARCHAR(64),
                                                         product_id VARCHAR(64),
                                                         country VARCHAR(50),
                                                         county VARCHAR(50),
                                                         channel VARCHAR(50),
                                                         status VARCHAR(20) DEFAULT 'ACTIVE',
                                                         version INT DEFAULT 1,
                                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                         created_by VARCHAR(64),
                                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                         updated_by VARCHAR(64),
                                                         is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Product_Qualification_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Product_Qualification_History (
                                                                 history_id BIGINT PRIMARY KEY,
                                                                 id VARCHAR(64),
                                                                 qualification_id VARCHAR(64),
                                                                 product_id VARCHAR(64),
                                                                 country VARCHAR(50),
                                                                 county VARCHAR(50),
                                                                 channel VARCHAR(50),
                                                                 status VARCHAR(20),
                                                                 version INT,
                                                                 created_at TIMESTAMP,
                                                                 created_by VARCHAR(64),
                                                                 updated_at TIMESTAMP,
                                                                 updated_by VARCHAR(64),
                                                                 is_deleted BOOLEAN,
                                                                 archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Account_Plans
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plans (
                                                 id  VARCHAR(64) PRIMARY KEY,
                                                 account_number VARCHAR(50) ,
                                                 plan_id VARCHAR(64),
                                                 purchased_date DATE,
                                                 closed_date DATE,
                                                 canceled_date DATE,
                                                 status VARCHAR(20) DEFAULT 'ACTIVE',
                                                 version INT DEFAULT 1,
                                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 created_by VARCHAR(64),
                                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 updated_by VARCHAR(64),
                                                 is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Account_Plans_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plans_History (
                                                         id  VARCHAR(64) ,
                                                         history_id VARCHAR(64) PRIMARY KEY,
                                                         account_number VARCHAR(50),
                                                         plan_id VARCHAR(64),
                                                         purchased_date DATE,
                                                         closed_date DATE,
                                                         canceled_date DATE,
                                                         status VARCHAR(20) DEFAULT 'ACTIVE',
                                                         version INT,
                                                         created_at TIMESTAMP,
                                                         created_by VARCHAR(64),
                                                         updated_at TIMESTAMP,
                                                         updated_by VARCHAR(64),
                                                         is_deleted BOOLEAN,
                                                         archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Account_Plan_Order
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plan_Order (
                                                    id  VARCHAR(64) PRIMARY KEY,
                                                      order_id VARCHAR(64) ,
                                                      account_number VARCHAR(50),
                                                      plan_id VARCHAR(64),
                                                      order_lines_count INT,
                                                      order_cost DECIMAL(10,2),
                                                      order_discount DECIMAL(10,2),
                                                      order_total_cost DECIMAL(10,2),
                                                      order_status VARCHAR(20),
                                                      order_canceled_date DATE,
                                                      order_fulfilled_date DATE,
                                                      status VARCHAR(20) DEFAULT 'ACTIVE',
                                                      version INT DEFAULT 1,
                                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                      created_by VARCHAR(64),
                                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                      updated_by VARCHAR(64),
                                                      is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Account_Plan_Order_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plan_Order_History (
                                                              history_id BIGINT PRIMARY KEY,
                                                              id  VARCHAR(64),
                                                                  order_id VARCHAR(64),
                                                              account_number VARCHAR(50),
                                                              plan_id VARCHAR(64),
                                                              order_lines_count INT,
                                                              order_cost DECIMAL(10,2),
                                                              order_discount DECIMAL(10,2),
                                                              order_total_cost DECIMAL(10,2),
                                                              order_status VARCHAR(20),
                                                              order_canceled_date DATE,
                                                              order_fulfilled_date DATE,
                                                              status VARCHAR(20),
                                                              version INT,
                                                              created_at TIMESTAMP,
                                                              created_by VARCHAR(64),
                                                              updated_at TIMESTAMP,
                                                              updated_by VARCHAR(64),
                                                              is_deleted BOOLEAN,
                                                              archived_at TIMESTAMP
);

-- ====================================================
-- Main Table: dsa_healthcare_db.Account_Plan_Order_Line
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plan_Order_Line (
                                                           id  VARCHAR(64) PRIMARY KEY ,
                                                           order_line_id VARCHAR(64),
                                                           order_id VARCHAR(64),
                                                           product_id VARCHAR(64),
                                                           quantity INT,
                                                           unit_price DECIMAL(10,2),
                                                           total_price DECIMAL(10,2),
                                                           discount DECIMAL(10,2),
                                                           total_cost DECIMAL(10,2),
                                                           payment_mode VARCHAR(20),
                                                           payment_id VARCHAR(50),
                                                           order_line_created_dt TIMESTAMP,
                                                           status VARCHAR(20) DEFAULT 'ACTIVE',
                                                           version INT DEFAULT 1,
                                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                           created_by VARCHAR(64),
                                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                           updated_by VARCHAR(64),
                                                           is_deleted BOOLEAN DEFAULT FALSE
);

-- ====================================================
-- History Table: dsa_healthcare_db.Account_Plan_Order_Line_History
-- ====================================================
CREATE TABLE dsa_healthcare_db.Account_Plan_Order_Line_History (
                                                                   history_id BIGINT PRIMARY KEY,
                                                                   id BIGINT,
                                                                   order_line_id VARCHAR(64),
                                                                   order_id VARCHAR(64),
                                                                   product_id VARCHAR(64),
                                                                   quantity INT,
                                                                   unit_price DECIMAL(10,2),
                                                                   total_price DECIMAL(10,2),
                                                                   discount DECIMAL(10,2),
                                                                   total_cost DECIMAL(10,2),
                                                                   payment_mode VARCHAR(20),
                                                                   payment_id VARCHAR(50),
                                                                   order_line_created_dt TIMESTAMP,
                                                                   status VARCHAR(20) DEFAULT 'ACTIVE',
                                                                   version INT,
                                                                   created_at TIMESTAMP,
                                                                   created_by VARCHAR(64),
                                                                   updated_at TIMESTAMP,
                                                                   updated_by VARCHAR(64),
                                                                   is_deleted BOOLEAN,
                                                                   archived_at TIMESTAMP
);
