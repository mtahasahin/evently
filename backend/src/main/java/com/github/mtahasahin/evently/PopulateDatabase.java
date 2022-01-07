package com.github.mtahasahin.evently;

import com.github.javafaker.Faker;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.entity.EventApplication;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.slugify.Slugify;
import com.ibm.icu.util.ULocale;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Profile("!test")
class PopulateDatabase implements ApplicationRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws InterruptedException {

        SearchSession searchSession = Search.session(entityManager);

        MassIndexer indexer = searchSession.massIndexer(Object.class);

        indexer.startAndWait();

        if (userRepository.count() != 0 || eventRepository.count() != 0) {
            return;
        }

        var userCount = 100;
        var eventCount = 100;
        Random random = new Random(1);

        var faker = new Faker(random);
        var userList = new ArrayList<AppUser>();
        for (int i = 0; i < userCount; i++) {
            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();
            var user = AppUser.builder()
                    .username(firstName.toLowerCase(Locale.ROOT) + "." + lastName.toLowerCase(Locale.ROOT))
                    .email(firstName + lastName + "@gmail.com")
                    .password(faker.internet().password())
                    .userProfile(UserProfile.builder()
                            .name(firstName + " " + lastName)
                            .profilePublic(faker.bool().bool())
                            .about(faker.lorem().paragraph())
                            .avatar("https://i.pravatar.cc/300?u=" + faker.random().nextInt(1000))
                            .dateOfBirth(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                            .facebookUsername(firstName.toLowerCase(Locale.ROOT))
                            .instagramUsername(firstName.toLowerCase(Locale.ROOT))
                            .twitterUsername(firstName.toLowerCase(Locale.ROOT))
                            .githubUsername(firstName.toLowerCase(Locale.ROOT))
                            .registrationDate(faker.date().between(faker.date().past(365, 2, TimeUnit.DAYS), faker.date().past(1, TimeUnit.DAYS)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                            .websiteUrl("https://" + firstName + lastName + ".com")
                            .language(randomLanguage(random))
                            .timezone(faker.address().timeZone())
                            .location(faker.address().city())
                            .build())
                    .build();
            user.getUserProfile().setUser(user);
            userList.add(user);
        }
        userRepository.saveAll(userList);

        var eventList = new ArrayList<Event>();
        for (int i = 0; i < eventCount; i++) {
            var name = faker.company().catchPhrase();
            var limited = faker.bool().bool();
            var locationType = faker.options().option(EventLocationType.ONLINE, EventLocationType.IN_PERSON);
            var event = Event.builder()
                    .name(name)
                    .slug(new Slugify().slugify(name) + "-" + faker.lorem().characters(5))
                    .description(faker.lorem().paragraph())
                    .eventUrl(faker.internet().url())
                    .startDate(faker.date().future(30, 1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .endDate(faker.date().future(60, 31, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .timezone(faker.address().timeZone())
                    .eventLocationType(locationType)
                    .location(locationType == EventLocationType.IN_PERSON ? faker.address().fullAddress() : null)
                    .eventUrl(locationType == EventLocationType.ONLINE ? faker.internet().url() : null)
                    .limited(limited)
                    .attendeeLimit(limited ? faker.number().numberBetween(1, 100) : 0)
                    .approvalRequired(faker.bool().bool())
                    .imagePath("https://picsum.photos/seed/" + faker.random().nextInt(1500) + "/1000/600")
                    .key(faker.lorem().characters(5))
                    .visibility(faker.options().option(EventVisibility.PUBLIC, EventVisibility.ONLY_WITH_LINK))
                    .language((random.nextDouble() > 0.5) ? randomLanguage(random) : "en")
                    .eventApplications(new HashSet<>())
                    .build();
            event.setOrganizer(userList.get(faker.number().numberBetween(0, userCount - 1)));
            eventList.add(event);
        }
        eventRepository.saveAll(eventList);

        for (var event : eventList) {
            var attendees = new HashMap<UUID, EventApplication>();
            for (int i = 0; event.isLimited() ? i < faker.number().numberBetween(1, event.getAttendeeLimit()) : i < 20; i++) {
                var user = userList.get(faker.number().numberBetween(0, userCount - 1));
                if (!attendees.containsKey(user.getId())) {
                    attendees.put(user.getId(), new EventApplication(null, event, user, true, null));
                }
            }
            event.setEventApplications(new HashSet<>(attendees.values()));
        }
        eventRepository.saveAll(eventList);

        indexer.startAndWait();
    }

    private static String randomLanguage(Random random) {
        return Arrays.stream(ULocale.getISOLanguages()).skip((int) (ULocale.getISOLanguages().length * random.nextDouble())).findFirst().get();
    }

}