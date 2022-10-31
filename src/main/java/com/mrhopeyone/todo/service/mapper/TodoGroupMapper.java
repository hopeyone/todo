package com.mrhopeyone.todo.service.mapper;

import com.mrhopeyone.todo.domain.TodoGroup;
import com.mrhopeyone.todo.service.dto.TodoGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TodoGroup} and its DTO {@link TodoGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface TodoGroupMapper extends EntityMapper<TodoGroupDTO, TodoGroup> {}
