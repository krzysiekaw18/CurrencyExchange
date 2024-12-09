package com.ks.currencyexchange.infrastructure.usecase;

public interface ConsumerUseCase<IN extends ConsumerUseCase.InputValues> {

    void execute(final IN input);

    interface InputValues {
    }
}
