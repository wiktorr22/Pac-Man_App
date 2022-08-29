import com.example.server.GameResult.GameResult;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class GameClient {

     private WebClient webClient;
     private final String SERVER = "http://localhost:8080/api/v1/result";

    public GameClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<GameResult> getResults() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(GameResult.class)
                .collectList()
                .block();
    }

    public Integer getTheBestResult() {
        return webClient.get().uri("/theBestResult")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    public GameResult addNewResult(GameResult gameResult) {
        return webClient.post().syncBody(gameResult)
                .retrieve()
                .bodyToMono(GameResult.class)
                .block();
    }


}
