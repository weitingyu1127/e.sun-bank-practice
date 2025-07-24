package com.favoriteslist.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void registerUser(String userid, String username, String email, String account) {
        jdbc.execute((ConnectionCallback<Void>) con -> {
            try (CallableStatement cs = con.prepareCall("{call register_user(?, ?, ?, ?)}")) {
                cs.setString(1, userid);
                cs.setString(2, username);
                cs.setString(3, email);
                cs.setString(4, account);
                cs.execute();
            }
            return null;
        });
    }
    public boolean userExists(String userid) {
        Boolean result = jdbc.execute((ConnectionCallback<Boolean>) con -> {
            try (CallableStatement cs = con.prepareCall("{call check_user_exists(?, ?)}")) {
                cs.setString(1, userid);
                cs.registerOutParameter(2, java.sql.Types.INTEGER);
                cs.execute();
                int exists = cs.getInt(2);
                return exists == 1;
            }
        });
        return Boolean.TRUE.equals(result);
    }

    public List<String> findAccountsByUserId(String userId) {
        return jdbc.execute((ConnectionCallback<List<String>>) con -> {
            List<String> result = new ArrayList<>();
            try (CallableStatement cs = con.prepareCall("{call get_accounts_by_user(?)}")) {
                cs.setString(1, userId);
                boolean hasResult = cs.execute();
                if (hasResult) {
                    try (ResultSet rs = cs.getResultSet()) {
                        while (rs.next()) {
                            result.add(rs.getString("account"));
                        }
                    }
                }
            }
            return result;
        });
    }

    public void insertToLikelist(String userId, String productName, int quantity, String paymentAccount, double totalFee, double totalAmount, double fee, int price) {
        jdbc.execute((ConnectionCallback<Void>) con -> {
            try (CallableStatement cs = con.prepareCall("{call add_to_likelist(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setString(1, userId);              
                cs.setString(2, productName);         
                cs.setDouble(3, fee);                 
                cs.setInt(4, price);                  
                cs.setInt(5, quantity);               
                cs.setString(6, paymentAccount);      
                cs.setDouble(7, totalFee);            
                cs.setDouble(8, totalAmount);         
                cs.execute();
            }
            return null;
        });
    }


    public List<Map<String, Object>> getLikelistByUserId(String userId) {
        return jdbc.execute((ConnectionCallback<List<Map<String, Object>>>) con -> {
            List<Map<String, Object>> result = new ArrayList<>();
            try (CallableStatement cs = con.prepareCall("{call get_likelist_by_user(?)}")) {
                cs.setString(1, userId);
                boolean hasResult = cs.execute();
                if (hasResult) {
                    try (ResultSet rs = cs.getResultSet()) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int columnCount = meta.getColumnCount();
                        while (rs.next()) {
                            Map<String, Object> row = new HashMap<>();
                            for (int i = 1; i <= columnCount; i++) {
                                row.put(meta.getColumnLabel(i), rs.getObject(i));
                            }
                            result.add(row);
                        }
                    }
                }
            }
            return result;
        });
    }

    public void deleteFromLikelistBySn(int sn) {
        jdbc.execute((ConnectionCallback<Void>) con -> {
            try (CallableStatement cs = con.prepareCall("{call delete_from_likelist(?)}")) {
                cs.setInt(1, sn);
                cs.execute();
            }
            return null;
        });
    }

    public void updateLikelist(int sn, String name, int quantity, String paymentAccount, double totalFee, double totalAmount, double fee, int price) {
        jdbc.execute((ConnectionCallback<Void>) con -> {
            try (CallableStatement cs = con.prepareCall("{call update_likelist(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setInt(1, sn);
                cs.setString(2, name);
                cs.setDouble(3, fee);
                cs.setInt(4, price);
                cs.setInt(5, quantity);
                cs.setString(6, paymentAccount);
                cs.setDouble(7, totalFee);
                cs.setDouble(8, totalAmount);
                cs.execute();
            }
            return null;
        });
    }

    public Map<String, Object> getUserProfile(String userId) {
        return jdbc.execute((Connection con) -> {
            Map<String, Object> result = new HashMap<>();
            List<String> accounts = new ArrayList<>();

            try (CallableStatement cs = con.prepareCall("{call get_user_profile(?)}")) {
                cs.setString(1, userId);

                boolean hasResult = cs.execute();

                if (hasResult) {
                    try (ResultSet rs = cs.getResultSet()) {
                        boolean first = true;
                        while (rs.next()) {
                            if (first) {
                                result.put("id", rs.getString("UserId"));
                                result.put("name", rs.getString("Username"));
                                result.put("email", rs.getString("Email"));
                                first = false;
                            }
                            String account = rs.getString("Account");
                            if (account != null) {
                                accounts.add(account);
                            }
                        }
                    }
                }

                result.put("accounts", accounts);
                return result;
            }
        });
    }
    public void addUserAccount(String userId, String account) {
        jdbc.execute((Connection con) -> {
            try (CallableStatement cs = con.prepareCall("{call add_user_account(?, ?)}")) {
                cs.setString(1, userId);
                cs.setString(2, account);
                cs.execute();
            }
            return null;
        });
    }

}