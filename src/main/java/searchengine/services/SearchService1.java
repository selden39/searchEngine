package searchengine.services;

import org.springframework.data.domain.Page;
import searchengine.dto.SearchResponseDto1;

import java.util.List;

public interface SearchService1 {
    Page<searchengine.model.Page> search1(String query, String searchSite, Integer Offset, Integer limit);
}
