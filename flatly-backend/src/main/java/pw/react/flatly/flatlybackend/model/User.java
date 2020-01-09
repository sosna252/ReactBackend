package pw.react.flatly.flatlybackend.model;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID UserId;

    @Column(name = "login")
    private String Login;

    @Column(name = "password")
    private String Password;

    @Column(name = "first_name")
    private String FirstName;

    @Column(name = "last_name")
    private String LastName;

    @Column(name = "security_token")
    private UUID SecurityToken;

    @ElementCollection
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    public User() {}
}
