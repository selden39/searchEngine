package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.IndexPage;
import searchengine.dto.OperationIndexingResponse;
import searchengine.dto.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final StartIndexingService startIndexingService;
    private final StopIndexingService stopIndexingService;
    private final IndexPageService indexPageService;
    private final SearchService searchService;
    private final String DEFAULT_OFFSET = "0";
    private final String DEFAULT_LIMIT = "20";

    public ApiController(StatisticsService statisticsService,
                         StartIndexingService startIndexingService,
                         StopIndexingService stopIndexingService,
                         IndexPageService indexPageService,
                         SearchService searchService) {
        this.statisticsService = statisticsService;
        this.startIndexingService = startIndexingService;
        this.stopIndexingService = stopIndexingService;
        this.indexPageService = indexPageService;
        this.searchService = searchService;
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

    @PostMapping("/indexPage")
    public ResponseEntity<OperationIndexingResponse> indexPage(@RequestBody IndexPage indexPage){
        if (indexPage.getUrl() == null || indexPage.getUrl().isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(indexPageService.postIndexPage(indexPage));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "site", required = false) String searchSite,
            @RequestParam(value = "offset",
                    required = false,
                    defaultValue = DEFAULT_OFFSET
            ) Integer offset,
            @RequestParam(value = "limit",
                    required = false,
                    defaultValue = DEFAULT_LIMIT
            ) Integer limit) {
//TODO https://sky.pro/wiki/java/rabota-s-query-parametrami-v-spring-boot-kontrollere/
// 400 - Bad Request,
// 401 - Unauthorized,
// 403 - Forbidden,
// 404 - Not Found,
// 405 - Method Not Allowed
// 500 - Internal Server Error

        SearchResponse searchResponse = searchService.search(query, searchSite, offset, limit);
        return ResponseEntity.ok(searchResponse);
    }
}
