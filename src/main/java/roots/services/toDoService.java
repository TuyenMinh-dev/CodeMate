package roots.services;

import roots.dao.toDoListDao;
import roots.entity.toDoList;

import java.util.List;

public class toDoService {

    public toDoList addTodo(String title) {
        if (title == null || title.trim().isEmpty()) return null;

        toDoList todo = new toDoList(title.trim());
        toDoListDao.save(todo);
        return todo;
    }

    public List<toDoList> getAllTodos() {
        return toDoListDao.findAll();
    }

    public void update(toDoList todo) {
        if (todo == null) return;
        toDoListDao.update(todo);
    }

    public void setCompleted(toDoList todo, boolean completed) {
        if (todo == null) return;
        todo.setCompleted(completed);
        update(todo);
    }

    public void delete(toDoList todo) {
        if (todo == null) return;
        toDoListDao.delete(todo);
    }
}