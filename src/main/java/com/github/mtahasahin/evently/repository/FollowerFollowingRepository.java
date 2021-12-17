package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.FollowerFollowing;
import com.github.mtahasahin.evently.entity.FollowerFollowingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FollowerFollowingRepository extends JpaRepository<FollowerFollowing, FollowerFollowingId> {

    @Query("select f.id.followingId from FollowerFollowing f where f.confirmed = true and f.id.followerId = ?1")
    List<Long> getFriendsIds(Long userId);

}
