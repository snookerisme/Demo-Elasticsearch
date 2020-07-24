package com.demo.elasticsearch.model;

import lombok.Data;

@Data
public class Employee {

    private String id;

    private String firstName;

    private String lastName;

    private String nickName;

    private String mobile;

    private String position;

    private String address;

    @Override
    public String toString() {
        return "Model{" +
                "id='" + id + '\'' +
                ", firstName=" + firstName +
                ", lastName='" + lastName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
