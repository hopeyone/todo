package com.mrhopeyone.todo.web.rest;

import com.mrhopeyone.todo.repository.TodoGroupRepository;
import com.mrhopeyone.todo.service.TodoGroupService;
import com.mrhopeyone.todo.service.dto.TodoGroupDTO;
import com.mrhopeyone.todo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mrhopeyone.todo.domain.TodoGroup}.
 */
@RestController
@RequestMapping("/api")
public class TodoGroupResource {

    private final Logger log = LoggerFactory.getLogger(TodoGroupResource.class);

    private static final String ENTITY_NAME = "todoTodoGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TodoGroupService todoGroupService;

    private final TodoGroupRepository todoGroupRepository;

    public TodoGroupResource(TodoGroupService todoGroupService, TodoGroupRepository todoGroupRepository) {
        this.todoGroupService = todoGroupService;
        this.todoGroupRepository = todoGroupRepository;
    }

    /**
     * {@code POST  /todo-groups} : Create a new todoGroup.
     *
     * @param todoGroupDTO the todoGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new todoGroupDTO, or with status {@code 400 (Bad Request)} if the todoGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todo-groups")
    public ResponseEntity<TodoGroupDTO> createTodoGroup(@Valid @RequestBody TodoGroupDTO todoGroupDTO) throws URISyntaxException {
        log.debug("REST request to save TodoGroup : {}", todoGroupDTO);
        if (todoGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new todoGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TodoGroupDTO result = todoGroupService.save(todoGroupDTO);
        return ResponseEntity
            .created(new URI("/api/todo-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todo-groups/:id} : Updates an existing todoGroup.
     *
     * @param id the id of the todoGroupDTO to save.
     * @param todoGroupDTO the todoGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoGroupDTO,
     * or with status {@code 400 (Bad Request)} if the todoGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the todoGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todo-groups/{id}")
    public ResponseEntity<TodoGroupDTO> updateTodoGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TodoGroupDTO todoGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TodoGroup : {}, {}", id, todoGroupDTO);
        if (todoGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TodoGroupDTO result = todoGroupService.update(todoGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todo-groups/:id} : Partial updates given fields of an existing todoGroup, field will ignore if it is null
     *
     * @param id the id of the todoGroupDTO to save.
     * @param todoGroupDTO the todoGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoGroupDTO,
     * or with status {@code 400 (Bad Request)} if the todoGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the todoGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the todoGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todo-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TodoGroupDTO> partialUpdateTodoGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TodoGroupDTO todoGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TodoGroup partially : {}, {}", id, todoGroupDTO);
        if (todoGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TodoGroupDTO> result = todoGroupService.partialUpdate(todoGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todo-groups} : get all the todoGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todoGroups in body.
     */
    @GetMapping("/todo-groups")
    public List<TodoGroupDTO> getAllTodoGroups() {
        log.debug("REST request to get all TodoGroups");
        return todoGroupService.findAll();
    }

    /**
     * {@code GET  /todo-groups/:id} : get the "id" todoGroup.
     *
     * @param id the id of the todoGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todoGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todo-groups/{id}")
    public ResponseEntity<TodoGroupDTO> getTodoGroup(@PathVariable Long id) {
        log.debug("REST request to get TodoGroup : {}", id);
        Optional<TodoGroupDTO> todoGroupDTO = todoGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todoGroupDTO);
    }

    /**
     * {@code DELETE  /todo-groups/:id} : delete the "id" todoGroup.
     *
     * @param id the id of the todoGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todo-groups/{id}")
    public ResponseEntity<Void> deleteTodoGroup(@PathVariable Long id) {
        log.debug("REST request to delete TodoGroup : {}", id);
        todoGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
