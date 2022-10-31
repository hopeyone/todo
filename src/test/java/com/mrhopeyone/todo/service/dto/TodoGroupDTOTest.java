package com.mrhopeyone.todo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodoGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoGroupDTO.class);
        TodoGroupDTO todoGroupDTO1 = new TodoGroupDTO();
        todoGroupDTO1.setId(1L);
        TodoGroupDTO todoGroupDTO2 = new TodoGroupDTO();
        assertThat(todoGroupDTO1).isNotEqualTo(todoGroupDTO2);
        todoGroupDTO2.setId(todoGroupDTO1.getId());
        assertThat(todoGroupDTO1).isEqualTo(todoGroupDTO2);
        todoGroupDTO2.setId(2L);
        assertThat(todoGroupDTO1).isNotEqualTo(todoGroupDTO2);
        todoGroupDTO1.setId(null);
        assertThat(todoGroupDTO1).isNotEqualTo(todoGroupDTO2);
    }
}
