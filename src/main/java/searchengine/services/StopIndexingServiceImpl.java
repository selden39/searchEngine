package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.OperationIndexingResponse;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StopIndexingServiceImpl implements StopIndexingService{

    @Override
    public OperationIndexingResponse getStopIndexing(){
        OperationIndexingResponse operationIndexingResponse;
        LocalDateTime now = LocalDateTime.now();
        operationIndexingResponse = new OperationIndexingResponse(true, now.toString());
        return operationIndexingResponse;
    }
}
