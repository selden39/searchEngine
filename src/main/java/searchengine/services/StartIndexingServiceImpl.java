package searchengine.services;

import org.springframework.stereotype.Service;
import searchengine.dto.statistics.StartIndexingResponse;
@Service
public class StartIndexingServiceImpl implements StartIndexingService{
    @Override
    public StartIndexingResponse getStartIndexing(){
        StartIndexingResponse startIndexingResponse =
                new StartIndexingResponse(true);
        return startIndexingResponse;
    }
}
