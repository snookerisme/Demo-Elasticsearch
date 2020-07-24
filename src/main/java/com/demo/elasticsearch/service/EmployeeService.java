package com.demo.elasticsearch.service;

import com.demo.elasticsearch.model.Employee;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexBoost;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.demo.elasticsearch.constant.ApplicationConstants.*;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(MyModelService.class);

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    private final ReactiveElasticsearchClient reactiveElasticsearchClient;

    public EmployeeService(ReactiveElasticsearchOperations reactiveElasticsearchOperations,
                          ReactiveElasticsearchClient reactiveElasticsearchClient) {
        this.reactiveElasticsearchOperations = reactiveElasticsearchOperations;
        this.reactiveElasticsearchClient = reactiveElasticsearchClient;
    }

    @PostConstruct
    private void checkIndexExists(){

        GetIndexRequest request = new GetIndexRequest();
        request.indices(EMPLOYEE_ES_INDEX);

        reactiveElasticsearchClient.indices()
                .existsIndex(request)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .flatMap(indexExists -> {
                    logger.info("Index {} exists: {}", EMPLOYEE_ES_INDEX, indexExists);
                    if (!indexExists)
                        return createIndex();
                    else
                        return Mono.empty();
                })
                .block();
    }

    private Mono<Void> createIndex(){

        CreateIndexRequest request = new CreateIndexRequest();
        request.index(EMPLOYEE_ES_INDEX);
        request.mapping(DEFAULT_ES_DOC_TYPE,
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"timestamp\": {\n" +
                        "      \"type\": \"date\",\n" +
                        "      \"format\": \"epoch_millis||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);

        return reactiveElasticsearchClient.indices()
                .createIndex(request)
                .doOnSuccess(aVoid -> logger.info("Created Index {}", EMPLOYEE_ES_INDEX))
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Mono<Employee> findEmployeeById(String id){

        return reactiveElasticsearchOperations.get(
                id,
                Employee.class,
                IndexCoordinates.of(EMPLOYEE_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Flux<Employee> findAllEmployee(String field, String value){

        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();

        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {

            query.withQuery(QueryBuilders.matchQuery(field, value));
        }

        return reactiveElasticsearchOperations.search(
                query.build(),
                Employee.class,
                IndexCoordinates.of(EMPLOYEE_ES_INDEX)
        )
                .map(SearchHit::getContent)
                .filter(Objects::nonNull)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Mono<Employee> saveEmployee(Employee employee){

        return reactiveElasticsearchOperations.save(
                employee,
                IndexCoordinates.of(EMPLOYEE_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Mono<String> deleteEmployeeById(String id){

        return reactiveElasticsearchOperations.delete(
                id,
                IndexCoordinates.of(EMPLOYEE_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Flux<Employee> findManyFieldEmployee(String value){

        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();

        if (!StringUtils.isEmpty(value)) {

            query.withQuery(QueryBuilders.multiMatchQuery(value,"firstName","lastName"));
        }

        return reactiveElasticsearchOperations.search(
                query.build(),
                Employee.class,
                IndexCoordinates.of(EMPLOYEE_ES_INDEX)
        )
                .map(SearchHit::getContent)
                .filter(Objects::nonNull)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }
}
