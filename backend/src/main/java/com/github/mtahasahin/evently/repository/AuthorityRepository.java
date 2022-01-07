package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority getByAuthority(String authorityName);
}
