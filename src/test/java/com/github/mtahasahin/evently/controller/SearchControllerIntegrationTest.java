package com.github.mtahasahin.evently.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mtahasahin.evently.BaseIT;
import com.github.mtahasahin.evently.MassIndexerExtension;
import com.github.mtahasahin.evently.WithMockCustomUser;
import com.github.mtahasahin.evently.dto.SearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser
@ExtendWith(MassIndexerExtension.class)
public class SearchControllerIntegrationTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void search() throws Exception {
        var searchRequest = new SearchRequest();
        searchRequest.setQuery("test");
        searchRequest.setHitsPerPage(10);

        mockMvc.perform(post("/api/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.events.size()").value(1))
                .andExpect(jsonPath("$.data.users.size()").value(2));

    }

    @Test
    void searchEvent() throws Exception {
        var searchRequest = new SearchRequest();
        searchRequest.setQuery("test");
        searchRequest.setHitsPerPage(10);

        mockMvc.perform(post("/api/search/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.events.size()").value(1));

    }

    @Test
    void searchUser() throws Exception {
        var searchRequest = new SearchRequest();
        searchRequest.setQuery("test");
        searchRequest.setHitsPerPage(10);

        mockMvc.perform(post("/api/search/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users.size()").value(2));
    }
}