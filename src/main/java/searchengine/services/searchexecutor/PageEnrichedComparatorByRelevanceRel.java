package searchengine.services.searchexecutor;

import java.util.Comparator;

public class PageEnrichedComparatorByRelevanceRel implements Comparator<PageEnriched> {
    @Override
    public int compare (PageEnriched pageEnriched1, PageEnriched pageEnriched2){
        return pageEnriched2.getRelevanceRel().compareTo(pageEnriched1.getRelevanceRel());
    }
}
