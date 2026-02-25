package roots.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "todo_list")
public class toDoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private boolean completed;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "session_durations_str")
    private String sessionDurationsStr = "30";

    @Transient
    private List<Integer> sessionDurations = new ArrayList<>();

    public toDoList() {
        this.createdAt = LocalDate.now();
    }

    public toDoList(String title, List<Integer> durations) {
        this.title = title;
        this.setSessionDurations(durations);
        this.completed = false;
        this.createdAt = LocalDate.now();
    }

    @PostLoad
    private void onLoad() {
        if (sessionDurationsStr != null && !sessionDurationsStr.isEmpty()) {
            this.sessionDurations = Arrays.stream(sessionDurationsStr.split(","))
                    .map(Integer::parseInt).collect(Collectors.toList());
        }
    }

    public List<Integer> getSessionDurations() {
        return sessionDurations;
    }

    public void setSessionDurations(List<Integer> durations) {
        this.sessionDurations = durations;
        this.sessionDurationsStr = durations.stream()
                .map(String::valueOf).collect(Collectors.joining(","));
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    } // Fix lỗi Hình 8, 10

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}