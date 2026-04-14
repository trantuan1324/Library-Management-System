package com.rabbyte.librarymanagementsystem.services;

import com.rabbyte.librarymanagementsystem.entities.User;
import com.rabbyte.librarymanagementsystem.repositories.UserRepository;
import com.rabbyte.librarymanagementsystem.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        log.debug("User loaded with id: {}", user.getId());

        return UserDetailsImpl.build(user);
    }
}
