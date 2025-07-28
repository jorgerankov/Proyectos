package com.jor.todolist.to_do_list.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jor.todolist.to_do_list.model.TodoItem;

public interface TodoRepo extends JpaRepository<TodoItem, Long>{


}
