package searchengine.services;

import org.springframework.stereotype.Service;
import searchengine.dto.SearchResponse;
import searchengine.services.lemmatization.Lemmatizer;

import java.util.HashMap;

@Service
public class SearchServiceImpl implements SearchService{

    private final String ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY = "Некорректный параметр query. В полученном запросе отсутствуют леммы";
    @Override
    public SearchResponse search(String query, String searchSite, Integer offset, Integer limit) throws Exception{

//Разбивать поисковый запрос на отдельные слова
//формировать из этих слов список уникальных лемм, исключая междометия, союзы, предлоги и частицы.
        Lemmatizer lemmatizer = new Lemmatizer();
        HashMap<String, Integer> lemmasList = lemmatizer.getLemmasFromText(query);
        lemmatizer.getLemmasFromText(query).forEach((lemma, count) -> System.out.println(lemma + " - " + count));
// проверить квери данные - если нет ни одной леммы???
        if (lemmasList.isEmpty()) {
            throw new ServiceValidationException(400, false, ERROR_DESC_THERE_IS_NO_LEMMAS_IN_QUERY);
        }

// Исключать из полученного списка леммы, которые встречаются на слишком большом количестве страниц

// Сортировать леммы в порядке увеличения частоты встречаемости (по возрастанию значения поля frequency) — от самых редких до самых частых.

// алгоритм поиска страниц
//  По первой, самой редкой лемме из списка, находить все страницы, на которых она встречается. Далее искать соответствия следующей леммы из этого списка страниц, а затем повторять операцию по каждой следующей лемме.
// Если в итоге не осталось ни одной страницы, то выводить пустой список

// рассчитывать по каждой из страниц релевантность
// Для каждой страницы рассчитывать абсолютную релевантность

// Сортировать страницы по убыванию релевантности (от большей к меньшей) и выдавать в виде списка объектов со следующими полями


        return new SearchResponse(true, query, searchSite, offset, limit);
    }
}
