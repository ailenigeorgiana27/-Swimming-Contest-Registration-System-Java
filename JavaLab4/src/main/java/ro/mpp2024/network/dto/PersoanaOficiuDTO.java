package ro.mpp2024.network.dto;

import ro.mpp2024.domain.PersoanaOficiu;

public class PersoanaOficiuDTO extends EntityDTO{
    private final String username;
    private final String password;

    public PersoanaOficiuDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public static PersoanaOficiuDTO fromPersoanaOficiu(PersoanaOficiu persoanaOficiu) {
        var a = new PersoanaOficiuDTO(persoanaOficiu.getUsername(), persoanaOficiu.getPassword());
        a.setId(persoanaOficiu.getId());
        return a;
    }

    public PersoanaOficiu toPersoanaOficiu() {
        var a = new PersoanaOficiu(getUsername(), getPassword());
        a.setId(getId());
        return a;
    }
}
