-- Function: archive_product_row()
CREATE OR REPLACE FUNCTION archive_product_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Product_History (product_id, name, description, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.product_id, OLD.name, OLD.description, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_product_update
    BEFORE UPDATE ON dsa_healthcare_db.Product
    FOR EACH ROW
EXECUTE FUNCTION archive_product_row();

-- Function: archive_product_feature_row()
CREATE OR REPLACE FUNCTION archive_product_feature_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Product_Feature_History (feature_id, product_id, feature_name, feature_description, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.feature_id, OLD.product_id, OLD.feature_name, OLD.feature_description, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_product_feature_update
    BEFORE UPDATE ON dsa_healthcare_db.Product_Feature
    FOR EACH ROW
EXECUTE FUNCTION archive_product_feature_row();

-- Function: archive_product_price_row()
CREATE OR REPLACE FUNCTION archive_product_price_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Product_Price_History (price_id, product_id, price_amount, currency, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.price_id, OLD.product_id, OLD.price_amount, OLD.currency, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_product_price_update
    BEFORE UPDATE ON dsa_healthcare_db.Product_Price
    FOR EACH ROW
EXECUTE FUNCTION archive_product_price_row();

-- Function: archive_product_discount_row()
CREATE OR REPLACE FUNCTION archive_product_discount_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Product_Discount_History (discount_id, product_id, discount_amount, discount_type, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.discount_id, OLD.product_id, OLD.discount_amount, OLD.discount_type, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_product_discount_update
    BEFORE UPDATE ON dsa_healthcare_db.Product_Discount
    FOR EACH ROW
EXECUTE FUNCTION archive_product_discount_row();

-- Function: archive_product_qualification_row()
CREATE OR REPLACE FUNCTION archive_product_qualification_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Product_Qualification_History (qualification_id, product_id, country, county, channel, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.qualification_id, OLD.product_id, OLD.country, OLD.county, OLD.channel, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_product_qualification_update
    BEFORE UPDATE ON dsa_healthcare_db.Product_Qualification
    FOR EACH ROW
EXECUTE FUNCTION archive_product_qualification_row();

-- Function: archive_account_plans_row()
CREATE OR REPLACE FUNCTION archive_account_plans_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Account_Plans_History (account_number, plan_id, purchased_date, closed_date, canceled_date, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.account_number, OLD.plan_id, OLD.purchased_date, OLD.closed_date, OLD.canceled_date, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_account_plans_update
    BEFORE UPDATE ON dsa_healthcare_db.Account_Plans
    FOR EACH ROW
EXECUTE FUNCTION archive_account_plans_row();

-- Function: archive_account_plan_order_row()
CREATE OR REPLACE FUNCTION archive_account_plan_order_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Account_Plan_Order_History (order_id, account_number, plan_id, order_lines_count, order_cost, order_discount, order_total_cost, order_status, order_canceled_date, order_fulfilled_date, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.order_id, OLD.account_number, OLD.plan_id, OLD.order_lines_count, OLD.order_cost, OLD.order_discount, OLD.order_total_cost, OLD.order_status, OLD.order_canceled_date, OLD.order_fulfilled_date, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_account_plan_order_update
    BEFORE UPDATE ON dsa_healthcare_db.Account_Plan_Order
    FOR EACH ROW
EXECUTE FUNCTION archive_account_plan_order_row();

-- Function: archive_account_plan_order_line_row()
CREATE OR REPLACE FUNCTION archive_account_plan_order_line_row()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO dsa_healthcare_db.Account_Plan_Order_Line_History (order_line_id, order_id, product_id, quantity, unit_price, total_price, discount, total_cost, payment_mode, payment_id, order_line_created_dt, status, version, created_at, created_by, updated_at, updated_by, is_deleted, archived_at)
    VALUES (OLD.order_line_id, OLD.order_id, OLD.product_id, OLD.quantity, OLD.unit_price, OLD.total_price, OLD.discount, OLD.total_cost, OLD.payment_mode, OLD.payment_id, OLD.order_line_created_dt, OLD.status, OLD.version, OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by, OLD.is_deleted, CURRENT_TIMESTAMP);
    NEW.version := OLD.version + 1;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_account_plan_order_line_update
    BEFORE UPDATE ON dsa_healthcare_db.Account_Plan_Order_Line
    FOR EACH ROW
EXECUTE FUNCTION archive_account_plan_order_line_row();
