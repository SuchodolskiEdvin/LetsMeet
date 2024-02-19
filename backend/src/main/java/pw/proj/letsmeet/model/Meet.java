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
    private String timeStart;

    @Column
    private String duration;

    @OneToOne
    private User creator;

    @Column
    private Boolean isOnline;

    @Column
    private String zoomUrlJoinLink;

    @Column
    private Long zoomId;

    @ManyToMany
    private Set<User> participants;
}
