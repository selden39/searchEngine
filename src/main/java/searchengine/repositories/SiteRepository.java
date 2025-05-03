package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Site;
import searchengine.model.Status;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site,Integer> {
    List<Site> findByStatus(Status status);
    List<Site> findByUrl(String url);
    List<Site> findByUrlAndStatus(String url, Status status);
    List<Site> findByUrlIn(List<String> urls);
    @Transactional
    void deleteSiteByIdIn(List<Integer> idList);
}
