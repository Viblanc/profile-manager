package com.github.viblanc.profilemanager.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.service.UserTypeService;

@WebMvcTest(UserTypeController.class)
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserTypeService userTypeService;

    private static final UserTypeDto USER_TYPE = new UserTypeDto(1L, "Admin");

    @Test
    void shouldReturnAllUserTypes() throws Exception {
        List<UserTypeDto> userTypes = List.of(new UserTypeDto(1L, "Admin"), new UserTypeDto(2L, "User"));

        when(userTypeService.findAll()).thenReturn(userTypes);

        this.mockMvc.perform(get("/api/user_types"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("Admin")))
            .andExpect(jsonPath("$[1].name", is("User")))
            .andReturn();
    }

    @Test
    void shouldReturnUserType() throws Exception {
        when(userTypeService.getUserType(USER_TYPE.id())).thenReturn(USER_TYPE);

        this.mockMvc.perform(get("/api/user_types/{id}", USER_TYPE.id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Admin")))
            .andReturn();
    }

    @Test
    void shouldReturnAddedUserType() throws Exception {
        when(userTypeService.addUserType(USER_TYPE)).thenReturn(USER_TYPE);

        this.mockMvc
            .perform(post("/api/user_types").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(USER_TYPE)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Admin")))
            .andReturn();
    }

    @Test
    void shouldReturnUpdatedUserType() throws Exception {
        when(userTypeService.editUserType(USER_TYPE.id(), USER_TYPE)).thenReturn(USER_TYPE);

        this.mockMvc
            .perform(put("/api/user_types/{id}", USER_TYPE.id()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(USER_TYPE)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Admin")))
            .andReturn();
    }

    @Test
    void shouldReturnNoContentOnUserTypeDelete() throws Exception {
        this.mockMvc.perform(delete("/api/user_types/{id}", USER_TYPE.id()))
            .andExpect(status().isNoContent())
            .andReturn();
    }

}
