package com.strikezone.strikezone_backend.domain.user.service;

import com.strikezone.strikezone_backend.domain.user.dto.controller.JoinDTO;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return;
        }

        User user = User.builder()
                        .username(username)
                        .password((bCryptPasswordEncoder.encode(password)))
                        .role("ROLE_ADMIN")
                        .build();

        userRepository.save(user);
    }
}

