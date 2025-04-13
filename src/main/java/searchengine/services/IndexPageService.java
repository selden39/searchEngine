package searchengine.services;

import searchengine.dto.IndexPageRequest;
import searchengine.dto.OperationIndexingResponse;

public interface IndexPageService {
    OperationIndexingResponse postIndexPage(IndexPageRequest indexPageRequest) throws ServiceValidationException;
}
