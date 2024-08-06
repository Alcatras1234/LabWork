package org.example.serverjava.repository;

import org.example.serverjava.entities.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class ActionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Action> findAll() {
        String sql = "SELECT * FROM actions";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Action.class));
    }

    public Action findById(Long id) {
        String sql = "SELECT * FROM actions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Action.class), id);
    }

    public int save(Action action) {
        String sql = "INSERT INTO actions (ip, input_text, output_text) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, action.getIp(), action.getInputText(), action.getOutputText());
    }

}
