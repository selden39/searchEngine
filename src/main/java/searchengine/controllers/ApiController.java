package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.IndexPageRequest;
import searchengine.dto.OperationIndexingResponse;
import searchengine.dto.SearchPageDto1;
import searchengine.dto.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.*;

import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class ApiController {

    private final StatisticsService statisticsService;
    private final StartIndexingService startIndexingService;
    private final StopIndexingService stopIndexingService;
    private final IndexPageService indexPageService;
    private final SearchService searchService;
    private final SearchService1 searchService1;
    private final String DEFAULT_OFFSET = "0";
    private final String DEFAULT_LIMIT = "20";

    public ApiController(StatisticsService statisticsService,
                         StartIndexingService startIndexingService,
                         StopIndexingService stopIndexingService,
                         IndexPageService indexPageService,
                         SearchService searchService,
                         SearchService1 searchService1) {
        this.statisticsService = statisticsService;
        this.startIndexingService = startIndexingService;
        this.stopIndexingService = stopIndexingService;
        this.indexPageService = indexPageService;
        this.searchService = searchService;
        this.searchService1 = searchService1;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<OperationIndexingResponse> startIndexing() throws ServiceValidationException {
        OperationIndexingResponse operationIndexingResponse;
        operationIndexingResponse = startIndexingService.getStartIndexing();
        return ResponseEntity.ok(operationIndexingResponse);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<OperationIndexingResponse> stopIndexing() throws ServiceValidationException{
        return ResponseEntity.ok(stopIndexingService.getStopIndexing());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<OperationIndexingResponse> indexPage(@RequestBody IndexPageRequest indexPageRequest) throws ServiceValidationException{
        if (indexPageRequest.getUrl() == null || indexPageRequest.getUrl().isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(indexPageService.postIndexPage(indexPageRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam(value = "query") @NotBlank String query,
            @RequestParam(value = "site", required = false) String searchSite,
            @RequestParam(value = "offset",
                    required = false,
                    defaultValue = DEFAULT_OFFSET
            ) @PositiveOrZero Integer offset,
            @RequestParam(value = "limit",
                    required = false,
                    defaultValue = DEFAULT_LIMIT
            ) @PositiveOrZero @Max(100) Integer limit) {

        SearchResponse searchResponse = searchService.search(query, searchSite, offset, limit);
        return ResponseEntity.ok(searchResponse);
    }

    @GetMapping("/search1")
    public ResponseEntity<List<SearchPageDto1>> search1(
            @RequestParam(value = "query") @NotBlank String query,
            @RequestParam(value = "site", required = false) String searchSite,
            @RequestParam(value = "offset",
                    required = false,
                    defaultValue = DEFAULT_OFFSET
            ) @PositiveOrZero Integer offset,
            @RequestParam(value = "limit",
                    required = false,
                    defaultValue = DEFAULT_LIMIT
            ) @PositiveOrZero @Max(100) Integer limit) {

        List<SearchPageDto1> searchResponseList1 = searchService1.search1(query, searchSite, offset, limit);
        return ResponseEntity.ok(searchResponseList1);
    }
}
