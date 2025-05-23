package ro.mpp2024.client;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ro.mpp2024.domain.Proba;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class ProbaClient {

    private final RestClient restClient = RestClient.builder().requestInterceptor(new CustomRestClientInterpretor()).build();

    public static final String URL = "http://localhost:8081/api/probe";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException("Eroare REST: " + e.getMessage(), e);
        }
    }

    public Iterable<Proba> getAll() {
        Proba[] probe = execute(() -> restClient.get().uri(URL).retrieve().body(Proba[].class));
        return Arrays.asList(probe);
    }

    public Proba getById(Long id) {
        return execute(() -> restClient.get()
                .uri(String.format("%s/%s", URL, id))
                .retrieve()
                .body(Proba.class));
    }

    public Proba create(Proba proba) {
        return execute(() -> restClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(proba)
                .retrieve()
                .body(Proba.class));
    }

    public void delete(Long id) {
        execute(() -> restClient.delete()
                .uri(String.format("%s/%s", URL, id))
                .retrieve()
                .toBodilessEntity());
    }

    public Proba update(Long id, Proba proba) {
        return execute(() -> restClient.put()
                .uri(String.format("%s/%s", URL, id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(proba)
                .retrieve()
                .body(Proba.class));
    }
}
