package searchengine.services.searchexecutor;

import java.util.Comparator;

public class PageEnrichedComparatorByRelevanceAbs implements Comparator<PageEnriched> {
    @Override
    public int compare (PageEnriched pageEnriched1, PageEnriched pageEnriched2){
        return pageEnriched1.getRelevanceAbs().compareTo(pageEnriched2.getRelevanceAbs());
    }
}
