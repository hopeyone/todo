package com.mrhopeyone.todo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mrhopeyone.todo.domain.TodoGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodoGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoGroupDTO)) {
            return false;
        }

        TodoGroupDTO todoGroupDTO = (TodoGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todoGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
