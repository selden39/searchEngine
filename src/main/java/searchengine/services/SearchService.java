package searchengine.services;

import searchengine.dto.SearchResponse;

public interface SearchService {

    SearchResponse search(String query, String searchSite, Integer Offset, Integer limit);

}
