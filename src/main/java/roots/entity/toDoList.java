package roots.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "todo_list")

public class toDoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private boolean completed;

    // Constructor rỗng – BẮT BUỘC cho Hibernate
    public toDoList() {
    }

    public toDoList(String title) {
        this.title = title;
        this.completed = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
