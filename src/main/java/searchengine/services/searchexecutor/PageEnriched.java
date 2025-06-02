package searchengine.services.searchexecutor;

import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;

import java.util.*;

@Getter
@Setter
public class PageEnriched {
    private final Page page;
    private String title;
    private String snippet;
    private Double relevanceAbs;
    private Double relevanceRel;
    private Set<LemmaEnriched> lemmaEnrichedSet;

    public PageEnriched(Page page){
        this.page = page;
        lemmaEnrichedSet = new HashSet<>();
        relevanceAbs = 0.0;
    }

    public void addToLemmaEnrichList(LemmaEnriched lemmaEnriched){
        lemmaEnrichedSet.add(lemmaEnriched);
    }

    public void increaseRelevanceAbs(Double rank){
        relevanceAbs = relevanceAbs + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageEnriched that = (PageEnriched) o;
        return Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page);
    }
}
