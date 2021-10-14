package com.github.mtahasahin.evently.repository;


import com.github.mtahasahin.evently.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

    int countAppUsersByUsernameContaining(String username);

}
