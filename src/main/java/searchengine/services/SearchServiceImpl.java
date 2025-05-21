package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.SearchResponse;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.searchexecutor.LemmaEnriched;
import searchengine.services.searchexecutor.LemmaListCompiler;
import searchengine.services.searchexecutor.PageEnriched;
import searchengine.utils.UrlHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final String ERROR_DESC_THERE_IS_NO_DATA_FOR_SITE = "Для указанного сайта нет данных или не выполнена индексация сайта";

    @Override
    public SearchResponse search(String query, String searchSite, Integer offset, Integer limit) throws Exception{

        List<Site> searchSiteList = getSearchSiteList(searchSite);

// подготовить список лемм
// Сортировать леммы в порядке увеличения частоты встречаемости (по возрастанию значения поля frequency) — от самых редких до самых частых.
        LemmaListCompiler lemmaListCompiler = new LemmaListCompiler(query, searchSiteList, pageRepository);
        Set<LemmaEnriched> lemmaReducedCollection = lemmaListCompiler.compileLemmaCollection();

// алгоритм поиска страниц
//  По первой, самой редкой лемме из списка, находить все страницы, на которых она встречается.
//  Далее искать соответствия следующей леммы из этого списка страниц, а затем повторять операцию по каждой следующей лемме.
/*
        // TODO переделать на формирование сразу pagesEnrichedWithWholeLemmas и заполнение списка лемм для каждой PageEnriched
        AtomicReference<Set<Page>> pagesWithWholeLemmas = new AtomicReference<>(new HashSet<>());
        AtomicBoolean isFirstFilling = new AtomicBoolean(true);

        lemmaReducedCollection.forEach(lemmaEnriched -> {
            lemmaEnriched.setPagesOfPresence(
                    pageRepository.findPagesListByLemmaAndSitelist(
                            lemmaEnriched.getLemma(),
                            searchSiteList.stream().map(site -> site.getId()).toList()
                    ));
            if (pagesWithWholeLemmas.get().isEmpty() && isFirstFilling.get()){
                pagesWithWholeLemmas.set(lemmaEnriched.getPagesOfPresence());
                isFirstFilling.set(false);
            } else {
                pagesWithWholeLemmas.get().retainAll(lemmaEnriched.getPagesOfPresence());
            }
            System.out.println(lemmaEnriched.getFrequency() + " - " + lemmaEnriched.getLemma());
            lemmaEnriched.getPagesOfPresence().forEach(p -> System.out.print(p.getId() + " + " + p.getPath() + " |  "));
            System.out.println();
            pagesWithWholeLemmas.get().forEach(p -> System.out.print(p.getId() + " + " + p.getPath() + " |  "));
            System.out.println();
        });
*/
        System.out.println("=== pagesEnrichedWithWholeLemmas");
        AtomicReference<Set<PageEnriched>> pagesEnrichedWithWholeLemmas = new AtomicReference<>(new HashSet<>());
        AtomicBoolean isFirstFillingE = new AtomicBoolean(true);
    // для каждой Enriched леммы
        lemmaReducedCollection.forEach(lemmaEnriched -> {
    // получаем из репо список страниц где она присутствует
            // TODO вот тут нужно добавить lemma_rank, получим Map Page - lemma_rank
            Set<Page> pageOfPresenceList = pageRepository.findPagesListByLemmaAndSitelist(
                    lemmaEnriched.getLemma(),
                    searchSiteList.stream().map(site -> site.getId()).toList()
            );
    // для каждой страницы создаем Enriched страницу
/*
            Set<PageEnriched> pageEnrichedOfPresenceList = pageOfPresenceList.stream()
                    .map(pageOfPresence -> {
                        PageEnriched pageEnriched = new PageEnriched(pageOfPresence);
                        return pageEnriched;
                    })
                    .collect(Collectors.toSet());

            Map<PageEnriched, Integer> pageEnrichedOfPresenceListWithLemmaRank_old = new HashMap<>();
            pageEnrichedOfPresenceList.forEach(pageEnriched -> {
                int lemmaRank = pageRepository.findRankByPageAndLemma(
                        pageEnriched.getPage().getId(),
                        lemmaEnriched.getLemma())
                        .get(0);
                pageEnrichedOfPresenceListWithLemmaRank_old.put(pageEnriched, lemmaRank);
            });
*/
            Map<PageEnriched, Integer> pageEnrichedOfPresenceListWithLemmaRank= new HashMap<>();
            pageOfPresenceList.stream()
                    .map(pageOfPresence -> {
                        PageEnriched pageEnriched = new PageEnriched(pageOfPresence);
                        return pageEnriched;
                    })
                    .forEach(pageEnriched -> {
                        int lemmaRank = pageRepository.findRankByPageAndLemma(
                                pageEnriched.getPage().getId(),
                                lemmaEnriched.getLemma()
                        ).get(0);
                        pageEnrichedOfPresenceListWithLemmaRank.put(pageEnriched, lemmaRank);
                    });

    // список Enriched страниц добавляем в Enriched лемму
            lemmaEnriched.setPagesEnrichedOfPresenceWithLemmaRank(pageEnrichedOfPresenceListWithLemmaRank);
    // заполняем список Enriched страниц, на которых есть лемма
            if (pagesEnrichedWithWholeLemmas.get().isEmpty() && isFirstFillingE.get()){
//                pagesEnrichedWithWholeLemmas.set(lemmaEnriched.getPagesEnrichedOfPresence().stream().collect(Collectors.toSet()));
                pagesEnrichedWithWholeLemmas.set(lemmaEnriched.getPagesEnrichedOfPresenceWithLemmaRank().keySet());
                isFirstFillingE.set(false);
            } else {
//                pagesEnrichedWithWholeLemmas.get().retainAll(lemmaEnriched.getPagesEnrichedOfPresence());
                pagesEnrichedWithWholeLemmas.get().retainAll(lemmaEnriched.getPagesEnrichedOfPresenceWithLemmaRank().keySet());
            }
    // для каждой Enriched страницы добавляем текущую Enriched лемму в lemmaEnrichedList
            // на выходе получаем список Enriched страницы со списком Enriched лемм в нем
            pagesEnrichedWithWholeLemmas.get().forEach(pageEnriched -> {
                pageEnriched.addToLemmaEnrichList(lemmaEnriched);
            });
    // отладочная информация
            System.out.println(lemmaEnriched.getFrequency() + " - " + lemmaEnriched.getLemma());
            lemmaEnriched.getPagesEnrichedOfPresenceWithLemmaRank()
                    .forEach((k,v) -> System.out.print(k.getPage().getId()  + " + " + k.getPage().getPath() + " |  "));
            System.out.println();
            pagesEnrichedWithWholeLemmas.get().forEach(pe -> System.out.print(pe.getPage().getId() + " + " + pe.getPage().getPath() + " |  "));
            System.out.println();
            System.out.print("== le: ");
            pagesEnrichedWithWholeLemmas.get().forEach(pe -> {
                System.out.println();
                System.out.println(pe.getPage().getPath());
                pe.getLemmaEnrichedSet().forEach(le -> {
                    System.out.print(" " + le.getLemma());
                });
            });
            System.out.println();

        });


// Если в итоге не осталось ни одной страницы, то выводить пустой список
        //TODO вернуться после формирования ответа
        if(pagesEnrichedWithWholeLemmas.get().isEmpty()){
            System.out.println("=== список страниц пустой");
            return null;
        }

// рассчитывать по каждой из страниц релевантность
// Для каждой страницы рассчитывать абсолютную релевантность
        // просто получили список обогащенных страниц
        List<PageEnriched> pagesEnrichedWithWholeLemmasOld = pagesEnrichedWithWholeLemmas.get().stream()
                .map(page -> new PageEnriched(page.getPage()))
                .collect(Collectors.toList());
        // добавляем релевантность
        pagesEnrichedWithWholeLemmasOld.forEach(pe -> {

        });

// Сортировать страницы по убыванию релевантности (от большей к меньшей) и выдавать в виде списка объектов со следующими полями


        return new SearchResponse(true, query, searchSite, offset, limit);
    }

    private List<Site> getSearchSiteList(String searchSite) throws ServiceValidationException{
        List<Site> searchSiteList;
        if(searchSite != null && !searchSite.isEmpty()){
            System.out.println("=== один сайт");
            searchSiteList = siteRepository.findByUrlAndStatus(UrlHandler.getPrettyRootUrl(searchSite), Status.INDEXED);
        } else {
            System.out.println("=== Все сайты");
            searchSiteList = siteRepository.findByStatus(Status.INDEXED);
        }
        if(searchSiteList.isEmpty()){
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_DATA_FOR_SITE);
        }
        searchSiteList.forEach(site -> System.out.println("==  " + site.getUrl()));
        return searchSiteList;
    }

    private List<PageEnriched> fillRankPageEnriched (){

        return null;
    }

}
