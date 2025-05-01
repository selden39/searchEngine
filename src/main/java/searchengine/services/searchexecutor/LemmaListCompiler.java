package searchengine.services.searchexecutor;

import lombok.AllArgsConstructor;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;
import searchengine.services.ServiceValidationException;
import searchengine.services.lemmatization.Lemmatizer;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LemmaListCompiler {
    private String query;
    private List<Site> searchSiteList;
    private final PageRepository pageRepository;
    private final Double LIMIT_OF_FREQUENCY = 0.75;
    private final String ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY = "Некорректный параметр query. В полученном запросе отсутствуют леммы";
    private final String ERROR_DESC_LEMMATIZATION_ERROR = "Возникла проблема с формированием списка лемм";

    public Set<LemmaEnriched> compileLemmaCollection() throws ServiceValidationException{
        Lemmatizer lemmatizer;
        HashMap<String, Integer> lemmaMap;
        try {
            lemmatizer = new Lemmatizer();
            lemmaMap = lemmatizer.getLemmasFromText(query);
        } catch (Exception e) {
            throw new ServiceValidationException(false, ERROR_DESC_LEMMATIZATION_ERROR);
        }

        if (lemmaMap.isEmpty()) {
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY);
        }

        List<String> lemmaList = lemmaMap.entrySet().stream()
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        Set<LemmaEnriched> lemmaReducedCollection = prepareReducedLemmaCollection(lemmaList, searchSiteList);

        return lemmaReducedCollection;
    }

    private Set<LemmaEnriched> prepareReducedLemmaCollection(List<String> lemmaList, List<Site> siteList) {
        List<Page> pageList = pageRepository.findBySiteIn(siteList);

        TreeSet<LemmaEnriched> lemmaEnrichedSet = new TreeSet<>(Comparator.comparing(LemmaEnriched::getFrequency)
                .thenComparing(LemmaEnriched::getLemma));
        pageRepository.findLemmasCountByPage(
                        siteList.stream().map(site -> site.getId()).toList(),
                        lemmaList)
                .forEach(str -> {
                    String queryLemma = str.substring(0, str.indexOf(','));
                    Integer queryCount = Integer.valueOf(str.substring(str.indexOf(',') + 1));
                    Double frequency = queryCount / (double) pageList.size();
                    if(frequency < LIMIT_OF_FREQUENCY) {
                        lemmaEnrichedSet.add(new LemmaEnriched(queryLemma, queryCount, frequency));
                    }
                });
        return lemmaEnrichedSet;
    }
}
