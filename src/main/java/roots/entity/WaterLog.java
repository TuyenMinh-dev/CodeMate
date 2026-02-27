package roots.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "water_log")
public class WaterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int totalAmount;

    public WaterLog() {
    }

    public WaterLog(LocalDate date, int totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDate() {
        return date;
    }
}