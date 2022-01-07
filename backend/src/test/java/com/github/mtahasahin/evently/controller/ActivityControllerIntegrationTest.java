package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.BaseIT;
import com.github.mtahasahin.evently.WithMockCustomUser;
import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManagerFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ActivityControllerIntegrationTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @WithMockCustomUser
    public void getUserActivities_Success() throws Exception {
        var user = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var activity = new Activity(null, user.getId(), ActivityType.FOLLOWED_USER, user2.getId());
        entityManager.persist(activity);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(get("/api/activity/test-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].user.username").value("test-user"))
                .andExpect(jsonPath("$.data.[0].activity_type").value("FOLLOWED_USER"))
                .andExpect(jsonPath("$.data.[0].following_user.username").value("test-user2"))
                .andExpect(jsonPath("$.data.[0].created_at").exists());
    }

    @Test
    @WithMockCustomUser
    public void getFeed_Success() throws Exception {
        var user = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var activity = new Activity(null, user.getId(), ActivityType.FOLLOWED_USER, user2.getId());
        entityManager.persist(activity);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(get("/api/activity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].user.username").value("test-user"))
                .andExpect(jsonPath("$.data.[0].activity_type").value("FOLLOWED_USER"))
                .andExpect(jsonPath("$.data.[0].following_user.username").value("test-user2"))
                .andExpect(jsonPath("$.data.[0].created_at").exists());
    }
}
