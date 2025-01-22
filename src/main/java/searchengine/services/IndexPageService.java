package searchengine.services;

import searchengine.dto.IndexPage;
import searchengine.dto.statistics.OperationIndexingResponse;

public interface IndexPageService {
    OperationIndexingResponse postIndexPage(IndexPage indexPage);
}
