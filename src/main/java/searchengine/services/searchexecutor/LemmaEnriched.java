package searchengine.services.searchexecutor;

import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class LemmaEnriched {
    private String lemma;
    private Integer pageCount;
    private Double frequency;
    private List<Page> pagesOfPresence;

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
