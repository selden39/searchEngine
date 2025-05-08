package searchengine.services.searchexecutor;

import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PageEnriched {
    private final Page page;
    private String title;
    private String snippet;
    private Double relevance;
    private List<LemmaEnriched> lemmaEnrichedList;

    public PageEnriched(Page page){
        this.page = page;
        lemmaEnrichedList = new ArrayList<>();
    }

    public void addToLemmaEnrichList(LemmaEnriched lemmaEnriched){
        lemmaEnrichedList.add(lemmaEnriched);
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
