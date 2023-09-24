package app.fiveminchange;

import app.bot.controller.Chat;
import app.fiveminchange.model.RequestDetails;
import app.fiveminchange.model.RootObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class SSEService implements CommandLineRunner {
    @Autowired
    private Chat chat;
    @Autowired
    private CreateMsg createMsg;

    private Instant lastWaitTime = Instant.now().plus(Duration.ofHours(3));
    private Instant lastVerifyTime = Instant.now().plus(Duration.ofHours(3));

    private final WebClient webClient;

    public SSEService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://5minchange.ru/api/admin/").build();
    }

    @Override
    public void run(String... args) throws Exception {
        listenToSSE(10, 0, "wait");
        listenToSSE(10, 0, "verify");
    }

    public void listenToSSE(int limit, int skip, String filter) {
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
                .publish()
                .autoConnect()
                .doOnNext(rootObject -> {
                    List<RequestDetails> list = rootObject.getRequests();

                    Instant lastTime = filter.equals("wait") ? lastWaitTime : lastVerifyTime;

                    list.stream()
                            .filter(request -> Instant.parse(request.getDate()).isAfter(lastTime))
                            .forEach(request -> chat.executeMsg(createMsg.getNewSendMessage(request)));

                    list.stream()
                            .map(RequestDetails::getDate)
                            .map(Instant::parse)
                            .max(Comparator.naturalOrder())
                            .ifPresent(maxDate -> {
                                if (filter.equals("wait")) {
                                    lastWaitTime = maxDate;
                                } else if (filter.equals("verify")) {
                                    lastVerifyTime = maxDate;
                                }
                            });

                })
                .doOnError(e -> {
                    System.out.println("Соединение с сервером было закрыто: " + e.getMessage());
                })
                .onErrorResume(e -> Flux.empty())
                .subscribe();
    }


//    public void listenToSSE(int limit, int skip, String filter) {
//        webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/getRequests")
//                        .queryParam("limit", limit)
//                        .queryParam("skip", skip)
//                        .queryParam("filter", filter)
//                        .build())
//                .accept(MediaType.TEXT_EVENT_STREAM)
//                .retrieve()
//                .bodyToFlux(RootObject.class)
//                .publish()
//                .autoConnect()
//                .doOnNext(rootObject -> {
//                    List<RequestDetails> list = rootObject.getRequests();
//
//                    Instant lastTime = filter.equals("wait") ? lastWaitTime : lastVerifyTime;
//
//                    list.stream()
//                            .filter(request -> Instant.parse(request.getDate()).isAfter(lastTime))
//                            .forEach(request -> chat.executeMsg(createMsg.getNewSendMessage(request)));
//
//                    list.stream()
//                            .map(RequestDetails::getDate)
//                            .map(Instant::parse)
//                            .max(Comparator.naturalOrder())
//                            .ifPresent(maxDate -> {
//                                if (filter.equals("wait")) {
//                                    lastWaitTime = maxDate;
//                                } else if (filter.equals("verify")) {
//                                    lastVerifyTime = maxDate;
//                                }
//                            });
//
//                }).subscribe();
//    }
//    public void listenToSSE(int limit, int skip, String filter) {
//        webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/getRequests")
//                        .queryParam("limit", limit)
//                        .queryParam("skip", skip)
//                        .queryParam("filter", filter)
//                        .build())
//                .accept(MediaType.TEXT_EVENT_STREAM)
//                .retrieve()
//                .bodyToFlux(RootObject.class)
//                .doOnNext(rootObject -> {
//                    List<RequestDetails> list = rootObject.getRequests();
//
//                    Instant lastTime = filter.equals("wait") ? lastWaitTime : lastVerifyTime;
//
//                    list.stream()
//                            .filter(request -> Instant.parse(request.getDate()).isAfter(lastTime))
//                            .forEach(request -> chat.executeMsg(createMsg.getNewSendMessage(request)));
//
//                    list.stream()
//                            .map(RequestDetails::getDate)
//                            .map(Instant::parse)
//                            .max(Comparator.naturalOrder())
//                            .ifPresent(maxDate -> {
//                                if (filter.equals("wait")) {
//                                    lastWaitTime = maxDate;
//                                } else if (filter.equals("verify")) {
//                                    lastVerifyTime = maxDate;
//                                }
//                            });
//
//                }).subscribe();
//    }
}
