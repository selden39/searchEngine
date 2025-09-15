package searchengine.services.searchexecutor;

import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;
import searchengine.services.lemmatization.BasicLemma;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class LemmaEnriched {
    private BasicLemma basicLemma;
    private Integer pageCount;
    private Double frequency;
    private Set<Page> pagesOfPresence;
/*
    private Set<PageEnriched> pagesEnrichedOfPresence;
    private Map<PageEnriched, Integer> pagesEnrichedOfPresenceWithLemmaRank_old;
 */
    private Map<PageEnriched, Double> pagesEnrichedOfPresenceWithLemmaRank;

    public LemmaEnriched(BasicLemma basicLemma, Integer pageCount,  Double frequency) {
        this.basicLemma = basicLemma;
        this.pageCount = pageCount;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemmaEnriched that = (LemmaEnriched) o;
        return Objects.equals(basicLemma, that.basicLemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basicLemma);
    }
}
