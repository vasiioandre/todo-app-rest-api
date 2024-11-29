package com.in28minutes.rest.webservices.restfulwebservices.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TodoResourceTest {

    @InjectMocks
    private TodoResource todoResource;

    @Mock
    private TodoService todoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void retrieveTodos_shouldReturnTodos() {
        String username = "user1";
        Todo todo1 = new Todo(1, username, "Description 1", LocalDate.now().plusYears(1), false);
        Todo todo2 = new Todo(2, username, "Description 2", LocalDate.now().plusYears(2), true);
        List<Todo> todos = Arrays.asList(todo1, todo2);
        when(todoService.findByUsername(username)).thenReturn(todos);

        List<Todo> retrievedTodos = todoResource.retrieveTodos(username);

        assertThat(retrievedTodos).hasSize(2);
        assertThat(retrievedTodos).containsExactly(todo1, todo2);
    }

    @Test
    public void retrieveTodo_shouldReturnTodo() {
        String username = "user1";
        int id = 1;
        Todo todo = new Todo(id, username, "Description", LocalDate.now().plusYears(1), false);
        when(todoService.findById(id)).thenReturn(todo);

        Todo retrievedTodo = todoResource.retrieveTodo(username, id);

        assertThat(retrievedTodo).isEqualTo(todo);
    }

	@Test
    public void deleteTodo_shouldReturnNoContent() {
        String username = "user1";
        int id = 1;
        doNothing().when(todoService).deleteById(id);

        ResponseEntity<Void> response = todoResource.deleteTodo(username, id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    public void testCreateTodo() {
        Todo mockTodo = new Todo(1, "ioana", "New Todo", LocalDate.now(), false);
        when(todoService.addTodo(eq("ioana"), anyString(), any(LocalDate.class), eq(false)))
            .thenReturn(mockTodo);

        Todo createdTodo = todoResource.createTodo("ioana", mockTodo);

        assertEquals("New Todo", createdTodo.getDescription());
        assertEquals(false, createdTodo.isDone());
        verify(todoService, times(1)).addTodo(eq("ioana"), anyString(), any(LocalDate.class), eq(false));
    }
}
