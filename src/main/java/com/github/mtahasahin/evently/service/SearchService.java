package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.EventSearchDto;
import com.github.mtahasahin.evently.dto.SearchRequest;
import com.github.mtahasahin.evently.dto.UserSearchDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final ElasticsearchOperations elasticsearchOperations;

    private Query createEventQuery(String query, int hitsPerPage) {
        return new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(boolQuery()
                                .should(matchBoolPrefixQuery("name", query).fuzziness(1).minimumShouldMatch("-1")))
                        .must(QueryBuilders.termQuery("visibility", "PUBLIC")))
                .withPageable(Pageable.ofSize(hitsPerPage))
                .build();
    }

    private Query createUserQuery(String query, int hitsPerPage) {
        return new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .should(matchBoolPrefixQuery("username", query).fuzziness(1).minimumShouldMatch("-1"))
                        .should(matchBoolPrefixQuery("userProfile.name", query).fuzziness(1).minimumShouldMatch("-1")))
                .withPageable(Pageable.ofSize(hitsPerPage))
                .build();
    }

    public Map<String, List> search(SearchRequest request) {

        Query eventQuery = createEventQuery(request.getQuery(), request.getHitsPerPage());

        Query userQuery = createUserQuery(request.getQuery(), request.getHitsPerPage());

        var searchHits = elasticsearchOperations
                .multiSearch(List.of(eventQuery, userQuery), List.of(EventSearchDto.class, UserSearchDto.class));
        var result = searchHits.stream().map(SearchHits::getSearchHits).collect(Collectors.toList());
        var events = (List<EventSearchDto>) result.get(0).stream().map(SearchHit::getContent).collect(Collectors.toList());
        var users = (List<UserSearchDto>) result.get(1).stream().map(SearchHit::getContent).collect(Collectors.toList());
        return Map.of("events", events, "users", users);
    }

    public Map<String, List> searchEvent(SearchRequest request) {
        Query searchQuery = createEventQuery(request.getQuery(), request.getHitsPerPage());

        var searchHits = elasticsearchOperations.search(searchQuery, EventSearchDto.class);
        var events = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return Map.of("events", events);
    }

    public Map<String, List> searchUser(SearchRequest request) {
        Query searchQuery = createUserQuery(request.getQuery(), request.getHitsPerPage());

        var searchHits = elasticsearchOperations.search(searchQuery, UserSearchDto.class);
        var users = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return Map.of("users", users);
    }

    public Map<String, List> getRandomEvents() {
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(QueryBuilders.termQuery("visibility", "PUBLIC"))
                        .must(functionScoreQuery(randomScoreFunction())))
                .withPageable(Pageable.ofSize(10))
                .build();
        var searchHits = elasticsearchOperations.search(searchQuery, EventSearchDto.class);
        var events = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return Map.of("events", events);
    }

    private RandomScoreFunctionBuilder randomScoreFunction(){
        return new RandomScoreFunctionBuilder().setWeight(1);
    }
}
