package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import searchengine.repositories.PageRepository;
@RequiredArgsConstructor
@Service
public class SearchServiceIml1 implements SearchService1{
    private final PageRepository pageRepository;

    @Override
    public Page<searchengine.model.Page> search1(String query, String searchSite, Integer offset, Integer limit){
        Page<searchengine.model.Page> limitPages = pageRepository.findAll(PageRequest.of(offset, limit));
        return limitPages;
    }

}
