package searchengine.services;

import searchengine.dto.OperationIndexingResponse;

public interface StartIndexingService {
    OperationIndexingResponse getStartIndexing() throws Exception;
}
