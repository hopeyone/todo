package com.mrhopeyone.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TodoGroup.
 */
@Entity
@Table(name = "todo_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodoGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "todoGroup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "todoGroup" }, allowSetters = true)
    private Set<Todo> todos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TodoGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TodoGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Todo> getTodos() {
        return this.todos;
    }

    public void setTodos(Set<Todo> todos) {
        if (this.todos != null) {
            this.todos.forEach(i -> i.setTodoGroup(null));
        }
        if (todos != null) {
            todos.forEach(i -> i.setTodoGroup(this));
        }
        this.todos = todos;
    }

    public TodoGroup todos(Set<Todo> todos) {
        this.setTodos(todos);
        return this;
    }

    public TodoGroup addTodo(Todo todo) {
        this.todos.add(todo);
        todo.setTodoGroup(this);
        return this;
    }

    public TodoGroup removeTodo(Todo todo) {
        this.todos.remove(todo);
        todo.setTodoGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoGroup)) {
            return false;
        }
        return id != null && id.equals(((TodoGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
