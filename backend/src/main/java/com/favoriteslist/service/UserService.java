package com.favoriteslist.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favoriteslist.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserProfile(String userId) {
        return userRepository.getUserProfile(userId);
    }
    public void addUserAccount(String userId, String account) {
        userRepository.addUserAccount(userId, account);
    }
}
