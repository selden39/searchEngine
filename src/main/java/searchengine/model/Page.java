package searchengine.model;

import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "Pages")
//@Table(name = "Pages", indexes = @Index(name = "path_indx", columnList = "path"))
//А точно, вспомнил, там вроде бы индекс в воркбенче появится позже, когда на 3ем этапе будешь делать сущности index и lemma
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name="site_id", insertable = false, updatable = false)
    private Site site;

    @NonNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String path;

    @NonNull
    @Column(nullable = false)
    private int code;

    @NonNull
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

}
