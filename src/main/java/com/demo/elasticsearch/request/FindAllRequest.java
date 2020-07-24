package com.demo.elasticsearch.request;

import lombok.Data;

@Data
public class FindAllRequest {
    String field;
    String value;
}
