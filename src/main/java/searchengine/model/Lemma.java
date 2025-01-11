package searchengine.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Lemmas")
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn (name = "site_id")
    private Site site;

    @NonNull
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String lemma;

    @NonNull
    @Column(nullable = false)
    private int frequency;
}
