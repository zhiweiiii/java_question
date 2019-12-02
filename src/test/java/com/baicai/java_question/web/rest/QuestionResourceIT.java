package com.baicai.java_question.web.rest;

import com.baicai.java_question.JavaQuestionApp;
import com.baicai.java_question.domain.Question;
import com.baicai.java_question.domain.Comment;
import com.baicai.java_question.repository.QuestionRepository;
import com.baicai.java_question.service.QuestionService;
import com.baicai.java_question.service.dto.QuestionDTO;
import com.baicai.java_question.service.mapper.QuestionMapper;
import com.baicai.java_question.web.rest.errors.ExceptionTranslator;
import com.baicai.java_question.service.dto.QuestionCriteria;
import com.baicai.java_question.service.QuestionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.baicai.java_question.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@SpringBootTest(classes = JavaQuestionApp.class)
public class QuestionResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_ID = 1L;
    private static final Long UPDATED_CREATE_ID = 2L;
    private static final Long SMALLER_CREATE_ID = 1L - 1L;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Integer DEFAULT_STAR = 1;
    private static final Integer UPDATED_STAR = 2;
    private static final Integer SMALLER_STAR = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionQueryService questionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restQuestionMockMvc;

    private Question question;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionResource questionResource = new QuestionResource(questionService, questionQueryService);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .content(DEFAULT_CONTENT)
            .answer(DEFAULT_ANSWER)
            .createId(DEFAULT_CREATE_ID)
            .remark(DEFAULT_REMARK)
            .star(DEFAULT_STAR)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return question;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .content(UPDATED_CONTENT)
            .answer(UPDATED_ANSWER)
            .createId(UPDATED_CREATE_ID)
            .remark(UPDATED_REMARK)
            .star(UPDATED_STAR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testQuestion.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testQuestion.getCreateId()).isEqualTo(DEFAULT_CREATE_ID);
        assertThat(testQuestion.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testQuestion.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testQuestion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testQuestion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testQuestion.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testQuestion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].createId").value(hasItem(DEFAULT_CREATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.createId").value(DEFAULT_CREATE_ID.intValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId equals to DEFAULT_CREATE_ID
        defaultQuestionShouldBeFound("createId.equals=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId equals to UPDATED_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.equals=" + UPDATED_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId not equals to DEFAULT_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.notEquals=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId not equals to UPDATED_CREATE_ID
        defaultQuestionShouldBeFound("createId.notEquals=" + UPDATED_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId in DEFAULT_CREATE_ID or UPDATED_CREATE_ID
        defaultQuestionShouldBeFound("createId.in=" + DEFAULT_CREATE_ID + "," + UPDATED_CREATE_ID);

        // Get all the questionList where createId equals to UPDATED_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.in=" + UPDATED_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId is not null
        defaultQuestionShouldBeFound("createId.specified=true");

        // Get all the questionList where createId is null
        defaultQuestionShouldNotBeFound("createId.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId is greater than or equal to DEFAULT_CREATE_ID
        defaultQuestionShouldBeFound("createId.greaterThanOrEqual=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId is greater than or equal to UPDATED_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.greaterThanOrEqual=" + UPDATED_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId is less than or equal to DEFAULT_CREATE_ID
        defaultQuestionShouldBeFound("createId.lessThanOrEqual=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId is less than or equal to SMALLER_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.lessThanOrEqual=" + SMALLER_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId is less than DEFAULT_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.lessThan=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId is less than UPDATED_CREATE_ID
        defaultQuestionShouldBeFound("createId.lessThan=" + UPDATED_CREATE_ID);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreateIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createId is greater than DEFAULT_CREATE_ID
        defaultQuestionShouldNotBeFound("createId.greaterThan=" + DEFAULT_CREATE_ID);

        // Get all the questionList where createId is greater than SMALLER_CREATE_ID
        defaultQuestionShouldBeFound("createId.greaterThan=" + SMALLER_CREATE_ID);
    }


    @Test
    @Transactional
    public void getAllQuestionsByStarIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star equals to DEFAULT_STAR
        defaultQuestionShouldBeFound("star.equals=" + DEFAULT_STAR);

        // Get all the questionList where star equals to UPDATED_STAR
        defaultQuestionShouldNotBeFound("star.equals=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star not equals to DEFAULT_STAR
        defaultQuestionShouldNotBeFound("star.notEquals=" + DEFAULT_STAR);

        // Get all the questionList where star not equals to UPDATED_STAR
        defaultQuestionShouldBeFound("star.notEquals=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star in DEFAULT_STAR or UPDATED_STAR
        defaultQuestionShouldBeFound("star.in=" + DEFAULT_STAR + "," + UPDATED_STAR);

        // Get all the questionList where star equals to UPDATED_STAR
        defaultQuestionShouldNotBeFound("star.in=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star is not null
        defaultQuestionShouldBeFound("star.specified=true");

        // Get all the questionList where star is null
        defaultQuestionShouldNotBeFound("star.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star is greater than or equal to DEFAULT_STAR
        defaultQuestionShouldBeFound("star.greaterThanOrEqual=" + DEFAULT_STAR);

        // Get all the questionList where star is greater than or equal to UPDATED_STAR
        defaultQuestionShouldNotBeFound("star.greaterThanOrEqual=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star is less than or equal to DEFAULT_STAR
        defaultQuestionShouldBeFound("star.lessThanOrEqual=" + DEFAULT_STAR);

        // Get all the questionList where star is less than or equal to SMALLER_STAR
        defaultQuestionShouldNotBeFound("star.lessThanOrEqual=" + SMALLER_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star is less than DEFAULT_STAR
        defaultQuestionShouldNotBeFound("star.lessThan=" + DEFAULT_STAR);

        // Get all the questionList where star is less than UPDATED_STAR
        defaultQuestionShouldBeFound("star.lessThan=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllQuestionsByStarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where star is greater than DEFAULT_STAR
        defaultQuestionShouldNotBeFound("star.greaterThan=" + DEFAULT_STAR);

        // Get all the questionList where star is greater than SMALLER_STAR
        defaultQuestionShouldBeFound("star.greaterThan=" + SMALLER_STAR);
    }


    @Test
    @Transactional
    public void getAllQuestionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy equals to DEFAULT_CREATED_BY
        defaultQuestionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the questionList where createdBy equals to UPDATED_CREATED_BY
        defaultQuestionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy not equals to DEFAULT_CREATED_BY
        defaultQuestionShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the questionList where createdBy not equals to UPDATED_CREATED_BY
        defaultQuestionShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultQuestionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the questionList where createdBy equals to UPDATED_CREATED_BY
        defaultQuestionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy is not null
        defaultQuestionShouldBeFound("createdBy.specified=true");

        // Get all the questionList where createdBy is null
        defaultQuestionShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy contains DEFAULT_CREATED_BY
        defaultQuestionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the questionList where createdBy contains UPDATED_CREATED_BY
        defaultQuestionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultQuestionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the questionList where createdBy does not contain UPDATED_CREATED_BY
        defaultQuestionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllQuestionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultQuestionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the questionList where createdDate equals to UPDATED_CREATED_DATE
        defaultQuestionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultQuestionShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the questionList where createdDate not equals to UPDATED_CREATED_DATE
        defaultQuestionShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultQuestionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the questionList where createdDate equals to UPDATED_CREATED_DATE
        defaultQuestionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createdDate is not null
        defaultQuestionShouldBeFound("createdDate.specified=true");

        // Get all the questionList where createdDate is null
        defaultQuestionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultQuestionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultQuestionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the questionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy is not null
        defaultQuestionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the questionList where lastModifiedBy is null
        defaultQuestionShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultQuestionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultQuestionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultQuestionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultQuestionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the questionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultQuestionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultQuestionShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the questionList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultQuestionShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultQuestionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the questionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultQuestionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModifiedDate is not null
        defaultQuestionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the questionList where lastModifiedDate is null
        defaultQuestionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        Comment comments = CommentResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        question.addComments(comments);
        questionRepository.saveAndFlush(question);
        Long commentsId = comments.getId();

        // Get all the questionList where comments equals to commentsId
        defaultQuestionShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the questionList where comments equals to commentsId + 1
        defaultQuestionShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].createId").value(hasItem(DEFAULT_CREATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .content(UPDATED_CONTENT)
            .answer(UPDATED_ANSWER)
            .createId(UPDATED_CREATE_ID)
            .remark(UPDATED_REMARK)
            .star(UPDATED_STAR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testQuestion.getCreateId()).isEqualTo(UPDATED_CREATE_ID);
        assertThat(testQuestion.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testQuestion.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testQuestion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testQuestion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = new Question();
        question1.setId(1L);
        Question question2 = new Question();
        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);
        question2.setId(2L);
        assertThat(question1).isNotEqualTo(question2);
        question1.setId(null);
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionDTO.class);
        QuestionDTO questionDTO1 = new QuestionDTO();
        questionDTO1.setId(1L);
        QuestionDTO questionDTO2 = new QuestionDTO();
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
        questionDTO2.setId(questionDTO1.getId());
        assertThat(questionDTO1).isEqualTo(questionDTO2);
        questionDTO2.setId(2L);
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
        questionDTO1.setId(null);
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(questionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(questionMapper.fromId(null)).isNull();
    }
}
