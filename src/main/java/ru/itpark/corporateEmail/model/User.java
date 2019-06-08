package ru.itpark.corporateEmail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private Integer id;

    private String name;

    private String login;

    private String email;

    private Integer departmentId;

}
