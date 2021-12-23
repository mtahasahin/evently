package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.EventSearchDto;
import com.github.mtahasahin.evently.dto.UpcomingEventsRequest;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@RequiredArgsConstructor
public class UpcomingEventsService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS");

    public Object getUpcomingEvents(UpcomingEventsRequest request) {
        var pageable = PageRequest.of(request.getPage(), request.getLimit(), Sort.by(Sort.Direction.ASC, "startDate"));
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(QueryBuilders.termQuery("visibility", "PUBLIC"))
                        .must(matchQuery("language", request.getLanguage()))
                        .must(QueryBuilders.rangeQuery("startDate")
                                .gte(LocalDateTime.now().format(formatter))
                                .lte(LocalDateTime.now().plusDays(90).format(formatter))))
                .withPageable(pageable)
                .build();
        var searchHits = elasticsearchOperations.search(searchQuery, EventSearchDto.class);
        var events = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return Map.of("events", events);
    }

    public Map<String, List> getRandomEvents() {
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(QueryBuilders.termQuery("visibility", "PUBLIC"))
                        .must(functionScoreQuery(randomScoreFunction()))
                        .must(QueryBuilders.rangeQuery("startDate")
                                .gte(LocalDateTime.now().format(formatter))))
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
