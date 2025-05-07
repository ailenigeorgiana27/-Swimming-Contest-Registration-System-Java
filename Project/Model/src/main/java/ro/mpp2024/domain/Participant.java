package ro.mpp2024.domain;

import java.io.Serializable;
import java.util.Objects;

public class Participant extends Entity<Long> implements Serializable {
    private String name;
    private int age;
    private int nrProbe;


    public Participant(Long id,String name, int age) {
        super(id);
        this.name = name;
        this.age = age;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getNrProbe() {
        return nrProbe;
    }
    public void setNrProbe(int nrProbe) {
        this.nrProbe = nrProbe;
    }
    @Override
    public String toString() {
        return "Participant{id=" + getId() + ", name=" + name + ", age=" + age + "}";
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(this.age, that.age) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
