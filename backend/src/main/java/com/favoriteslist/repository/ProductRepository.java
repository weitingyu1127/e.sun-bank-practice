package com.favoriteslist.repository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.favoriteslist.model.Product;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void insertProduct(String name, int price, double fee) {
        jdbc.update("CALL insert_product(?, ?, ?)", name, price, fee);
    }

    public List<Product> getAllProducts() {
        String sql = "CALL get_all_products()";
        return jdbc.query(sql, (rs, rowNum) -> {
            Product product = new Product();
            product.setName(rs.getString("ProductName"));
            product.setPrice(rs.getInt("Price"));
            product.setFee(rs.getDouble("FeeRate"));
            return product;
        });
    }
}
