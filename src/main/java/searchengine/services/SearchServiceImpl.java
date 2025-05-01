package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.SearchResponse;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.searchexecutor.LemmaEnriched;
import searchengine.services.searchexecutor.LemmaListCompiler;
import searchengine.utils.UrlHandler;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final String ERROR_DESC_THERE_IS_NO_DATA_FOR_SITE = "Для указанного сайта нет данных";

    @Override
    public SearchResponse search(String query, String searchSite, Integer offset, Integer limit) throws Exception{

        List<Site> searchSiteList = getSearchSiteList(searchSite);

// подготовить список лемм
// Сортировать леммы в порядке увеличения частоты встречаемости (по возрастанию значения поля frequency) — от самых редких до самых частых.
        LemmaListCompiler lemmaListCompiler = new LemmaListCompiler(query, searchSiteList, pageRepository);
        Set<LemmaEnriched> lemmaReducedCollection = lemmaListCompiler.compileLemmaCollection();




// алгоритм поиска страниц
//  По первой, самой редкой лемме из списка, находить все страницы, на которых она встречается. Далее искать соответствия следующей леммы из этого списка страниц, а затем повторять операцию по каждой следующей лемме.
        //TODO Обогащать TreeSet  <LemmaEnriched>
// Если в итоге не осталось ни одной страницы, то выводить пустой список

// рассчитывать по каждой из страниц релевантность
// Для каждой страницы рассчитывать абсолютную релевантность

// Сортировать страницы по убыванию релевантности (от большей к меньшей) и выдавать в виде списка объектов со следующими полями


        return new SearchResponse(true, query, searchSite, offset, limit);
    }

    private List<Site> getSearchSiteList(String searchSite) throws ServiceValidationException{
        List<Site> searchSiteList;
        if(searchSite != null && !searchSite.isEmpty()){
            System.out.println("один сайт");
            searchSiteList = siteRepository.findByUrl(UrlHandler.getPrettyRootUrl(searchSite));
        } else {
            System.out.println("Все сайты");
            searchSiteList = siteRepository.findAll();
        }
        if(searchSiteList.isEmpty()){
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_DATA_FOR_SITE);
        }
        searchSiteList.forEach(site -> System.out.println("==  " + site.getUrl()));
        return searchSiteList;
    }

}
