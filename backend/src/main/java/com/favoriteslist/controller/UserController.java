package com.favoriteslist.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.favoriteslist.model.Product;
import com.favoriteslist.model.ProductAddRequest;
import com.favoriteslist.model.UserRegisterRequest;
import com.favoriteslist.repository.ProductRepository;
import com.favoriteslist.repository.UserRepository;
import com.favoriteslist.service.AccountService;
import com.favoriteslist.service.LikeListService;
import com.favoriteslist.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 

public class UserController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest req) {
        System.out.println("收到註冊資料：username=" + req.getUsername());
        try {
            userRepository.registerUser(req.getUserid(), req.getUsername(), req.getEmail(), req.getAccount());
            return "註冊成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "註冊失敗：" + e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body) {
        String userid = body.get("userid");
        boolean exists = userRepository.userExists(userid);
        if (exists) {
            return ResponseEntity.ok("登入成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("帳號不存在");
        }
    }

    @Autowired
    private ProductRepository productRepository;
    @PostMapping("/product/add")
    public String addProduct(@RequestBody ProductAddRequest req) {
        try {
            productRepository.insertProduct(req.getName(), req.getPrice(), req.getFee());
            return "產品新增成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "新增失敗：" + e.getMessage();
        }
    }

    @GetMapping("/product/all")
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    private final AccountService accountService;
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("/accounts/{userId}")
    public List<String> getAccountsByUser(@PathVariable String userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @Autowired
    private LikeListService likeListService;
    @PostMapping("/likelist/add")
    public ResponseEntity<String> addToLikelist(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            int quantity = (int) payload.get("quantity");
            String paymentAccount = (String) payload.get("paymentAccount");
            double totalFee = Double.parseDouble(payload.get("totalFee").toString());
            double totalAmount = Double.parseDouble(payload.get("totalAmount").toString());
            String name = (String) payload.get("name");
            double fee = Double.parseDouble(payload.get("fee").toString());
            int price = (int) payload.get("price");
            likeListService.addToLikelist(userId, name, quantity, paymentAccount, totalFee, totalAmount, fee, price);

            return ResponseEntity.ok("加入成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("加入失敗：" + e.getMessage());
        }
    }

    @GetMapping("/likelist/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getLikelistByUser(@PathVariable String userId) {
        try {
            List<Map<String, Object>> list = likeListService.getLikelistByUser(userId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/likelist/{sn}")
    public ResponseEntity<String> deleteFromLikelist(@PathVariable int sn) {
        try {
            likeListService.deleteFromLikelist(sn);
            return ResponseEntity.ok("刪除成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("刪除失敗：" + e.getMessage());
        }
    }

    @PostMapping("/likelist/update")
    public ResponseEntity<String> updateLikelist(@RequestBody Map<String, Object> payload) {
        try {
            int sn = Integer.parseInt(payload.get("sn").toString());
            int quantity = (int) payload.get("quantity");
            String paymentAccount = (String) payload.get("paymentAccount");
            double totalFee = Double.parseDouble(payload.get("totalFee").toString());
            double totalAmount = Double.parseDouble(payload.get("totalAmount").toString());
            String name = (String) payload.get("name");
            double fee = Double.parseDouble(payload.get("fee").toString());
            int price = (int) payload.get("price");

            likeListService.updateLikelist(sn, name, quantity, paymentAccount, totalFee, totalAmount, fee, price);
            return ResponseEntity.ok("更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("更新失敗：" + e.getMessage());
        }
    }

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String userId) {
        try {
            Map<String, Object> profile = userService.getUserProfile(userId);
            if (profile != null && !profile.isEmpty()) {
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "找不到該使用者"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/useraccount/add")
    public ResponseEntity<String> addAccount(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String account = body.get("account");

        try {
            userService.addUserAccount(userId, account);
            return ResponseEntity.ok("帳號新增成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("新增帳號失敗：" + e.getMessage());
        }
    }
}