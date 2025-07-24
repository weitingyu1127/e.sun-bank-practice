package com.favoriteslist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.favoriteslist.repository.UserRepository;

@Service
public class AccountService {

    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAccountsByUserId(String userId) {
        return userRepository.findAccountsByUserId(userId);
    }
}
