package com.mrhopeyone.todo.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mrhopeyone.todo.domain.Todo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodoDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    private LocalDate due;

    private TodoGroupDTO todoGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public TodoGroupDTO getTodoGroup() {
        return todoGroup;
    }

    public void setTodoGroup(TodoGroupDTO todoGroup) {
        this.todoGroup = todoGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoDTO)) {
            return false;
        }

        TodoDTO todoDTO = (TodoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", due='" + getDue() + "'" +
            ", todoGroup=" + getTodoGroup() +
            "}";
    }
}
