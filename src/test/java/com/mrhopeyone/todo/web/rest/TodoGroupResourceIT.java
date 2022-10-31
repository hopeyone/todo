package com.mrhopeyone.todo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mrhopeyone.todo.IntegrationTest;
import com.mrhopeyone.todo.domain.TodoGroup;
import com.mrhopeyone.todo.repository.TodoGroupRepository;
import com.mrhopeyone.todo.service.dto.TodoGroupDTO;
import com.mrhopeyone.todo.service.mapper.TodoGroupMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TodoGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TodoGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/todo-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TodoGroupRepository todoGroupRepository;

    @Autowired
    private TodoGroupMapper todoGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTodoGroupMockMvc;

    private TodoGroup todoGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodoGroup createEntity(EntityManager em) {
        TodoGroup todoGroup = new TodoGroup().name(DEFAULT_NAME);
        return todoGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodoGroup createUpdatedEntity(EntityManager em) {
        TodoGroup todoGroup = new TodoGroup().name(UPDATED_NAME);
        return todoGroup;
    }

    @BeforeEach
    public void initTest() {
        todoGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createTodoGroup() throws Exception {
        int databaseSizeBeforeCreate = todoGroupRepository.findAll().size();
        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);
        restTodoGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TodoGroup testTodoGroup = todoGroupList.get(todoGroupList.size() - 1);
        assertThat(testTodoGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTodoGroupWithExistingId() throws Exception {
        // Create the TodoGroup with an existing ID
        todoGroup.setId(1L);
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        int databaseSizeBeforeCreate = todoGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoGroupRepository.findAll().size();
        // set the field null
        todoGroup.setName(null);

        // Create the TodoGroup, which fails.
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        restTodoGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoGroupDTO)))
            .andExpect(status().isBadRequest());

        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTodoGroups() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        // Get all the todoGroupList
        restTodoGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTodoGroup() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        // Get the todoGroup
        restTodoGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, todoGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todoGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTodoGroup() throws Exception {
        // Get the todoGroup
        restTodoGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTodoGroup() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();

        // Update the todoGroup
        TodoGroup updatedTodoGroup = todoGroupRepository.findById(todoGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTodoGroup are not directly saved in db
        em.detach(updatedTodoGroup);
        updatedTodoGroup.name(UPDATED_NAME);
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(updatedTodoGroup);

        restTodoGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
        TodoGroup testTodoGroup = todoGroupList.get(todoGroupList.size() - 1);
        assertThat(testTodoGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTodoGroupWithPatch() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();

        // Update the todoGroup using partial update
        TodoGroup partialUpdatedTodoGroup = new TodoGroup();
        partialUpdatedTodoGroup.setId(todoGroup.getId());

        restTodoGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodoGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodoGroup))
            )
            .andExpect(status().isOk());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
        TodoGroup testTodoGroup = todoGroupList.get(todoGroupList.size() - 1);
        assertThat(testTodoGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTodoGroupWithPatch() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();

        // Update the todoGroup using partial update
        TodoGroup partialUpdatedTodoGroup = new TodoGroup();
        partialUpdatedTodoGroup.setId(todoGroup.getId());

        partialUpdatedTodoGroup.name(UPDATED_NAME);

        restTodoGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodoGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodoGroup))
            )
            .andExpect(status().isOk());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
        TodoGroup testTodoGroup = todoGroupList.get(todoGroupList.size() - 1);
        assertThat(testTodoGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, todoGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTodoGroup() throws Exception {
        int databaseSizeBeforeUpdate = todoGroupRepository.findAll().size();
        todoGroup.setId(count.incrementAndGet());

        // Create the TodoGroup
        TodoGroupDTO todoGroupDTO = todoGroupMapper.toDto(todoGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(todoGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodoGroup in the database
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTodoGroup() throws Exception {
        // Initialize the database
        todoGroupRepository.saveAndFlush(todoGroup);

        int databaseSizeBeforeDelete = todoGroupRepository.findAll().size();

        // Delete the todoGroup
        restTodoGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, todoGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TodoGroup> todoGroupList = todoGroupRepository.findAll();
        assertThat(todoGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
