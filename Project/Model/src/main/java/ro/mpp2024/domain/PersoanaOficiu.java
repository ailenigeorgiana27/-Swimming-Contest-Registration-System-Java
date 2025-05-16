package ro.mpp2024.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name="PersoanaOficiu")
public class PersoanaOficiu extends Entity<Long>{
    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;

    public PersoanaOficiu(Long id, String username, String password) {
        this.setId(id);
        this.username = username;
        this.password = password;
    }
    public PersoanaOficiu() {}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersoanaOficiu oficiu = (PersoanaOficiu) o;
        return Objects.equals(username, oficiu.username) && Objects.equals(password, oficiu.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}