package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PageRepository extends JpaRepository<Page, Integer> {
    List<Page> findByPath(String path);

    List<Page> findByPathAndSite(String path, Site site);

    List<Page> findBySiteIn(List<Site> siteList);

    @Transactional
    void deletePageByIdIn(List<Integer> idList);

    @Query(value = "SELECT l.lemma, count(i.page_id) " +
            "FROM lemmas l " +
            "LEFT JOIN indexes i on l.id = i.lemma_id " +
            "LEFT JOIN sites s on l.site_id = s.id " +
            "WHERE l.lemma in (:lemmaList) " +
            "AND s.id in (:siteIdList) " +
            "Group by l.lemma"
            , nativeQuery = true)
    List<String> findLemmasCountByPage(List<Integer> siteIdList, List<String> lemmaList);
    @Query(value =
            "SELECT p.* " +
                    "FROM search_engine.lemmas l " +
                    "JOIN search_engine.sites s ON l.site_id = s.id " +
                    "JOIN search_engine.indexes i ON i.lemma_id = l.id " +
                    "JOIN search_engine.pages p ON i.page_id = p.id " +
                    "WHERE s.id IN (:siteIdList) " +
                    "AND l.lemma IN (:lemma)"
            , nativeQuery = true
    )
    Set<Page> findPagesListByLemmaAndSitelist(String lemma, List<Integer> siteIdList);

    @Query(value =
            "SELECT i.lemma_rank " +
                    "FROM search_engine.indexes i " +
                    "JOIN search_engine.pages p ON i.page_id = p.id " +
                    "JOIN search_engine.lemmas l ON i.lemma_id = l.id " +
                    "WHERE p.id = :pageId " +
                    "AND l.lemma = :lemma"
            , nativeQuery = true
    )
    List<Integer> findRankByPageAndLemma(Integer pageId, String lemma);
}
