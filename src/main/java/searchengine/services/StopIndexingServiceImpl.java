package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.services.indexingexecutor.ThreadCollector;


@RequiredArgsConstructor
@Service
public class StopIndexingServiceImpl implements StopIndexingService{
    private final String UNEXPECTED_ERROR_DESC = "Непредвиденная ошибка";
    private final String INDEXING_NOT_RUN__ERROR_DESC = "Индексация не запущена";

    @Override
    public OperationIndexingResponse getStopIndexing(){
        OperationIndexingResponse operationIndexingResponse;

        if (ThreadCollector.getIndexingThreads().isEmpty()){
            operationIndexingResponse = new OperationIndexingResponse(false, INDEXING_NOT_RUN__ERROR_DESC);
        } else {
            try {
                ThreadCollector.getIndexingThreads().forEach((thread, forkJoinPool) -> {
                    forkJoinPool.shutdownNow();
                });
                operationIndexingResponse = new OperationIndexingResponse(true);
            } catch (Exception e) {
                operationIndexingResponse = new OperationIndexingResponse(false, UNEXPECTED_ERROR_DESC);
            }
        }
        return operationIndexingResponse;
    }
}
