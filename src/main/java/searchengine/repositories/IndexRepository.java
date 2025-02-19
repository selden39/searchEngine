package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Index;
import searchengine.model.Page;

import java.util.Collection;
import java.util.List;

public interface IndexRepository extends JpaRepository<Index, Integer> {
    @Query("select i from Index i where i.page in (:pages)")
    Collection<Index> findIndexListByPageList(@Param("pages") List<Page> pageList);

    @Modifying
    @Transactional
    @Query("delete from Index i where i.page in (:pages)")
    void deleteIndexListByPageList(@Param("pages") List<Page> pageList);
}
