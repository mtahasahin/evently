package com.github.mtahasahin.evently.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLightDto {
    private UUID id;
    private String username;
    private String name;
    private String avatar;
}
