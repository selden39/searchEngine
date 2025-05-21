package searchengine.services.searchexecutor;

import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class LemmaEnriched {
    private final String lemma;
    private Integer pageCount;
    private Double frequency;
    private Set<Page> pagesOfPresence;
/*
    private Set<PageEnriched> pagesEnrichedOfPresence;
    private Map<PageEnriched, Integer> pagesEnrichedOfPresenceWithLemmaRank_old;
 */
    private Map<PageEnriched, Integer> pagesEnrichedOfPresenceWithLemmaRank;

    public LemmaEnriched(String lemma, Double frequency){
        this.lemma = lemma;
        this.frequency = frequency;
    }

    public LemmaEnriched(String lemma, Integer pageCount,  Double frequency ){
        this.lemma = lemma;
        this.pageCount = pageCount;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemmaEnriched that = (LemmaEnriched) o;
        return Objects.equals(lemma, that.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lemma);
    }
}
