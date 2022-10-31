package com.mrhopeyone.todo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TodoGroupMapperTest {

    private TodoGroupMapper todoGroupMapper;

    @BeforeEach
    public void setUp() {
        todoGroupMapper = new TodoGroupMapperImpl();
    }
}
