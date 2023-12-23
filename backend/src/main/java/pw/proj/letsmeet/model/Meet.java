package pw.proj.letsmeet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Meet extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    // **********************************************************************
    // POLA

    @Column
    private String name;

    @Column
    private LocalDate date;

    @Column
    private String time;

    @OneToOne
    private User creator;

    @ManyToMany
    private Set<User> participants;
}
