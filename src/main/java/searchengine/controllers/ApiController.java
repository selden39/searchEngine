package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.StartIndexingService;
import searchengine.services.StatisticsService;
import searchengine.services.StopIndexingService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final StartIndexingService startIndexingService;
    private final StopIndexingService stopIndexingService;

    public ApiController(StatisticsService statisticsService,
                         StartIndexingService startIndexingService,
                         StopIndexingService stopIndexingService) {
        this.statisticsService = statisticsService;
        this.startIndexingService = startIndexingService;
        this.stopIndexingService = stopIndexingService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<OperationIndexingResponse> startIndexing(){
        return ResponseEntity.ok(startIndexingService.getStartIndexing());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<OperationIndexingResponse> stopIndexing(){
        return ResponseEntity.ok(stopIndexingService.getStopIndexing());
    }
}
