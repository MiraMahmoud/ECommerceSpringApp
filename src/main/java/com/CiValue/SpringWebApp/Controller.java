package com.CiValue.SpringWebApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

import javax.validation.constraints.Max;
import javax.ws.rs.QueryParam;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.CiValue.Service.Impl.DBConnectionServiceImpl;
import com.google.gson.Gson;

import Model.Product;
import Model.Shopper;
import io.swagger.annotations.ApiParam;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

    @GetMapping("/getProductsByShopper")
    public String getProductsByShopper(
            @ApiParam(value = "The shopperId", required = true) @QueryParam("shopperId") String shopperId,
            @ApiParam(value = "The category", required = false) @QueryParam("category") String category,
            @ApiParam(value = "The brand", required = false) @QueryParam("brand") String brand,
            @ApiParam(value = "The limit", required = false) @DefaultValue("10") @Max(100) @QueryParam("limit") int limit)
            throws SQLException {
        List<Product> products = DBConnectionServiceImpl.getProductsByShopper(shopperId, category, brand, limit);
        return new Gson().toJson(products);
    }

    @GetMapping("/getShoppersByProduct")
    public String getShoppersByProduct(
            @ApiParam(value = "The productId", required = true) @QueryParam("productId") String productId,
            @ApiParam(value = "The limit", required = false) @DefaultValue("10") @Max(1000) @QueryParam("limit") int limit)
            throws SQLException {
        List<Shopper> shoppers = DBConnectionServiceImpl.getShoppersByProduct(productId, limit);
        return new Gson().toJson(shoppers);
    }
}