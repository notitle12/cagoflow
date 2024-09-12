package com.spring_cloud.eureka.client.auth.application.security;

import com.spring_cloud.eureka.client.auth.domain.User;
import com.spring_cloud.eureka.client.auth.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        if (user.getIsDelete()) {
            throw new UsernameNotFoundException("User is deleted");
        }

        // UserDetailsImpl 객체를 생성할 때 User 객체를 전달합니다.
        return new UserDetailsImpl(user);
    }
}
