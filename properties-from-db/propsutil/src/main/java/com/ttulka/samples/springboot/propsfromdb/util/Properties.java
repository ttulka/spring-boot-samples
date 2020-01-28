package com.ttulka.samples.springboot.propsfromdb.util;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Properties {

    private final String tableName;

    private final JdbcTemplate jdbcTemplate;

    public Set<Property> all() {
        return jdbcTemplate.queryForList("SELECT name, value FROM " + tableName).stream()
                .map(m -> new Property((String) m.get("name"), (String) m.get("value")))
                .collect(Collectors.toSet());
    }

    public Optional<Property> byName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                    "SELECT name, value FROM " + tableName + " WHERE name = ?",
                    new Object[]{name},
                    (rs, rowNum) -> new Property(rs.getString(1), rs.getString(2)))
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(String name, Object value) {
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName + " WHERE name = ?",
                                        new Object[]{name}, Integer.class) > 0) {
            jdbcTemplate.update("UPDATE " + tableName + " SET value = ? WHERE name = ?", value, name);
        } else {
            jdbcTemplate.update("INSERT INTO " + tableName + " VALUES (?, ?)", name, value);
        }
    }
}
