package com.events.eventPlanner.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Data
@Component
public class Subscription {
    private int id;
    private boolean isActive;
    private Date expireDate;
}
