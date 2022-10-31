package com.mrhopeyone.todo.repository;

import com.mrhopeyone.todo.domain.TodoGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TodoGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup, Long> {}
