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

    // lấy toàn bộ các
    public List<toDoList> getAllTodos() {
        return toDoListDao.findAll();
    }
}
