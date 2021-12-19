package com.github.mtahasahin.evently.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "user_index")
public class UserSearchDto {
    private Long id;
    private String username;
    @JsonProperty("profile")
    private ProfileSearchDto userProfile;
}

@Data
class ProfileSearchDto {
    private String name;
    private String about;
    private String avatar;
}
