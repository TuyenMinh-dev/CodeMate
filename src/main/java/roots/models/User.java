package roots.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="username", nullable = false)
    private String username;

    @Column(name ="fullname", nullable = false)
    private String fullname;

    @Column(name ="password", nullable = false)
    private String password;

    @Column(name ="email", nullable = false)
    private String email;

    public User(){}

    public User(String fullname,String username, String password, String email) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

