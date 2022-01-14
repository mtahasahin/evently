package com.github.mtahasahin.evently.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Data
@Document(indexName = "user_index")
public class UserSearchDto {
    private UUID id;
    private String username;
    @JsonProperty("profile")
    private ProfileSearchDto userProfile;

    @Data
    public static class ProfileSearchDto {
        private String name;
        private String about;
        private String avatar;
    }
}


