package com.FoodServe.Dilevery.service;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class BlacklistService {

    private Set<String> blacklist = new HashSet<>();

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

	
}