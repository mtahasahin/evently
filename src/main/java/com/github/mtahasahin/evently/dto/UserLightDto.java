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
    private String name;
    private String avatar;
}
