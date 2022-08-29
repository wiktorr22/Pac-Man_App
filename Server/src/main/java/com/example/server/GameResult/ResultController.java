package com.example.server.GameResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/result")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<GameResult> getResults() {
        return resultService.getResults();
    }

    @PostMapping
    public void addNewResult(@RequestBody GameResult gameResult) {
        resultService.addNewGameResult(gameResult);
    }

    @DeleteMapping(path = "/{resultId}")
    public void deleteGameResult(@PathVariable("resultId") Long id) {
        resultService.deleteResult(id);
    }

    @DeleteMapping(path = "/deleteAll")
    public void deleteAllResults() {
        resultService.deleteAllResults();
    }

    @GetMapping(path = "resultId/{id}")
    public GameResult getResult(@PathVariable("id") Long id) {
        return resultService.getResultById(id);
    }

    @GetMapping("theBestResult")
    public Integer getTheBestResult() {
        return resultService.getTheBestResult();
    }
}


