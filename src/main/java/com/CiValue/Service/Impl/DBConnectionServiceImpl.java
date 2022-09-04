package com.CiValue.Service.Impl;

import com.CiValue.Service.DBConnectionService;
import com.CiValue.Model.Product;
import com.CiValue.Model.Shopper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

public class DBConnectionServiceImpl {
    private static DBConnectionService dbConnectionService;
    private static final int productsMaxLimit = 100;

    public static List<Product> getProductsByShopper(@NotNull String shopperId, String category, String brand,
            @Max(productsMaxLimit) int limit) throws SQLException {

        List<Product> products = new ArrayList<>();
        int count = 1;
        try {
            // String query = "SELECT * FROM products WHERE category=? AND brand=? AND productId IN(SELECT productId FROM shelves WHERE shopperId=?) LIMIT (?)";
            String query = "SELECT * FROM products WHERE ";
            if (category != null) {
                query += "category=? AND ";
            }
            if (brand != null) {
                query += "brand=? AND ";
            }

            query += "productId IN(SELECT productId FROM shelves WHERE shopperId=?) LIMIT (?)";
            PreparedStatement ps = DBConnectionService.getInstance().getConnection().prepareStatement(query);

            if (category != null) {
                ps.setString(count, category);
                count++;
            }
            if (brand != null) {
                ps.setString(count, brand);
                count++;
            }
            ps.setString(count, shopperId);
            count++;

            if (limit <= productsMaxLimit) {
                ps.setInt(count, limit);
            } else {
                ps.setInt(count, productsMaxLimit);
            }
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                String productId = result.getString("productId");
                // String brand = result.getString("productId");
                Product product = new Product();
                product.setProductId(productId);
                product.setBrand(brand != null ? brand : result.getString("brand"));
                product.setCategory(category != null ? category : result.getString("category"));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static List<Shopper> getShoppersByProduct(@NotNull String productId,
            @DefaultValue("10") @Max(1000) int limit) throws SQLException {
        List<Shopper> shoppers = new ArrayList<>();
        
            String query = "SELECT shopperId FROM shelves WHERE productId=? LIMIT (?)";
            PreparedStatement ps = DBConnectionService.getInstance().getConnection().prepareStatement(query);
            ps.setString(1, productId);
            ps.setInt(2, limit);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                String shopperId = result.getString("shopperId");
                Shopper shopper = new Shopper();
                shopper.setShopperId(shopperId);
                shoppers.add(shopper);
            }

        
        return shoppers;
    }
}
