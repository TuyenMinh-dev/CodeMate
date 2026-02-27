package roots.services;

import roots.dao.ToDoListDAO;
import roots.entity.ToDoList;

import java.util.List;

public class ToDoService {

    public static ToDoList addTodo(String title) {
        if (title == null || title.trim().isEmpty()) return null;

        ToDoList todo = new ToDoList(title.trim());
        ToDoListDAO.save(todo);
        return todo;
    }

    public List<ToDoList> getAllTodos() {
        return ToDoListDAO.findAll();
    }

    public void update(ToDoList todo) {
        if (todo == null) return;
        ToDoListDAO.update(todo);
    }

    public void setCompleted(ToDoList todo, boolean completed) {
        if (todo == null) return;
        todo.setCompleted(completed);
        update(todo);
    }

    public void delete(ToDoList todo) {
        if (todo == null) return;
        ToDoListDAO.delete(todo);
    }
}