package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import searchengine.model.Index;
import searchengine.model.Page;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class LemmasDataRemover {
    private List<Page> pageList;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    public void removeLemmasData(){
        System.out.println("======== getIndexList");
        Collection<Index> indexList = indexRepository.findIndexListByPageList(pageList);
        pageList.forEach(page -> System.out.println("pageList: " + page.getId() + " - " + page.getPath()));
        indexList.forEach(index -> System.out.println("indexList: " + index.getId() + " - " + index.getLemma().getId()));

        System.out.println("======== deleteIndexList");
        indexRepository.deleteIndexListByPageList(pageList);
        System.out.println("======== stopDeleteIndexList");

        System.out.println("======== deleteIndexList");


        // TODO не забыть обернуть в трай кетч получение данных
        /*
        HashMap<Integer, Integer> lemmasRank = getLemmasRank();
        lemmasRank.forEach((lemma_id, rank) -> {
            System.out.println("id: " + lemma_id + " rank: " + rank);
        });

         */
      //  селект все лемма_ид и лемма_ранк из Индекс
      //  делит все записи в Индекс по Пейдж
      //  чендж количества лемм в Леммы
    }
/*
    private HashMap<Integer, Integer> getLemmasRank(){
        return indexRepository.lemmasRank(pageList.stream()
                .map(page -> page.getId())
                .toList()
        );
    }


 */
}
