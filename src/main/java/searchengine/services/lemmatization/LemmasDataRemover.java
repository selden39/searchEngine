package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class LemmasDataRemover {
    private List<Page> pageList;
    private Site site;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    public void removeLemmasData() throws Exception{
        Collection<Index> indexListForPage = indexRepository.findIndexListByPageList(pageList);
        if(!indexListForPage.isEmpty()) {
            indexRepository.deleteIndexListByPageList(pageList);
            updateLemmasFrequency(indexListForPage);
        }
    }

    private void updateLemmasFrequency(Collection<Index> indexListForPage){
        List<Lemma> lemmasBySite = lemmaRepository.findBySite(site);
        lemmasBySite.forEach(lemma -> {
            indexListForPage.forEach(index -> {
                if(lemma.equals(index.getLemma())){
                    int currentFrequency = lemma.getFrequency();
                    lemma.setFrequency(currentFrequency - index.getRank());
                    // TODO по хорошему нужно бы удалять те леммы, у которых Frequency получили ноль
                    // сейчас они остаются в БД со значением Frequency=0
                    lemmaRepository.save(lemma);
                }
            });
        });
    }
}
