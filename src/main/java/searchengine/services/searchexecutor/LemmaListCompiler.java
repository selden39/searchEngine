package searchengine.services.searchexecutor;

import lombok.AllArgsConstructor;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;
import searchengine.services.ServiceValidationException;
import searchengine.services.lemmatization.BasicLemma;
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
        HashMap<BasicLemma, Integer> lemmaMap;
    // TODO где то тут нужно сохранить исходную форму слова в леммуЕнрич
        //  и по этой исходной форме искать на странице
        // будет ли это список или только одна исходная форма ?????
        try {
            lemmatizer = new Lemmatizer();
            lemmaMap = lemmatizer.getLemmasFromText(query);
        } catch (Exception e) {
            throw new ServiceValidationException(false, ERROR_DESC_LEMMATIZATION_ERROR);
        }

        if (lemmaMap.isEmpty()) {
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY);
        }

        List<BasicLemma> lemmaList = lemmaMap.entrySet().stream()
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        Set<LemmaEnriched> lemmaReducedCollection = prepareReducedLemmaCollection(lemmaList, searchSiteList);

        return lemmaReducedCollection;
    }

    private Set<LemmaEnriched> prepareReducedLemmaCollection(List<BasicLemma> lemmaList, List<Site> siteList) {
        List<Page> pageList = pageRepository.findBySiteIn(siteList);

        TreeSet<LemmaEnriched> lemmaEnrichedSet = new TreeSet<>(Comparator.comparing(LemmaEnriched::getFrequency)
                .thenComparing(Comparator.comparing(le -> le.getBasicLemma().getNormalWord())));
        pageRepository.findLemmasCountByPage(
                        siteList.stream().map(site -> site.getId()).toList(),
                        lemmaList.stream().map(basicLemma -> basicLemma.getNormalWord()).toList())
                .forEach(str -> {
                    String queryLemma = str.substring(0, str.indexOf(','));
                    Integer queryCount = Integer.valueOf(str.substring(str.indexOf(',') + 1));
                    Double frequency = queryCount / (double) pageList.size();
                    if(frequency < LIMIT_OF_FREQUENCY) {
                        addLemmaEnrichedToSet(lemmaList, lemmaEnrichedSet, queryLemma, queryCount, frequency);
                    }
                });
        return lemmaEnrichedSet;
    }

    private void addLemmaEnrichedToSet(List<BasicLemma> lemmaList, TreeSet<LemmaEnriched> lemmaEnrichedSet,
                                       String queryLemma, Integer queryCount, Double frequency){
        lemmaList.forEach(basicLemma -> {
            if (basicLemma.getNormalWord().equals(queryLemma)) {
                lemmaEnrichedSet.add(new LemmaEnriched(basicLemma, queryCount, frequency));
            }
        });
    }
}
