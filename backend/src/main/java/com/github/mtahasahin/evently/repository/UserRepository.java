package com.github.mtahasahin.evently.repository;


import com.github.mtahasahin.evently.dto.UserLightDto;
import com.github.mtahasahin.evently.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {

    @EntityGraph(type=EntityGraph.EntityGraphType.FETCH, attributePaths={"authorities"})
    Optional<AppUser> findById(UUID id);

    @EntityGraph(type=EntityGraph.EntityGraphType.FETCH, attributePaths={"authorities"})
    Optional<AppUser> findByUsername(String username);

    @EntityGraph(type=EntityGraph.EntityGraphType.FETCH, attributePaths={"authorities"})
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT new com.github.mtahasahin.evently.dto.UserLightDto(u.id, u.username, u.userProfile.name, u.userProfile.avatar) FROM AppUser u WHERE u.id in :ids")
    List<UserLightDto> findAllById(List<UUID> ids);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"userProfile"}
    )
    Page<AppUser> findByFollowers_Id_followerIdAndFollowers_confirmedOrderById(UUID userId, boolean confirmed, Pageable pageable);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"userProfile"}
    )
    Page<AppUser> findByFollowings_Id_followingIdAndFollowings_confirmedOrderById(UUID userId, boolean confirmed, Pageable pageable);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    int countAppUsersByUsernameContaining(String username);

}
