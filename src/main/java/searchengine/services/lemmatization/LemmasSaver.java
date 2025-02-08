package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.LemmaRepository;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class LemmasSaver {
    Site site;
    Page page;
    private final LemmaRepository lemmaRepository;

    public void saveLemmas() throws Exception {
        Lemmatizer lemmatizer = new Lemmatizer();
        HashMap<String, Integer> lemmasFromPage = lemmatizer.getLemmasFromText(page.getContent());
        lemmasFromPage.forEach((lemmaFromPage,count) -> {
            List<Lemma> existingLemmas = lemmaRepository.findByLemmaAndSite(lemmaFromPage, site);
            if (existingLemmas.isEmpty()) {
                Lemma lemma = new Lemma();
                lemma.setLemma(lemmaFromPage);
                lemma.setFrequency(count);
                lemma.setSite(site);
                lemmaRepository.save(lemma);
            } else {
                int currentFrequency = existingLemmas.get(0).getFrequency();
                existingLemmas.get(0).setFrequency(currentFrequency + count);
                lemmaRepository.save(existingLemmas.get(0));
            }
        });
    }
}
