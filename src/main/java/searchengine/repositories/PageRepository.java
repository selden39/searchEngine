package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer> {
    List<Page> findByPath(String path);
    List<Page> findByPathAndSite(String path, Site site);
    List<Page> findBySiteIn(List<Site> siteList);
    @Transactional
    void deletePageByIdIn(List<Integer> idList);
}
