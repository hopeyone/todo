package com.mrhopeyone.todo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TodoMapperTest {

    private TodoMapper todoMapper;

    @BeforeEach
    public void setUp() {
        todoMapper = new TodoMapperImpl();
    }
}
