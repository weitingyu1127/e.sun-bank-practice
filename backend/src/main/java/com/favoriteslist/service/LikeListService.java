package com.favoriteslist.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.favoriteslist.repository.UserRepository;

@Service  
public class LikeListService {

    private final UserRepository userRepository;

    public LikeListService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addToLikelist(String userId, String productName, int quantity, String paymentAccount, double totalFee, double totalAmount, double fee, int price) {
        userRepository.insertToLikelist(userId, productName, quantity, paymentAccount, totalFee, totalAmount, fee, price);
    }

    public List<Map<String, Object>> getLikelistByUser(String userId) {
        return userRepository.getLikelistByUserId(userId);
    }

    public void deleteFromLikelist(int sn) {
        userRepository.deleteFromLikelistBySn(sn);
    }

    public void updateLikelist(int sn, String name, int quantity, String paymentAccount, double totalFee, double totalAmount, double fee, int price) {
        userRepository.updateLikelist(sn, name, quantity, paymentAccount, totalFee, totalAmount, fee, price);
    }
}
