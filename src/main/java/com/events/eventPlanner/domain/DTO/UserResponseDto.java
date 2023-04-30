package com.events.eventPlanner.domain.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class UserResponseDto {

    private String name;
    private Date created;
    private Date birthdate;
    private String email;
}
