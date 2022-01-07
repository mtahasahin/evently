package com.github.mtahasahin.evently.util;

import com.github.mtahasahin.evently.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import java.util.Locale;

public class UsernameUtils {
    public static String createUniqueUsername(@NotEmpty String name, UserRepository userRepository) {
        String username;
        do {
            username = name.trim().toLowerCase(Locale.ROOT).replaceAll(" ", "");
            int count = userRepository.countAppUsersByUsernameContaining(username);
            if (count > 0)
                username += "-" + count;
        }
        while (userRepository.findByUsername(username).isPresent());
        return username;
    }
}
