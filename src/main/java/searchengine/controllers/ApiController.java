package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.IndexPage;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.IndexPageService;
import searchengine.services.StartIndexingService;
import searchengine.services.StatisticsService;
import searchengine.services.StopIndexingService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final StartIndexingService startIndexingService;
    private final StopIndexingService stopIndexingService;
    private final IndexPageService indexPageService;

    public ApiController(StatisticsService statisticsService,
                         StartIndexingService startIndexingService,
                         StopIndexingService stopIndexingService,
                         IndexPageService indexPageService) {
        this.statisticsService = statisticsService;
        this.startIndexingService = startIndexingService;
        this.stopIndexingService = stopIndexingService;
        this.indexPageService = indexPageService;
    }

    // TODO всегда возвращаем HTTP 200 ок

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

    @PostMapping("/indexPage")
    public ResponseEntity<OperationIndexingResponse> indexPage(@RequestBody IndexPage indexPage){
        if (indexPage.getUrl() == null || indexPage.getUrl().isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        System.out.println("POST: " + indexPage.getUrl());

        return ResponseEntity.ok(indexPageService.postIndexPage(indexPage));
    }
}
