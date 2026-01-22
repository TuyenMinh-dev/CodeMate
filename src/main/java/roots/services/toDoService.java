package roots.services;
import roots.dao.toDoListDao;
import roots.entity.toDoList;

import java.util.List;
public class toDoService {
    public toDoList addTodo(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }

        toDoList todo = new toDoList(title);
        toDoListDao.save(todo);//lưu xuống database(hibernate insert vào mySQL)
        return todo;
    }

    // lấy toàn bộ các todoo
    public List<toDoList> getAllTodos() {
        return toDoListDao.findAll();
    }

    public boolean deleteTodo(toDoList todo){
        if (todo == null) {
            return false;
        }
        try {
            toDoListDao.delete(todo);
            return true;
        } catch (Exception e) {
            System.out.println("Delete todo failed: " + e.getMessage());
            return false;
        }

    }
    public toDoList toggleCompleted(toDoList todo) {
        if (todo == null) {
            return null;
        }
        todo.setCompleted(!todo.isCompleted());
        toDoListDao.update(todo); // LƯU DB
        return todo;
    }
    public void setCompleted(toDoList todo, boolean completed) {
        todo.setCompleted(completed);
        toDoListDao.update(todo);
    }
    public void delete(toDoList todo) {
        toDoListDao.delete(todo);
    }




}
