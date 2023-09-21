package app.fiveminchange;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import app.fiveminchange.model.RequestDetails;
import app.fiveminchange.model.RootObject;

public class SSEServiceJersey {

    public static void main(String[] args) {
        SSEServiceJersey j = new SSEServiceJersey();
        j.listenToSSEVerify(10, 0);
    }

    private final String baseUrl = "https://5minchange.ru/api/admin";
    private final Client client;

    public SSEServiceJersey() {
        this.client = ClientBuilder.newClient();
    }

    public void listenToSSEVerify(int limit, int skip) {
        WebTarget target = client.target(baseUrl)
                .path("/getRequests")
                .queryParam("limit", limit)
                .queryParam("skip", skip)
                .queryParam("filter", "verify");

        try (SseEventSource source = SseEventSource.target(target).build()) {

            source.register(event -> {
                System.out.println("Received data, starting processing...");
                RootObject rootObject = event.readData(RootObject.class);
                for (RequestDetails request : rootObject.getRequests()) {
                    System.out.println(request.getRequestId());
                }
                System.out.println("Data processing completed.");
            }, throwable -> {
                System.out.println("Error on SSE connection: " + throwable.getMessage());
            }, () -> {
                System.out.println("SSE connection with filter verify terminated");
            });

            source.open();

            while (true) { // добавлен бесконечный цикл
                try {
                    Thread.sleep(1000); // ждать 1 секунду перед следующей итерацией
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted: " + e.getMessage());
                    break; // выйти из цикла, если поток был прерван
                }
            }
        }
    }
}