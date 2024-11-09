package searchengine.model;

import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(nullable = false)
    private Status status;

    @NonNull
    @Column(nullable = false, name = "status_time")
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @NonNull
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;

    @NonNull
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @OneToMany(mappedBy = "site",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Page> pages = new ArrayList<>();

}
