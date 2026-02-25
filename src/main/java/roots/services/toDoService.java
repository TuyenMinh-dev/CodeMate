package roots.services;

import roots.dao.toDoListDao;
import roots.entity.toDoList;
import java.util.List;

public class toDoService {
    public toDoList addTodo(String title, List<Integer> durations) {
        if (title == null || title.trim().isEmpty()) return null;
        toDoList todo = new toDoList(title, durations);
        toDoListDao.save(todo);
        return todo;
    }

    public void incrementActualPomo(toDoList todo) {
        if (todo == null) return;
        toDoListDao.update(todo);
    }

    public void update(toDoList todo) {
        if (todo != null) toDoListDao.update(todo);
    }

    public void setCompleted(toDoList todo, boolean completed) {
        if (todo == null) return;
        todo.setCompleted(completed);
        update(todo);
    }

    public void delete(toDoList todo) {
        if (todo != null) toDoListDao.delete(todo);
    }

}