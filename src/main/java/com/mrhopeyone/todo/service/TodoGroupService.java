package com.mrhopeyone.todo.service;

import com.mrhopeyone.todo.domain.TodoGroup;
import com.mrhopeyone.todo.repository.TodoGroupRepository;
import com.mrhopeyone.todo.service.dto.TodoGroupDTO;
import com.mrhopeyone.todo.service.mapper.TodoGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TodoGroup}.
 */
@Service
@Transactional
public class TodoGroupService {

    private final Logger log = LoggerFactory.getLogger(TodoGroupService.class);

    private final TodoGroupRepository todoGroupRepository;

    private final TodoGroupMapper todoGroupMapper;

    public TodoGroupService(TodoGroupRepository todoGroupRepository, TodoGroupMapper todoGroupMapper) {
        this.todoGroupRepository = todoGroupRepository;
        this.todoGroupMapper = todoGroupMapper;
    }

    /**
     * Save a todoGroup.
     *
     * @param todoGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public TodoGroupDTO save(TodoGroupDTO todoGroupDTO) {
        log.debug("Request to save TodoGroup : {}", todoGroupDTO);
        TodoGroup todoGroup = todoGroupMapper.toEntity(todoGroupDTO);
        todoGroup = todoGroupRepository.save(todoGroup);
        return todoGroupMapper.toDto(todoGroup);
    }

    /**
     * Update a todoGroup.
     *
     * @param todoGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public TodoGroupDTO update(TodoGroupDTO todoGroupDTO) {
        log.debug("Request to update TodoGroup : {}", todoGroupDTO);
        TodoGroup todoGroup = todoGroupMapper.toEntity(todoGroupDTO);
        todoGroup = todoGroupRepository.save(todoGroup);
        return todoGroupMapper.toDto(todoGroup);
    }

    /**
     * Partially update a todoGroup.
     *
     * @param todoGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TodoGroupDTO> partialUpdate(TodoGroupDTO todoGroupDTO) {
        log.debug("Request to partially update TodoGroup : {}", todoGroupDTO);

        return todoGroupRepository
            .findById(todoGroupDTO.getId())
            .map(existingTodoGroup -> {
                todoGroupMapper.partialUpdate(existingTodoGroup, todoGroupDTO);

                return existingTodoGroup;
            })
            .map(todoGroupRepository::save)
            .map(todoGroupMapper::toDto);
    }

    /**
     * Get all the todoGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TodoGroupDTO> findAll() {
        log.debug("Request to get all TodoGroups");
        return todoGroupRepository.findAll().stream().map(todoGroupMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one todoGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TodoGroupDTO> findOne(Long id) {
        log.debug("Request to get TodoGroup : {}", id);
        return todoGroupRepository.findById(id).map(todoGroupMapper::toDto);
    }

    /**
     * Delete the todoGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TodoGroup : {}", id);
        todoGroupRepository.deleteById(id);
    }
}
