package com.example.repository;

//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.model.AdminSuperAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<AdminSuperAdmin> getDistributors(){
        String QUERY = "select * from get_admins();";

        List<AdminSuperAdmin> distributors =new ArrayList<>();
        jdbcTemplate.query(QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AdminSuperAdmin distributor = AdminSuperAdmin.builder()
                        .cloud(rs.getBoolean("cloud"))
                        .email(rs.getString("email"))
                        .adminId(rs.getLong("admin_id"))
                        .adminName(rs.getString("admin_name"))
                        .createdOn(rs.getString("created_on"))
                        .depotsAssigned(rs.getInt("depots_assigned"))
                        .profilePic(rs.getString("profile_pic"))
                        .contact(rs.getString("contact"))
                        .employeeNo(rs.getString("employee_no"))
                        .build();
                distributors.add(distributor);
            }
        });
        return distributors;
    }

    // public List<AdminSuperAdmin> getUnalocatedAdmins(){
    //     String QUERY = "select * from get_unallocated_admins()";

    //     List<AdminSuperAdmin> distributors =new ArrayList<>();
    //     jdbcTemplate.query(QUERY, new RowCallbackHandler() {
    //         @Override
    //         public void processRow(ResultSet rs) throws SQLException {
    //             AdminSuperAdmin distributor = AdminSuperAdmin.builder()
    //                     .cloud(rs.getBoolean("cloud"))
    //                     .email(rs.getString("email"))
    //                     .adminId(rs.getLong("admin_id"))
    //                     .adminName(rs.getString("admin_name"))
    //                     .createdOn(rs.getString("created_on"))
    //                     .depotsAssigned(rs.getInt("depots_assigned"))
    //                     .profilePic(rs.getString("profile_pic"))
    //                     .contact(rs.getString("contact"))
    //                     .employeeNo(rs.getString("employee_no"))
    //                     .build();
    //             distributors.add(distributor);
    //         }
    //     });
    //     return distributors;
    // }



    public List<AdminSuperAdmin> getSuperAdmins(){
        String QUERY = "SELECT * FROM get_super_admins();";

        List<AdminSuperAdmin> supers =new ArrayList<>();
        jdbcTemplate.query(QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AdminSuperAdmin superAdmin = AdminSuperAdmin.builder()
                        .cloud(rs.getBoolean("cloud"))
                        .email(rs.getString("email"))
                        .adminId(rs.getLong("admin_id"))
                        .adminName(rs.getString("admin_name"))
                        .createdOn(rs.getString("created_on"))
                        .depotsAssigned(rs.getInt("depots_assigned"))
                        .profilePic(rs.getString("profile_pic"))
                        .contact(rs.getString("contact"))
                        .employeeNo(rs.getString("employee_no"))
                        .build();
                supers.add(superAdmin);
            }
        });
        return supers;
    }

    public List<DisplayProductDto> getDisplayProductDtos(){
        String query  = "select * from get_display_products();";
        List<DisplayProductDto> displayProductDtos = new ArrayList<>();

        jdbcTemplate.query(query, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DisplayProductDto displayProductDto = DisplayProductDto.builder()
                        .productId(rs.getLong("product_id"))
                        .productName(rs.getString("product_name"))
                        .productImage(rs.getString("product_image"))
                        .cloud(rs.getBoolean("cloud"))
                        .product1AttName(rs.getString("product_1att_name"))
                        .product1AttPrice(rs.getFloat("product_1att_price"))
                        .productDescription(rs.getString("product_description"))
                        .build();

                displayProductDtos.add(displayProductDto);
            }

        });
        return displayProductDtos;

    }

    public List<OrderItemDetails> getOrderItemsDetailsByOrderId(Long orderId){
        String query = "select * from order_details_by_order_id(?);";

        List<OrderItemDetails> orderItemDetails = new ArrayList<>();
        jdbcTemplate.query(query, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                OrderItemDetails orderItemDetail = OrderItemDetails.builder()
                        .cloud(rs.getBoolean("cloud"))
                        .productDescription(rs.getString("product_description"))
                        .productId(rs.getLong("product_id"))
                        .productImage(rs.getString("product_image"))
                        .totalPrice(rs.getFloat("total_price"))
                        .productName(rs.getString("product_name"))
                        .productQatid(rs.getLong("product_qatid"))
                        .productQattName(rs.getString("product_qatt_name"))
                        .totalPrice(rs.getFloat("total_price"))
                        .quantity(rs.getFloat("quantity"))
                        .build();

                orderItemDetails.add(orderItemDetail);
            }

        }, new Object[] {orderId});
        return orderItemDetails;
    }

    public List<CartItemDto> getCartItems(Long retailerId){
        String query = "select * from get_retailer_cart_items(?)";

        List<CartItemDto> cartItemDtos = new ArrayList<>();
        jdbcTemplate.query(query, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                CartItemDto cartItemDto = CartItemDto.builder()
                        .cartItemId(rs.getLong("cart_item_id"))
                        .cloud(rs.getBoolean("cloud"))
                        .picture(rs.getString("picture"))
                        .productAttrId(rs.getLong("product_att_id"))
                        .productAttrName(rs.getString("product_attr_name"))
                        .productAttrPrice(rs.getFloat("product_att_price"))
                        .productId(rs.getLong("product_id"))
                        .productName(rs.getString("product_name"))
                        .quantity(rs.getInt("quantity"))
                        .offerPrice(rs.getFloat("offer_price"))
                        .offerFrom(rs.getString("offer_from"))
                        .offerTo(rs.getString("offer_to"))
                        .build();

                cartItemDtos.add(cartItemDto);
            }

        }, new Object[]{retailerId});
        return cartItemDtos;
    }



    public List<DriverDto> getDriversBySoftDelete(boolean softDelete){
        List<DriverDto> driverDtos = new ArrayList<>();
        String query = "select * from get_drivers_by_soft_delete(?)";
        jdbcTemplate.query(query,new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DriverDto driverDto = DriverDto.builder()
                        .contact(rs.getString("contact"))
                        .createdOn(rs.getString("created_on"))
                        .depotId(rs.getLong("depot_id"))
                        .depotName(rs.getString("depot_name"))
                        .driverId(rs.getLong("driver_id"))
                        .email(rs.getString("email"))
                        .employeeNo(rs.getString("employee_no"))
                        .fullName(rs.getString("full_name"))
                        .truckNo(rs.getString("truck_no"))
                        .build();
                driverDtos.add(driverDto);
            }

        } ,new Object[]{softDelete});
        return driverDtos;
    }

    public List<DriverDto> getDriversWithNoTrucks(){
        List<DriverDto> driverDtos = new ArrayList<>();
        String query = "select * from get_drivers_by_soft_delete(false) where truck_no = 'no truck'";
        jdbcTemplate.query(query,new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DriverDto driverDto = DriverDto.builder()
                        .contact(rs.getString("contact"))
                        .createdOn(rs.getString("created_on"))
                        .depotId(rs.getLong("depot_id"))
                        .depotName(rs.getString("depot_name"))
                        .driverId(rs.getLong("driver_id"))
                        .email(rs.getString("email"))
                        .employeeNo(rs.getString("employee_no"))
                        .fullName(rs.getString("full_name"))
                        .truckNo(rs.getString("truck_no"))
                        .build();
                driverDtos.add(driverDto);
            }

        });
        return driverDtos;
    }


    public List<DriverDto> getUnallocatedDrivers(boolean softDelete){
        List<DriverDto> driverDtos = new ArrayList<>();
        String query = "select * from get_unallocated_drivers(?)";
        jdbcTemplate.query(query,new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DriverDto driverDto = DriverDto.builder()
                        .contact(rs.getString("contact"))
                        .createdOn(rs.getString("created_on"))
                        .depotId(rs.getLong("depot_id"))
                        .depotName(rs.getString("depot_name"))
                        .driverId(rs.getLong("driver_id"))
                        .email(rs.getString("email"))
                        .employeeNo(rs.getString("employee_no"))
                        .fullName(rs.getString("full_name"))
                        .truckNo(rs.getString("truck_no"))
                        .build();
                driverDtos.add(driverDto);
            }

        } ,new Object[]{softDelete});
        return driverDtos;
    }

    public List<DriverDto> getDepotDrivers(Long adminId){
        List<DriverDto> driverDtos = new ArrayList<>();
        String query = "select * from get_drivers_in_admin_depots(?)";
        jdbcTemplate.query(query,new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DriverDto driverDto = DriverDto.builder()
                        .contact(rs.getString("contact"))
                        .createdOn(rs.getString("created_on"))
                        .depotId(rs.getLong("depot_id"))
                        .depotName(rs.getString("depot_name"))
                        .driverId(rs.getLong("driver_id"))
                        .email(rs.getString("email"))
                        .employeeNo(rs.getString("employee_no"))
                        .fullName(rs.getString("full_name"))
                        .truckNo(rs.getString("truck_no"))
                        .build();
                driverDtos.add(driverDto);
            }

        } ,new Object[]{adminId});
        return driverDtos;
    }

    public List<StockDto> getDepotStock(Long adminId){
        List<StockDto> stockDtos = new ArrayList<>();
        String query = "select * from get_admin_depot_stock(?)";

        jdbcTemplate.query(query, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                StockDto stockDto = StockDto.builder()
                        .cloud(rs.getBoolean("cloud"))
                        .picture(rs.getString("picture"))
                        .productAttributeId(rs.getLong("product_attribute_id"))
                        .productAttributeName(rs.getString("product_attribute_name"))
                        .productId(rs.getLong("product_id"))
                        .productName(rs.getString("product_name"))
                        .depotId(rs.getLong("depot_id"))
                        .depotName(rs.getString("depot_name"))
                        .quantity(rs.getInt("quantity"))
                        .build();

                stockDtos.add(stockDto);
            }

        } , new Object[]{adminId});
        return stockDtos;
    }
}


