package com.example.demo.core.context;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Component
public class LocalContext {

    @Autowired
    private UserRepository userRepository;
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    public User getCurrentUser() {
        User returnUser = new User(UUID.randomUUID(), null, null, null,null,null);
        try {
            returnUser = userRepository.findByEmail(getAuthentication().getName()).orElse(null);
        } catch (Exception ignore) {
            return returnUser;
        }
        return returnUser;
    };
    public boolean isUserAdministrator() {
        return getCurrentUser().getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }

}
