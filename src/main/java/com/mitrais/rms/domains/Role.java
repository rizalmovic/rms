package com.mitrais.rms.domains;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Data
@Table(name="roles")
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = -2939883711631583594L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    private String name;

    public void setName(String name) {
        this.name = name;
        this.canonical = name.toLowerCase().replace(" ", "_");
    }

    @Setter(AccessLevel.NONE)
    private String canonical;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<User>();
}