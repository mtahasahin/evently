package com.github.mtahasahin.evently.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String query;
    private String filter;
    private int hitsPerPage;
}
