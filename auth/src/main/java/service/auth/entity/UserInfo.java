package service.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private long study_id;
    private String name;
    private String email;
    private String password;
    private String roles;

    public UserInfo() {
    }

    public UserInfo(long study_id, String name, String email, String password, String roles) {
        this.study_id = study_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

}
