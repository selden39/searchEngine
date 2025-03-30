package searchengine.services;

import org.springframework.stereotype.Service;
import searchengine.dto.SearchResponse;

@Service
public class SearchServiceImpl implements SearchService{
    @Override
    public SearchResponse search(String query, String searchSite, Integer offset, Integer limit){
        return new SearchResponse(true, query, searchSite, offset, limit);
    }
}
