package com.github.mtahasahin.evently.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLightDto {
    private Long id;
    private String username;

    public UserLightDto(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    private String name;
    private String avatar;
}
