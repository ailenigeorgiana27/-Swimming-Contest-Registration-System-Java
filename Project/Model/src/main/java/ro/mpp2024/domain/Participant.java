package ro.mpp2024.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name="Participant")
public class Participant extends Entity<Long> implements Serializable {
    @Column(nullable=false)
    private static final long serialVersionUID = 1L;


    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int age;

    public Participant() {}
    public Participant(Long id,String name, int age) {
        this.setId(id);
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