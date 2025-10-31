package searchengine.services.searchexecutor;

import java.util.Comparator;

public class PageEnrichedComparatorByRelevanceRel implements Comparator<PageEnriched> {
    @Override
    public int compare (PageEnriched pageEnriched1, PageEnriched pageEnriched2){
        int relevanceRelComparison = pageEnriched2.getRelevanceRel().compareTo(pageEnriched1.getRelevanceRel());
        if (relevanceRelComparison != 0) {
            return relevanceRelComparison;
        }

        int pathComparison = pageEnriched2.getPage().getPath().compareTo(pageEnriched1.getPage().getPath());
        return pathComparison;
    }
}
