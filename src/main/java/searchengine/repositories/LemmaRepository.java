package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Lemma;
import searchengine.model.Site;

import java.util.List;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    List<Lemma> findByLemmaAndSite(String lemma, Site site);
    List<Lemma> findBySite(Site site);
    @Transactional
    void deleteLemmaBySiteIn(List<Site> sites);
}
