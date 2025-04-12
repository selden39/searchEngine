package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import searchengine.dto.SearchPageDto1;
import searchengine.model.Page;
import searchengine.repositories.PageRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchServiceIml1 implements SearchService1{
    private final PageRepository pageRepository;

    //TODO подумать над переименованием Page -> PageEntity и проч
    //TODO подумать над переименованием  SearchPageDto1

    @Override
    public List<SearchPageDto1> search1 (String query, String searchSite, Integer offset, Integer limit){
        List<Page> pageList = pageRepository.findAll();
        pageList.forEach(page -> System.out.println(page.getPath()));

        Pageable paging = PageRequest.of(offset, limit, Sort.by("id"));
        org.springframework.data.domain.Page<Page> pagedResult = pageRepository.findAll(paging);

        List<Page> pagelistRed = pagedResult.getContent();
        pagelistRed.forEach(page -> System.out.println(page.getId() + " - " + page.getPath()));

        List<SearchPageDto1> searchPageDto1List1 = pagedResult.stream()
                .map(page -> pageToDto(page))
                .toList();
        if(pagedResult.hasContent()) {
            return searchPageDto1List1;
        } else {
            return new ArrayList<>();
        }
    }

    public static SearchPageDto1 pageToDto(Page page){
        SearchPageDto1 searchPageDto1 = new SearchPageDto1();
        searchPageDto1.setId(page.getId());
        searchPageDto1.setUrl(page.getPath());
        searchPageDto1.setSiteId(page.getSite().getId());
        return searchPageDto1;
    }

}
