package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.BaseIT;
import com.github.mtahasahin.evently.WithMockCustomUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.entity.EventApplication;
import com.github.mtahasahin.evently.entity.EventQuestion;
import com.github.mtahasahin.evently.entity.EventQuestionAnswer;
import com.github.mtahasahin.evently.enums.EventQuestionType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.repository.EventApplicationRepository;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.util.ImageUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EventControllerIntegrationTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventApplicationRepository eventApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @WithMockCustomUser
    public void whenEventNotExists_returnNotFound() throws Exception {
        mockMvc.perform(get("/api/event/my-event")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    public void whenEventExists_returnEvent() throws Exception {
        mockMvc.perform(get("/api/event/test-event-a83jkm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Event"))
                .andExpect(jsonPath("$.data.description").value("Test Event Description"))
                .andExpect(jsonPath("$.data.location").value("Test Location"))
                .andExpect(jsonPath("$.data.organizer.username").value("test-user"))
                .andExpect(jsonPath("$.data.organizer.name").value("Test User"))
                .andExpect(jsonPath("$.data.limited").value(false))
                .andExpect(jsonPath("$.data.approvalRequired").value(false))
                .andExpect(jsonPath("$.data.imagePath").value("https://picsum.photos/seed/100/1000/600"))
                .andExpect(jsonPath("$.data.key").doesNotExist())
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.language").value("en"))
                .andExpect(jsonPath("$.data.timezone").value("Europe/Istanbul"));
    }

    @Test
    public void whenEventIsNotPublicAndKeyIsNotProvided_returnNotFound() throws Exception {
        Event event = eventRepository.findBySlug("test-event-a83jkm").orElseThrow();
        event.setVisibility(EventVisibility.ONLY_WITH_LINK);
        eventRepository.save(event);

        mockMvc.perform(get("/api/event/test-event-a83jkm"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenEventIsNotPublicAndKeyIsProvided_returnEvent() throws Exception {
        Event event = eventRepository.findBySlug("test-event-a83jkm").orElseThrow();
        event.setVisibility(EventVisibility.ONLY_WITH_LINK);
        eventRepository.save(event);

        mockMvc.perform(get("/api/event/test-event-a83jkm?key=" + event.getKey()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Event"))
                .andExpect(jsonPath("$.data.description").value("Test Event Description"))
                .andExpect(jsonPath("$.data.location").value("Test Location"))
                .andExpect(jsonPath("$.data.organizer.username").value("test-user"))
                .andExpect(jsonPath("$.data.organizer.name").value("Test User"))
                .andExpect(jsonPath("$.data.limited").value(false))
                .andExpect(jsonPath("$.data.approvalRequired").value(false))
                .andExpect(jsonPath("$.data.imagePath").value("https://picsum.photos/seed/100/1000/600"))
                .andExpect(jsonPath("$.data.key").doesNotExist())
                .andExpect(jsonPath("$.data.visibility").value("ONLY_WITH_LINK"))
                .andExpect(jsonPath("$.data.language").value("en"))
                .andExpect(jsonPath("$.data.timezone").value("Europe/Istanbul"));
    }

    @Test
    @WithMockCustomUser
    public void createEvent_Success() throws Exception {
        BufferedImage image = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);

        mockMvc.perform(multipart("/api/event")
                        .file(new MockMultipartFile("image", "test.jpg", "image/jpeg", ImageUtils.toByteArray(image,"jpeg")))
                        .param("startDate", LocalDateTime.now().plusDays(1).toString())
                        .param("endDate", LocalDateTime.now().plusDays(5).toString())
                        .param("timezone", "Europe/Istanbul")
                        .param("eventUrl", "https://www.event.com/test")
                        .param("name", "Test Event")
                        .param("description", "Test Event Description")
                        .param("eventLocationType", "IN_PERSON")
                        .param("location", "Test Location")
                        .param("limited", "false")
                        .param("approvalRequired", "false")
                        .param("visibility", "PUBLIC")
                        .param("language", "en")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Event"))
                .andExpect(jsonPath("$.data.description").value("Test Event Description"))
                .andExpect(jsonPath("$.data.location").value("Test Location"))
                .andExpect(jsonPath("$.data.organizer.username").value("test-user"))
                .andExpect(jsonPath("$.data.organizer.name").value("Test User"))
                .andExpect(jsonPath("$.data.limited").value(false))
                .andExpect(jsonPath("$.data.approvalRequired").value(false))
                .andExpect(jsonPath("$.data.imagePath").exists())
                .andExpect(jsonPath("$.data.key").doesNotExist())
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.language").value("en"))
                .andExpect(jsonPath("$.data.timezone").value("Europe/Istanbul"));

    }

    @Test
    @WithMockCustomUser
    public void updateEvent_Success() throws Exception {

        mockMvc.perform(multipart("/api/event" + "/test-event-a83jkm")
                        .param("startDate", LocalDateTime.now().plusDays(1).toString())
                        .param("endDate", LocalDateTime.now().plusDays(5).toString())
                        .param("timezone", "Europe/Istanbul")
                        .param("eventUrl", "https://www.event.com/test")
                        .param("name", "Test Event")
                        .param("description", "Test Event Description")
                        .param("eventLocationType", "IN_PERSON")
                        .param("location", "Test Location")
                        .param("limited", "false")
                        .param("approvalRequired", "false")
                        .param("visibility", "PUBLIC")
                        .param("language", "en")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Event"))
                .andExpect(jsonPath("$.data.description").value("Test Event Description"))
                .andExpect(jsonPath("$.data.location").value("Test Location"))
                .andExpect(jsonPath("$.data.organizer.username").value("test-user"))
                .andExpect(jsonPath("$.data.organizer.name").value("Test User"))
                .andExpect(jsonPath("$.data.limited").value(false))
                .andExpect(jsonPath("$.data.approvalRequired").value(false))
                .andExpect(jsonPath("$.data.imagePath").exists())
                .andExpect(jsonPath("$.data.key").doesNotExist())
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.language").value("en"))
                .andExpect(jsonPath("$.data.timezone").value("Europe/Istanbul"));

    }

    @Test
    @WithMockCustomUser
    public void removeEvent_Success() throws Exception {
        mockMvc.perform(delete("/api/event/" + "test-event-a83jkm"))
                .andExpect(status().isOk());

        Assertions.assertFalse(eventRepository.findBySlug("test-event-a83jkm").isPresent());
    }

    @Test
    @WithMockCustomUser
    public void getAttendees_Success() throws Exception {
        var event = eventRepository.findBySlug("test-event-a83jkm").get();
        var attendee = userRepository.findByUsername("test-user").get();
        eventApplicationRepository.save(new EventApplication(null, event, attendee, true, Set.of()));

        mockMvc.perform(get("/api/event/" + "test-event-a83jkm" + "/attendees")
                        .param("page","0")
                        .param("limit","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].username").value("test-user"));
    }

    @Test
    @WithMockCustomUser
    public void updateEventQuestions_Success() throws Exception {
        mockMvc.perform(put("/api/event/" + "test-event-a83jkm" + "/questions")
                        .content("[{\"title\":\"Test Question\",\"description\":\"description\",\"type\":\"SHORT_TEXT\",\"required\":true,\"order\":1}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test Question"))
                .andExpect(jsonPath("$.data[0].description").value("description"))
                .andExpect(jsonPath("$.data[0].type").value("SHORT_TEXT"))
                .andExpect(jsonPath("$.data[0].required").value(true))
                .andExpect(jsonPath("$.data[0].order").value(1));
    }

    @Test
    @WithMockCustomUser
    public void getAnswer_Success() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var event = eventRepository.findBySlug("test-event-a83jkm").get();
        var question = new EventQuestion(null, event, new HashSet<EventQuestionAnswer>(), EventQuestionType.SHORT_TEXT, 1, "Test Question", "description", true);
        entityManager.persist(question);

        var attendee = userRepository.findByUsername("test-user").get();
        var application = new EventApplication(null, event, attendee, true, null);
        var answer = new EventQuestionAnswer(null, question, application, "Test Answer");
        application.setAnswers(Set.of(answer));
        entityManager.persist(application);
        entityManager.getTransaction().commit();
        entityManager.close();
        mockMvc.perform(get("/api/event/" + "test-event-a83jkm" + "/answers/"+ application.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questions[0].title").value("Test Question"))
                .andExpect(jsonPath("$.data.questions[0].description").value("description"))
                .andExpect(jsonPath("$.data.questions[0].type").value("SHORT_TEXT"))
                .andExpect(jsonPath("$.data.questions[0].required").value(true))
                .andExpect(jsonPath("$.data.questions[0].order").value(1))
                .andExpect(jsonPath("$.data.answers[0].questionId").value(question.getId().toString()))
                .andExpect(jsonPath("$.data.answers[0].answer").value("Test Answer"));
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void applyEvent_Success() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var event = eventRepository.findBySlug("test-event-a83jkm").get();
        var question = new EventQuestion(null, event, new HashSet<EventQuestionAnswer>(), EventQuestionType.SHORT_TEXT, 1, "Test Question", "description", true);
        entityManager.persist(question);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(post("/api/event/" + "test-event-a83jkm" + "/answers")
                        .content("[{\"questionId\":\"" + question.getId() + "\",\"answer\":\"Test Answer\"}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockCustomUser(username = "test-user2")
    public void cancelApplication_Success() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var event = eventRepository.findBySlug("test-event-a83jkm").get();
        var question = new EventQuestion(null, event, new HashSet<EventQuestionAnswer>(), EventQuestionType.SHORT_TEXT, 1, "Test Question", "description", true);
        entityManager.persist(question);

        var attendee = userRepository.findByUsername("test-user2").get();
        var application = new EventApplication(null, event, attendee, true, null);
        var answer = new EventQuestionAnswer(null, question, application, "Test Answer");
        application.setAnswers(Set.of(answer));
        entityManager.persist(application);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(delete("/api/event/" + "test-event-a83jkm" + "/answers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    public void confirmApplication_Success() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var event = eventRepository.findBySlug("test-event-a83jkm").get();
        event.setApprovalRequired(true);
        entityManager.merge(event);

        var attendee = userRepository.findByUsername("test-user2").get();
        var application = new EventApplication(null, event, attendee, true, Set.of());
        entityManager.persist(application);
        entityManager.getTransaction().commit();
        entityManager.close();

        mockMvc.perform(post("/api/event/" + "test-event-a83jkm" + "/answers/" + application.getId() + "/approve"))
                .andExpect(status().isOk());

        var confirmed = eventApplicationRepository.findByEventSlugAndApplicantId("test-event-a83jkm", attendee.getId()).get().isConfirmed();
        Assertions.assertTrue(confirmed);
    }

}
