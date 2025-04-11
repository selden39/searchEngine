package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.OperationIndexingResponse;
import searchengine.model.Status;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexingexecutor.ThreadCollector;
import searchengine.services.stopindexingexecutor.LastErrorMessage;


@RequiredArgsConstructor
@Service
public class StopIndexingServiceImpl implements StopIndexingService{
    private final String UNEXPECTED_ERROR_DESC = "Непредвиденная ошибка";
    private final String INDEXING_NOT_RUN_ERROR_DESC = "Индексация не запущена";
    private final String STOP_INDEXING_MESSAGE = "Индексация остановлена пользователем";
    private final SiteRepository siteRepository;

    @Override
    public OperationIndexingResponse getStopIndexing() throws ServiceValidationException{
        OperationIndexingResponse operationIndexingResponse;
        if (siteRepository.findByStatus(Status.INDEXING).isEmpty()){
            throw new ServiceValidationException(406, false, INDEXING_NOT_RUN_ERROR_DESC);
        } else {
            try {
                ThreadCollector.getIndexingThreads().forEach((thread, forkJoinPool) -> {
                    forkJoinPool.shutdownNow();
                });
                LastErrorMessage.setLastErrorMessage(STOP_INDEXING_MESSAGE);
                // TODO перевести статус site в FAILED и записать текст ошибки «Индексация остановлена пользователем».
                operationIndexingResponse = new OperationIndexingResponse(true);
            } catch (Exception e) {
                throw new ServiceValidationException(false, UNEXPECTED_ERROR_DESC);
            }
        }
        return operationIndexingResponse;
    }
}
