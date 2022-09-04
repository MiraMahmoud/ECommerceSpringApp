package com.CiValue.SpringWebApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.CiValue.Service.Impl.DBConnectionServiceImpl;

import Model.Product;
import Model.Shopper;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

	@Test
	public void getShopperByProduct() throws Exception {
        List<Shopper> shoppers = DBConnectionServiceImpl.getShoppersByProduct("BB-2144746855",10);
		assertEquals(shoppers.size(), 4);
		assertEquals(shoppers.get(0).getShopperId(), "S-1000");
	}

    @Test
	public void getProductsByShopper() throws Exception {
        List<Product> products = DBConnectionServiceImpl.getProductsByShopper("S-1000",null,"Babyom",10);
        System.out.println(products.toString());
		assertEquals(products.size(), 2);
		assertEquals(products.get(0).getProductId(), "BB-2144746855");
	}
   
}
