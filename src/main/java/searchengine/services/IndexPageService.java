package searchengine.services;

import org.springframework.data.jpa.repository.Query;
import searchengine.dto.IndexPageRequest;
import searchengine.dto.OperationIndexingResponse;
import searchengine.model.Site;

import java.util.List;

public interface IndexPageService {
    OperationIndexingResponse postIndexPage(IndexPageRequest indexPageRequest) throws ServiceValidationException;
}
