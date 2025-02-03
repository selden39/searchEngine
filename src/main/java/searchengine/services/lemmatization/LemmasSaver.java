package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.LemmaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LemmasSaver {
    Site site;
    Page page;
    private final LemmaRepository lemmaRepository;

    public void saveLemmas() throws Exception {
        Lemmatizer lemmatizer = new Lemmatizer();
        HashMap<String, Integer> lemmasFromPage = lemmatizer.getLemmasFromText(page.getContent());
        lemmasFromPage.forEach((lemma,count) -> {
            List<Lemma> existingLemmas = lemmaRepository.findByLemmaAndSite(lemma, site);
            if (existingLemmas.isEmpty()) {
                // добавляем лемму
            } else {
                // добавляем count
            }


        });
    }

}
