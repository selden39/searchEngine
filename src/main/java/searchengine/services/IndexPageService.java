package searchengine.services;

import searchengine.dto.IndexPage;
import searchengine.dto.OperationIndexingResponse;

public interface IndexPageService {
    OperationIndexingResponse postIndexPage(IndexPage indexPage) throws ServiceValidationException;
}
