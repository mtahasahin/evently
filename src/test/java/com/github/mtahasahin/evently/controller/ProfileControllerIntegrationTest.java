package com.github.mtahasahin.evently.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mtahasahin.evently.BaseIT;
import com.github.mtahasahin.evently.WithMockCustomUser;
import com.github.mtahasahin.evently.dto.ProfileDto;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.FollowerFollowing;
import com.github.mtahasahin.evently.entity.FollowerFollowingId;
import com.github.mtahasahin.evently.repository.EventApplicationRepository;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProfileControllerIntegrationTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @WithMockCustomUser
    public void getSelfProfile() throws Exception {
        var user = userRepository.findByUsername("test-user").orElseThrow();

        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.profile.name").value(user.getUserProfile().getName()))
                .andExpect(jsonPath("$.data.profile.dateOfBirth").value(user.getUserProfile().getDateOfBirth().toString()))
                .andExpect(jsonPath("$.data.profile.profilePublic").value(user.getUserProfile().isProfilePublic()))
                .andExpect(jsonPath("$.data.profile.timezone").value(user.getUserProfile().getTimezone()))
                .andExpect(jsonPath("$.data.profile.location").value(user.getUserProfile().getLocation()))
                .andExpect(jsonPath("$.data.profile.language").value(user.getUserProfile().getLanguage()))
                .andExpect(jsonPath("$.data.profile.about").value(user.getUserProfile().getAbout()))
                .andExpect(jsonPath("$.data.profile.avatar").value(user.getUserProfile().getAvatar()))
                .andExpect(jsonPath("$.data.profile.websiteUrl").value(user.getUserProfile().getWebsiteUrl()))
                .andExpect(jsonPath("$.data.profile.twitterUsername").value(user.getUserProfile().getTwitterUsername()))
                .andExpect(jsonPath("$.data.profile.facebookUsername").value(user.getUserProfile().getFacebookUsername()))
                .andExpect(jsonPath("$.data.profile.instagramUsername").value(user.getUserProfile().getInstagramUsername()))
                .andExpect(jsonPath("$.data.profile.githubUsername").value(user.getUserProfile().getGithubUsername()));
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void getPublicUserProfile() throws Exception {
        var user = userRepository.findByUsername("test-user").orElseThrow();

        mockMvc.perform(get("/api/profile/test-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.name").value(user.getUserProfile().getName()))
                .andExpect(jsonPath("$.data.dateOfBirth").value(user.getUserProfile().getDateOfBirth().toString()))
                .andExpect(jsonPath("$.data.profilePublic").value(user.getUserProfile().isProfilePublic()))
                .andExpect(jsonPath("$.data.about").value(user.getUserProfile().getAbout()))
                .andExpect(jsonPath("$.data.avatar").value(user.getUserProfile().getAvatar()))
                .andExpect(jsonPath("$.data.websiteUrl").value(user.getUserProfile().getWebsiteUrl()))
                .andExpect(jsonPath("$.data.twitterUsername").value(user.getUserProfile().getTwitterUsername()))
                .andExpect(jsonPath("$.data.facebookUsername").value(user.getUserProfile().getFacebookUsername()))
                .andExpect(jsonPath("$.data.instagramUsername").value(user.getUserProfile().getInstagramUsername()))
                .andExpect(jsonPath("$.data.githubUsername").value(user.getUserProfile().getGithubUsername()));
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void updateProfile() throws Exception {
        var user = userRepository.findByUsername("test-user").orElseThrow();

        var profileDto = ProfileDto.builder()
                .profilePublic(true)
                .about("about")
                .dateOfBirth(LocalDate.now())
                .twitterUsername("twitterUsername")
                .facebookUsername("facebookUsername")
                .instagramUsername("instagramUsername")
                .githubUsername("githubUsername")
                .language("en")
                .location("location")
                .name("name")
                .timezone("Europe/London")
                .build();

        var userDto = UserDto.builder()
                .profile(profileDto)
                .username("test-user")
                .email("test@example.com")
                .build();

        mockMvc.perform(put("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.data.profile.name").value(profileDto.getName()))
                .andExpect(jsonPath("$.data.profile.dateOfBirth").value(profileDto.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.data.profile.profilePublic").value(profileDto.isProfilePublic()))
                .andExpect(jsonPath("$.data.profile.timezone").value(profileDto.getTimezone()))
                .andExpect(jsonPath("$.data.profile.location").value(profileDto.getLocation()))
                .andExpect(jsonPath("$.data.profile.language").value(profileDto.getLanguage()))
                .andExpect(jsonPath("$.data.profile.about").value(profileDto.getAbout()))
                .andExpect(jsonPath("$.data.profile.avatar").value(user.getUserProfile().getAvatar()))
                .andExpect(jsonPath("$.data.profile.websiteUrl").value(profileDto.getWebsiteUrl()))
                .andExpect(jsonPath("$.data.profile.twitterUsername").value(profileDto.getTwitterUsername()))
                .andExpect(jsonPath("$.data.profile.facebookUsername").value(profileDto.getFacebookUsername()))
                .andExpect(jsonPath("$.data.profile.instagramUsername").value(profileDto.getInstagramUsername()))
                .andExpect(jsonPath("$.data.profile.githubUsername").value(profileDto.getGithubUsername()));
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void getFollowing() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(true);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(get("/api/profile/" + user1.getUsername() + "/following")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.size()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(user2.getId().toString()))
                .andExpect(jsonPath("$.data.content[0].username").value(user2.getUsername()))
                .andExpect(jsonPath("$.data.content[0].name").value(user2.getUserProfile().getName()))
                .andExpect(jsonPath("$.data.content[0].avatar").value(user2.getUserProfile().getAvatar()));
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void getFollowers() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(true);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(get("/api/profile/" + user2.getUsername() + "/followers")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.size()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(user1.getId().toString()))
                .andExpect(jsonPath("$.data.content[0].username").value(user1.getUsername()))
                .andExpect(jsonPath("$.data.content[0].name").value(user1.getUserProfile().getName()))
                .andExpect(jsonPath("$.data.content[0].avatar").value(user1.getUserProfile().getAvatar()));
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void getFollowerRequests() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(false);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(get("/api/profile/" + user2.getUsername() + "/follower-requests")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.size()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(user1.getId().toString()))
                .andExpect(jsonPath("$.data.content[0].username").value(user1.getUsername()))
                .andExpect(jsonPath("$.data.content[0].name").value(user1.getUserProfile().getName()))
                .andExpect(jsonPath("$.data.content[0].avatar").value(user1.getUserProfile().getAvatar()));
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void follow_publicProfile() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();

        mockMvc.perform(put("/api/profile/" + user2.getUsername() + "/following"))
                .andExpect(status().isOk());

        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = entityManager.find(FollowerFollowing.class, new FollowerFollowingId(user1.getId(), user2.getId()));
        entityManager.close();
        Assertions.assertNotNull(followerFollowing);
        Assertions.assertTrue(followerFollowing.isConfirmed());
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void follow_privateProfile() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        user2.getUserProfile().setProfilePublic(false);
        userRepository.save(user2);

        mockMvc.perform(put("/api/profile/" + user2.getUsername() + "/following"))
                .andExpect(status().isOk());

        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = entityManager.find(FollowerFollowing.class, new FollowerFollowingId(user1.getId(), user2.getId()));
        entityManager.close();
        Assertions.assertNotNull(followerFollowing);
        Assertions.assertFalse(followerFollowing.isConfirmed());
    }

    @Test
    @WithMockCustomUser(username = "test-user")
    public void unfollow() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(true);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.clear();

        mockMvc.perform(delete("/api/profile/" + user2.getUsername() + "/following"))
                .andExpect(status().isOk());

        var query = entityManager.createQuery("SELECT f FROM FollowerFollowing f WHERE f.follower.id = :followerId AND f.following.id = :followingId", FollowerFollowing.class);
        query.setParameter("followerId", user1.getId());
        query.setParameter("followingId", user2.getId());
        Assertions.assertTrue(query.getResultList().isEmpty());
        entityManager.close();
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void acceptFollowerRequest() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(false);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.clear();

        mockMvc.perform(put("/api/profile/follower-request/" + user1.getUsername()))
                .andExpect(status().isOk());

        var query = entityManager.createQuery("SELECT f FROM FollowerFollowing f WHERE f.follower.id = :followerId AND f.following.id = :followingId", FollowerFollowing.class);
        query.setParameter("followerId", user1.getId());
        query.setParameter("followingId", user2.getId());
        Assertions.assertTrue(query.getSingleResult().isConfirmed());
        entityManager.close();
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void rejectFollowerRequest() throws Exception {
        var user1 = userRepository.findByUsername("test-user").orElseThrow();
        var user2 = userRepository.findByUsername("test-user2").orElseThrow();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var followerFollowing = new FollowerFollowing();
        followerFollowing.setFollower(user1);
        followerFollowing.setFollowing(user2);
        followerFollowing.setConfirmed(false);
        entityManager.merge(followerFollowing);
        entityManager.getTransaction().commit();
        entityManager.clear();

        mockMvc.perform(delete("/api/profile/follower-request/" + user1.getUsername()))
                .andExpect(status().isOk());

        var query = entityManager.createQuery("SELECT f FROM FollowerFollowing f WHERE f.follower.id = :followerId AND f.following.id = :followingId", FollowerFollowing.class);
        query.setParameter("followerId", user1.getId());
        query.setParameter("followingId", user2.getId());
        Assertions.assertTrue(query.getResultList().isEmpty());
        entityManager.close();
    }


}
