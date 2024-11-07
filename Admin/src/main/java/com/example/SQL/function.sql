CREATE PROCEDURE get_admin_depot_stock
    @inid BIGINT
AS
BEGIN
    -- Create a temporary table to store results
    CREATE TABLE #tmp_table (
        product_name NVARCHAR(255),
        product_id BIGINT,
        product_attribute_name NVARCHAR(255),
        product_attribute_id BIGINT,
        quantity INT,
        depot_id BIGINT,
        depot_name NVARCHAR(150),
        cloud BIT,
        picture NVARCHAR(255)
    );

    DECLARE @depotadminid BIGINT, @depotid BIGINT, @depot_name NVARCHAR(150),
            @product_name NVARCHAR(255), @productid BIGINT,
            @product_attribute_name NVARCHAR(255), @productattributeid BIGINT,
            @quantity INT, @cloud BIT, @picture NVARCHAR(255);

    DECLARE counter CURSOR FOR
        SELECT da.id FROM depotadmin da WHERE da.userid = @inid;

    OPEN counter;
    FETCH NEXT FROM counter INTO @depotadminid;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        SELECT @depotid = @depotid FROM depotadmin da WHERE da.id = @depotadminid;
        SELECT @depot_name = d.name FROM depot d WHERE d.depotid = @depotid;

        DECLARE dp CURSOR FOR
            SELECT dd.productid, dd.quantityattid, dd.quantity
            FROM depotproduct dd WHERE dd.depotid = @depotid;

        OPEN dp;
        FETCH NEXT FROM dp INTO @productid, @productattributeid, @quantity;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            SELECT @product_name = pp.productname FROM products pp WHERE pp.productid = @productid;
            SELECT @product_attribute_name = qa.attrdescription,
                   @cloud = qa.cloud,
                   @picture = qa.picture
            FROM quantityattribute qa WHERE qa.quantityattid = @productattributeid;

            INSERT INTO #tmp_table (product_name, product_id, product_attribute_name, product_attribute_id, quantity, depot_id, depot_name, cloud, picture)
            VALUES (@product_name, @productid, @product_attribute_name, @productattributeid, @quantity, @depotid, @depot_name, @cloud, @picture);

            FETCH NEXT FROM dp INTO @productid, @productattributeid, @quantity;
        END;

        CLOSE dp;
        DEALLOCATE dp;

        FETCH NEXT FROM counter INTO @depotadminid;
    END;

    CLOSE counter;
    DEALLOCATE counter;

    SELECT * FROM #tmp_table;

    DROP TABLE #tmp_table;
END;
GO

CREATE PROCEDURE get_drivers_in_admin_depots
    @inid BIGINT
AS
BEGIN
    CREATE TABLE #tmp_table (
        driver_id BIGINT,
        full_name NVARCHAR(255),
        email NVARCHAR(255),
        employee_no NVARCHAR(50),
        truck_no NVARCHAR(50),
        contact NVARCHAR(20),
        depot_name NVARCHAR(255),
        created_on DATETIME,
        depot_id BIGINT
    );

    DECLARE @depotadminid BIGINT, @depotId BIGINT, @driverId BIGINT,
            @fullName NVARCHAR(255), @lemail NVARCHAR(255), @employeeNo NVARCHAR(50),
            @truckNo NVARCHAR(50), @contact NVARCHAR(20), @depotName NVARCHAR(255),
            @createdOn DATETIME;

    DECLARE counter CURSOR FOR
        SELECT da.id FROM depotadmin da WHERE da.userid = @inid;

    OPEN counter;
    FETCH NEXT FROM counter INTO @depotadminid;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        SELECT @depotId = da.depotid FROM depotadmin da WHERE da.id = @depotadminid;

        DECLARE dd CURSOR FOR
            SELECT d.driverid FROM depotdriver d WHERE d.depotdepotid = @depotId;

        OPEN dd;
        FETCH NEXT FROM dd INTO @driverId;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            SELECT @contact = mu.contact, @lemail = mu.email, @employeeNo = mu.employeeno, @fullName = mu.fullname
            FROM myuser mu WHERE mu.id = @driverId;

            SELECT @depotName = d.name FROM depot d WHERE d.depotid = @depotId;

            SELECT @truckNo = ISNULL(t.truckno, 'no truck')
            FROM truck t WHERE t.userid = @driverId;

            INSERT INTO #tmp_table (driver_id, full_name, email, employee_no, truck_no, contact, depot_name, created_on, depot_id)
            VALUES (@driverId, @fullName, @lemail, @employeeNo, @truckNo, @contact, @depotName, GETDATE(), @depotId);

            FETCH NEXT FROM dd INTO @driverId;
        END;

        CLOSE dd;
        DEALLOCATE dd;

        FETCH NEXT FROM counter INTO @depotadminid;
    END;

    CLOSE counter;
    DEALLOCATE counter;

    SELECT * FROM #tmp_table;
    DROP TABLE #tmp_table;
END;
GO
