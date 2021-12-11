package com.github.mtahasahin.evently.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.tokenizer("edge_ngram_3")
                .type("edge_ngram")
                .param("min_gram", "3")
                .param("max_gram", "10")
                .param("token_chars", "letter");

        context.tokenFilter("snowball_english")
                .type("snowball")
                .param("language", "English");

        context.analyzer("english").custom()
                .tokenizer("edge_ngram_3")
                .tokenFilters("lowercase", "snowball_english", "asciifolding");

        context.analyzer("name").custom()
                .tokenizer("edge_ngram_3")
                .tokenFilters("lowercase", "asciifolding");
    }
}
