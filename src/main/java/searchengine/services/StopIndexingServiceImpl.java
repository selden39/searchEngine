package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.services.indexingexecutor.ThreadCollector;
import searchengine.services.indexinginterrupter.IndexingInterrupter;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StopIndexingServiceImpl implements StopIndexingService{

    @Override
    public OperationIndexingResponse getStopIndexing(){
        OperationIndexingResponse operationIndexingResponse;

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println("============ ThreadSet before =========");
        threadSet.forEach(thread -> {
            System.out.println(thread.getName() + " - interrupted: " + thread.isInterrupted());
        });

        ThreadCollector.getIndexingThreads().forEach(thread -> {
            Thread interrupter = new Thread(new IndexingInterrupter(thread));
            System.out.println("========== Thread to interrupt : " + thread.getName());

            interrupter.start();
        });

        Set<Thread> threadSet2 = Thread.getAllStackTraces().keySet();
        System.out.println("============ ThreadSet after =========");
        threadSet2.forEach(thread -> {
            System.out.println(thread.getName() + " - interrupted: " + thread.isInterrupted());
        });

        LocalDateTime now = LocalDateTime.now();
        operationIndexingResponse = new OperationIndexingResponse(true, now.toString());
        return operationIndexingResponse;
    }
}
