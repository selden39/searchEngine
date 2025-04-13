package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class LemmasDataSaver {
    private Site site;
    private Page page;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    public void saveLemmasData() throws Exception {
        Lemmatizer lemmatizer = new Lemmatizer();
        HashMap<String, Integer> lemmasFromPage = lemmatizer.getLemmasFromHtml(page.getContent());
        lemmasFromPage.forEach((lemmaFromPage,count) -> {
            List<Lemma> existingLemmas = lemmaRepository.findByLemmaAndSite(lemmaFromPage, site);
            if (existingLemmas.isEmpty()) {
                Lemma lemma = addNewLemma(lemmaFromPage, count);
                addIndex(lemma, count);
            } else {
                int currentFrequency = existingLemmas.get(0).getFrequency();
                existingLemmas.get(0).setFrequency(currentFrequency + count);
                lemmaRepository.save(existingLemmas.get(0));
                addIndex(existingLemmas.get(0), count);
            }
        });
    }

    private Lemma addNewLemma(String lemmaFromPage, int count){
        Lemma lemma = new Lemma();
        lemma.setLemma(lemmaFromPage);
        lemma.setFrequency(count);
        lemma.setSite(site);
        lemmaRepository.save(lemma);
        return lemma;
    }

    private void addIndex(Lemma lemma, int count){
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(count);
        indexRepository.save(index);
    }
}
