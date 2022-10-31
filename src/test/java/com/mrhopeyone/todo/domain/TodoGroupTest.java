package com.mrhopeyone.todo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodoGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoGroup.class);
        TodoGroup todoGroup1 = new TodoGroup();
        todoGroup1.setId(1L);
        TodoGroup todoGroup2 = new TodoGroup();
        todoGroup2.setId(todoGroup1.getId());
        assertThat(todoGroup1).isEqualTo(todoGroup2);
        todoGroup2.setId(2L);
        assertThat(todoGroup1).isNotEqualTo(todoGroup2);
        todoGroup1.setId(null);
        assertThat(todoGroup1).isNotEqualTo(todoGroup2);
    }
}
