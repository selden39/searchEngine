package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Map;

public interface PageRepository extends JpaRepository<Page, Integer> {
    List<Page> findByPath(String path);
    List<Page> findByPathAndSite(String path, Site site);
    List<Page> findBySiteIn(List<Site> siteList);
    @Transactional
    void deletePageByIdIn(List<Integer> idList);
    @Query(value = "SELECT * " +
            "FROM lemmas l " +
            "WHERE l.lemma in (:lemmaList)"
            , nativeQuery = true)
    List<String> getPagesNative(List<String> lemmaList);

    @Query(value = "SELECT l.lemma, count(i.page_id) " +
            "FROM lemmas l " +
            "LEFT JOIN indexes i on l.id = i.lemma_id " +
            "LEFT JOIN sites s on l.site_id = s.id " +
            "WHERE l.lemma in (:lemmaList) " +
            "AND s.id in (:siteIdList) " +
            "GROup by l.lemma"
            , nativeQuery = true)
    List<String> findLemmasCountByPage(List<Integer> siteIdList, List<String> lemmaList);
}
