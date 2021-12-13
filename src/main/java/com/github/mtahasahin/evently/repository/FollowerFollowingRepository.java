package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.FollowerFollowing;
import com.github.mtahasahin.evently.entity.FollowerFollowingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowerFollowingRepository extends JpaRepository<FollowerFollowing, FollowerFollowingId> {
}
