package com.ks.currencyexchange.adapter.out.api.nbp;

import com.ks.currencyexchange.application.domain.model.common.Currency;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.application.port.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class NbpCurrencyRateService implements CurrencyRateService {

    private final NbpWebClient nbpWebClient;

    @Override
    public BigDecimal getRateForCurrency(final Currency currency) {
        return BigDecimal.valueOf(nbpWebClient.getCurrencyRate(currency)
                                              .rates()
                                              .stream()
                                              .findFirst()
                                              .orElseThrow(() -> new ResourceNotFoundException(
                                                      String.format("Currency rate for: %s, not found",
                                                                    currency.name())))
                                              .mid());
    }
}
