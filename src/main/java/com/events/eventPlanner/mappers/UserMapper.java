package com.events.eventPlanner.mappers;

import com.events.eventPlanner.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setCreated(rs.getDate("created"));
        user.setEdited(rs.getDate("edited"));
        user.setDeleted(rs.getBoolean("is_deleted"));
        user.setBirthdate(rs.getDate("birthday_date"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
