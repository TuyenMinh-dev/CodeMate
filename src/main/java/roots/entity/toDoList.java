package roots.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "created_at")
    private LocalDate createdAt;

    // Constructor rỗng – BẮT BUỘC cho Hibernate
    public toDoList() {
        this.createdAt = LocalDate.now();
    }

    public toDoList(String title) {
        this.title = title;
        this.completed = false;
        this.createdAt = LocalDate.now(); // Gán ngày hiện tại khi tạo mới
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

    @Override

    public String toString(){
        return title;
    }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
