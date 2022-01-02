package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    @Query("select a from Activity a where a.userId in :userIds order by a.lastModifiedDate desc")
    Slice<Activity> getActivitiesByUserId(List<UUID> userIds, Pageable pageable);

    @Query("select a from Activity a where a.userId = :userId order by a.lastModifiedDate desc")
    Slice<Activity> getActivitiesByUserId(UUID userId, Pageable pageable);

    void deleteActivityByUserIdAndObjectIdAndActivityType(UUID userId, UUID objectId, ActivityType activityType);

    int countByUserId(UUID userId);
}
