package com.mrhopeyone.todo.service.mapper;

import com.mrhopeyone.todo.domain.Todo;
import com.mrhopeyone.todo.domain.TodoGroup;
import com.mrhopeyone.todo.service.dto.TodoDTO;
import com.mrhopeyone.todo.service.dto.TodoGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TodoMapper extends EntityMapper<TodoDTO, Todo> {
    @Mapping(target = "todoGroup", source = "todoGroup", qualifiedByName = "todoGroupId")
    TodoDTO toDto(Todo s);

    @Named("todoGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TodoGroupDTO toDtoTodoGroupId(TodoGroup todoGroup);
}
