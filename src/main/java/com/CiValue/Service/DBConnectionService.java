package com.CiValue.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Product;
import Model.Shelf;
import Model.Shopper;

public class DBConnectionService {
    private static DBConnectionService instance;
    private Connection connection;
    private String url = "jdbc:postgresql://localhost:5432";
    private String username = "postgres";
    private String password = "mmahmoud";

    private static final String DBName = "appdb";
    private static final String CREATE_DB_SQL = "CREATE DATABASE appdb";
    private static final String CREATE_PRODUCTS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS appdb.public.products (productId VARCHAR (50) PRIMARY KEY,category VARCHAR (50),brand VARCHAR (50));";
    private static final String CREATE_SHOPPERS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS appdb.public.shoppers (shopperId VARCHAR (50) PRIMARY KEY);";
    private static final String CREATE_SHELVES_TABLE_SQL = "CREATE TABLE IF NOT EXISTS appdb.public.shelves (shelfId SERIAL PRIMARY KEY,"
            +
            "shopperId VARCHAR (50) NOT NULL," +
            "productId VARCHAR (50) NOT NULL," +
            "relevancyScore NUMERIC);";

    public Connection getConnection() {
        return connection;
    }

    public static DBConnectionService getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnectionService();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnectionService();
        }

        return instance;
    }

    private DBConnectionService() throws SQLException {

        try {
            this.connection = DriverManager.getConnection(url, username, password);
            // create DB
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_DB_SQL);
            this.connection = DriverManager.getConnection(url + "/" + DBName, username, password);
            createDBTables(connection);
        } catch (SQLException sqlException) {
            String errorMessageDBAlreadyExists = String.format("ERROR: database \"" + DBName + "\" already exists");
            if (!sqlException.getMessage().equals(errorMessageDBAlreadyExists)){
                System.out.println("Database Connection Creation Failed : " + sqlException.getMessage());
                throw new SQLException("Database Connection Creation Failed : " + sqlException.getMessage());
            }
            else {
                this.connection = DriverManager.getConnection(url + "/" + DBName, username, password);
            }
        }
    }

    public static void createDBTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_PRODUCTS_TABLE_SQL);
            insertProducts(connection);
            statement.executeUpdate(CREATE_SHOPPERS_TABLE_SQL);
            statement.executeUpdate(CREATE_SHELVES_TABLE_SQL);
            insertShoppersAndShelves(connection);
            statement.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block // handle this catch
            e.printStackTrace();
        }
    }

    public static void insertProducts(Connection con) {
        try {
            String fileName = "resources/products.json";
            // Parsing the contents of the JSON file

            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(Paths.get(fileName).toFile(),
                    new TypeReference<List<Product>>() {
                    });

            // Insert a row into the MyPlayers table
            PreparedStatement pstmt = con
                    .prepareStatement("INSERT INTO public.products values (?, ?, ?) ON CONFLICT DO NOTHING");
            for (Product product : products) {
                pstmt.setString(1, product.getProductId());
                pstmt.setString(2, product.getCategory());
                pstmt.setString(3, product.getBrand());
                pstmt.executeUpdate();
            }
            System.out.println("Records inserted.....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertShoppersAndShelves(Connection con) {
        try {
            String fileName = "resources/shelves.json";
            // Parsing the contents of the JSON file
            ObjectMapper mapper = new ObjectMapper();
            List<Shopper> shoppers = mapper.readValue(Paths.get(fileName).toFile(),
                    new TypeReference<List<Shopper>>() {
                    });
            // INSERT INTO shoppers (shopperid) VALUES (?);
            PreparedStatement pstmtShoppers = con
                    .prepareStatement("INSERT INTO public.shoppers (shopperid) VALUES (?) ON CONFLICT DO NOTHING;");
            for (Shopper shopper : shoppers) {
                pstmtShoppers.setString(1, shopper.getShopperId());
                pstmtShoppers.executeUpdate();
                PreparedStatement pstmtShelves = con.prepareStatement(
                        "INSERT INTO public.shelves (shopperid, productid, relevancyscore) VALUES (?,?,?) ON CONFLICT DO NOTHING;");
                for (Shelf shelf : shopper.getShelf()) {
                    pstmtShelves.setString(1, shopper.getShopperId());
                    pstmtShelves.setString(2, shelf.getProductId());
                    pstmtShelves.setBigDecimal(3, shelf.getRelevancyScore());
                    pstmtShelves.executeUpdate();
                }

            }
            System.out.println("Records inserted.....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
