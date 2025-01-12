package com.strikezone.strikezone_backend.domain.user.service;

import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
