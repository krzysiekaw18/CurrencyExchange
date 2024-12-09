package com.ks.currencyexchange.adapter.out.api.nbp;

import com.ks.currencyexchange.application.domain.model.common.Currency;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
class NbpWebClient {

    private final WebClient webClient;

    public NbpWebClient(@Value("${external-service.nbp.url}")
                        String nbpUrl) {
        webClient = WebClient.builder()
                             .clientConnector(
                                     new ReactorClientHttpConnector(HttpClient.newConnection()
                                                                              .compress(true)
                                                                              .responseTimeout(Duration.ofSeconds(5))))
                             .baseUrl(nbpUrl)
                             .build();
    }

    public ExchangeRateResponse getCurrencyRate(final Currency currency) {
        return webClient.get()
                        .uri("/exchangerates/rates/A/{currencyCode}/?format=json", currency.name())
                        .retrieve()
                        .onStatus(httpStatusCode -> httpStatusCode.isSameCodeAs(HttpStatus.NOT_FOUND),
                                  error -> Mono.error(new ResourceNotFoundException(
                                          String.format("Currency details not found for: %s", currency.name())
                                  )))
                        .bodyToMono(ExchangeRateResponse.class)
                        .block();
    }

}
