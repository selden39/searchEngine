package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.services.indexingexecutor.ThreadCollector;
import searchengine.services.indexinginterrupter.IndexingInterrupter;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class StopIndexingServiceImpl implements StopIndexingService{

    @Override
    public OperationIndexingResponse getStopIndexing(){
        OperationIndexingResponse operationIndexingResponse;

        ThreadCollector.getIndexingThreads().forEach((thread, forkJoinPool) -> {
            Thread interrupter = new Thread(new IndexingInterrupter(thread));
            interrupter.start();
            System.out.println("+++++++++++++++++");
            System.out.println(thread.getName() + " - " + thread.isInterrupted());
            System.out.println(forkJoinPool + ": " + forkJoinPool.getRunningThreadCount());
            System.out.println(forkJoinPool.isShutdown());
            forkJoinPool.shutdownNow();
            System.out.println(forkJoinPool.isShutdown());
            System.out.println("------------------");
            System.out.println(thread.isInterrupted());
        });

        LocalDateTime now = LocalDateTime.now();
        operationIndexingResponse = new OperationIndexingResponse(true, now.toString());
        return operationIndexingResponse;
    }
}
