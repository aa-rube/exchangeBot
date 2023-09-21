package app.fiveminchange;

import app.bot.controller.Chat;
import app.fiveminchange.model.RequestDetails;
import app.fiveminchange.model.RootObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class SSEService implements CommandLineRunner {
    @Autowired
    private Chat chat;
    @Autowired
    private CreateMsg createMsg;
    private final WebClient webClient;

    @Override
    public void run(String... args) throws Exception {
        listenToSSEVerify(10, 0, "verify");
        listenToSSEWait(10, 0, "wait");
    }
    public SSEService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://5minchange.ru/api/admin/").build();
    }
    public void listenToSSEVerify(int limit, int skip, String filter) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getRequests")
                        .queryParam("limit", limit)
                        .queryParam("skip", skip)
                        .queryParam("filter", filter)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(RootObject.class)
                .doOnSubscribe(subscription -> {
                }).doOnNext(rootObject -> {
                    for (RequestDetails request : rootObject.getRequests()) {
                        if ("VERIFY".equals(request.getStatus())) {
                            chat.executeMsg(createMsg.getNewSendMessage(request));
                        }
                    }
                }).subscribe();
    }
    public void listenToSSEWait(int limit, int skip, String filter) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getRequests")
                        .queryParam("limit", limit)
                        .queryParam("skip", skip)
                        .queryParam("filter", filter)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(RootObject.class)
                .doOnSubscribe(subscription -> {
                }).doOnNext(rootObject -> {
                    for (RequestDetails request : rootObject.getRequests()) {
                        if ("WAIT".equals(request.getStatus())) {
                            chat.executeMsg(createMsg.getNewSendMessage(request));
                        }
                    }
                }).subscribe();
    }

}