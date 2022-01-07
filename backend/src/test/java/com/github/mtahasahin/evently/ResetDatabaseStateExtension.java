package com.github.mtahasahin.evently;

import com.github.javafaker.Faker;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.repository.AuthorityRepository;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ResetDatabaseStateExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        // Delete all data from database
        var dataSource = SpringExtension.getApplicationContext(extensionContext).getBean(DataSource.class);
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("clean-database.sql"));
        resourceDatabasePopulator.execute(dataSource);
        dataSource.getConnection().close();

        // Create test data
        var userRepository = SpringExtension.getApplicationContext(extensionContext).getBean(UserRepository.class);
        var eventRepository = SpringExtension.getApplicationContext(extensionContext).getBean(EventRepository.class);
        var authorityRepository = SpringExtension.getApplicationContext(extensionContext).getBean(AuthorityRepository.class);
        var passwordEncoder = SpringExtension.getApplicationContext(extensionContext).getBean(PasswordEncoder.class);

        var random = new Random(999);
        var faker = new Faker(random);

        var authority = authorityRepository.getByAuthority("ROLE_USER");

        var testUser1 = AppUser.builder()
                .username("test-user")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .authorities(Set.of(authority))
                .userProfile(UserProfile.builder()
                        .name("Test User")
                        .profilePublic(faker.bool().bool())
                        .about(faker.lorem().paragraph())
                        .avatar("https://i.pravatar.cc/300?u=" + faker.random().nextInt(1000))
                        .dateOfBirth(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        .facebookUsername("testuser-facebook")
                        .instagramUsername("testuser-instagram")
                        .twitterUsername("testuser-twitter")
                        .githubUsername("testuser-github")
                        .registrationDate(LocalDateTime.now())
                        .websiteUrl("https://testuser.com")
                        .language("en")
                        .timezone(faker.address().timeZone())
                        .location(faker.address().city())
                        .build())
                .build();
        testUser1.getUserProfile().setUser(testUser1);

        var testUser2 = AppUser.builder()
                .username("test-user2")
                .email("")
                .password(passwordEncoder.encode("password2"))
                .authorities(Set.of(authority))
                .userProfile(UserProfile.builder()
                        .name("Test User2")
                        .profilePublic(faker.bool().bool())
                        .about(faker.lorem().paragraph())
                        .avatar("https://i.pravatar.cc/300?u=" + faker.random().nextInt(1000))
                        .dateOfBirth(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        .facebookUsername("testuser2-facebook")
                        .instagramUsername("testuser2-instagram")
                        .twitterUsername("testuser2-twitter")
                        .githubUsername("testuser2-github")
                        .registrationDate(LocalDateTime.now())
                        .websiteUrl("https://testuser2.com")
                        .language("en")
                        .timezone(faker.address().timeZone())
                        .location(faker.address().city())
                        .build())
                .build();
        testUser2.getUserProfile().setUser(testUser2);

        userRepository.saveAll(List.of(testUser1, testUser2));

        var testEvent = Event.builder()
                .name("Test Event")
                .slug("test-event-a83jkm")
                .description("Test Event Description")
                .eventUrl("https://testevent.com")
                .startDate(LocalDateTime.now().plusDays(3))
                .endDate(LocalDateTime.now().plusDays(7))
                .timezone("Europe/Istanbul")
                .eventLocationType(EventLocationType.IN_PERSON)
                .location("Test Location")
                .limited(false)
                .approvalRequired(false)
                .imagePath("https://picsum.photos/seed/100/1000/600")
                .key("abcde")
                .visibility(EventVisibility.PUBLIC)
                .language("en")
                .organizer(testUser1)
                .eventApplications(new HashSet<>())
                .build();

        eventRepository.save(testEvent);
    }
}
