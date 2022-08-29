package com.example.server.GameResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<GameResult> getResults() {
        return resultRepository.findAll();
    }

    public void addNewGameResult(GameResult gameResult) {
        resultRepository.save(gameResult);
    }


    public void deleteResult(Long id) {
        boolean exists = resultRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("there is not gameResult with such a id");
        }
        resultRepository.deleteById(id);
    }

    public GameResult getResultById(Long id) {
        Optional<GameResult> optionalResult = resultRepository.findById(id);
        if (optionalResult.isEmpty()) {
            throw new IllegalStateException("there is not gameResult with such a id");
        }
        return optionalResult.get();
    }

    public Integer getTheBestResult() {
        Optional<Integer> result = resultRepository.getTheBestResult();
        if (result.isEmpty()) {
            return 0;
        }
        return result.get();
    }

    public void deleteAllResults() {
        resultRepository.deleteAll();
    }
}
