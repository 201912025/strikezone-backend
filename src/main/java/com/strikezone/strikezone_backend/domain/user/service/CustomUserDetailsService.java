package com.strikezone.strikezone_backend.domain.user.service;

import com.strikezone.strikezone_backend.domain.user.dto.CustomUserDetails;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.exception.UserExceptionType;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(UserExceptionType.NOT_FOUND_USER));

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(user);

    }
}
