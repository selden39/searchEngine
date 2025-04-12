package searchengine.services;

import searchengine.dto.SearchPageDto1;

import java.util.List;

public interface SearchService1 {
    List<SearchPageDto1> search1(String query, String searchSite, Integer Offset, Integer limit);
}
