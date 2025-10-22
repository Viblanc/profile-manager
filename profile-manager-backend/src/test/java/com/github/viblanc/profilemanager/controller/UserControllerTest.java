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
import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;

    private static final UserTypeDto USER_TYPE = new UserTypeDto(1L, "Admin");

    private static final UserDto USER = new UserDto(1L, "John", "Doe", "john@doe.mail", USER_TYPE);

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> users = List.of(new UserDto(1L, "John", "Doe", "john@doe.mail", USER_TYPE),
                new UserDto(1L, "Jane", "Doe", "jane@doe.mail", USER_TYPE));

        when(userService.findAll()).thenReturn(users);

        this.mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].email", is("john@doe.mail")))
            .andExpect(jsonPath("$[1].email", is("jane@doe.mail")))
            .andReturn();
    }

    @Test
    void shouldReturnUserType() throws Exception {
        when(userService.getUser(USER.id())).thenReturn(USER);

        this.mockMvc.perform(get("/api/users/{id}", USER.id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.email", is("john@doe.mail")))
            .andReturn();
    }

    @Test
    void shouldReturnAddedUser() throws Exception {
        when(userService.addUser(USER)).thenReturn(USER);

        this.mockMvc
            .perform(
                    post("/api/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(USER)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.email", is("john@doe.mail")))
            .andReturn();
    }

    @Test
    void shouldReturnUpdatedUserType() throws Exception {
        when(userService.editUser(USER_TYPE.id(), USER)).thenReturn(USER);

        this.mockMvc
            .perform(put("/api/users/{id}", USER.id()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(USER)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.email", is("john@doe.mail")))
            .andReturn();
    }

    @Test
    void shouldReturnNoContentOnUserDelete() throws Exception {
        this.mockMvc.perform(delete("/api/users/{id}", USER.id())).andExpect(status().isNoContent()).andReturn();
    }

}
