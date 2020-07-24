package com.demo.elasticsearch.model;

import lombok.Data;

@Data
public class MyModel {

    private String id;

    private Long timestamp;

    private String data;

    @Override
    public String toString() {
        return "Model{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                '}';
    }
}
