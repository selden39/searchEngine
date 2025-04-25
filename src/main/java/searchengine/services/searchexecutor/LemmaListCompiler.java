package searchengine.services.searchexecutor;

import lombok.AllArgsConstructor;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;
import searchengine.services.ServiceValidationException;
import searchengine.services.lemmatization.Lemmatizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LemmaListCompiler {
    private String query;
    private List<Site> searchSiteList;
    private final PageRepository pageRepository;
    private final Double LIMIT_OF_FREQUENCY = 0.85;
    private final String ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY = "Некорректный параметр query. В полученном запросе отсутствуют леммы";
    private final String ERROR_DESC_LEMMATIZATION_ERROR = "Возникла проблема с формированием списка лемм";

    public List<String> getLemmaList() throws ServiceValidationException{
        //Разбивать поисковый запрос на отдельные слова
        //формировать из этих слов список уникальных лемм, исключая междометия, союзы, предлоги и частицы.
        Lemmatizer lemmatizer;
        HashMap<String, Integer> lemmaMap;
        try {
            lemmatizer = new Lemmatizer();
            lemmaMap = lemmatizer.getLemmasFromText(query);
        } catch (Exception e) {
            throw new ServiceValidationException(false, ERROR_DESC_LEMMATIZATION_ERROR);
        }
        System.out.println("=== print lemma map");
        lemmatizer.getLemmasFromText(query).forEach((lemma, count) -> System.out.println(lemma + " - " + count));

        // проверить квери данные - если нет ни одной леммы, то ошибка
        if (lemmaMap.isEmpty()) {
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY);
        }

        // Исключать из полученного списка леммы, которые встречаются на слишком большом количестве страниц
        List<String> lemmaList = lemmaMap.entrySet().stream()
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        List<String> lemmaReducedList = getReducedLemmaList(lemmaList, searchSiteList);

        return null;
    }

    private List<String> getReducedLemmaList (List<String> lemmaList, List<Site> siteList) {
        List<Page> pageList = pageRepository.findBySiteIn(siteList);

        List<String> lemmaReducedList = new ArrayList<>();
        pageRepository.findLemmasCountByPage(
                    siteList.stream().map(site -> site.getId()).toList(),
                    lemmaList)
                .forEach(str -> {
                    String queryLemma = str.substring(0, str.indexOf(','));
                    Integer queryCount = Integer.valueOf(str.substring(str.indexOf(',') + 1));
                    Double frequency = queryCount / (double) pageList.size();
                    if(frequency < LIMIT_OF_FREQUENCY){
                        lemmaReducedList.add(queryLemma);
                    }
                });

        return lemmaReducedList;
    }
}
