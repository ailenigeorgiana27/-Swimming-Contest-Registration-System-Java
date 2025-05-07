package ro.mpp2024.DTO;

import java.io.Serial;
import java.io.Serializable;

public class OficiuDTO implements Serializable {
    String username;
    Long id;
    String password;

    @Serial
    private static final long serialVersionUID = 1L;

    public OficiuDTO(String username, Long id, String password) {
        this.username = username;
        this.id = id;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }


    public String getPassword() {
        return password;
    }

}
