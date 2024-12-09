package com.ks.currencyexchange.infrastructure.usecase;

public interface UseCase<IN extends UseCase.InputValues, OUT extends UseCase.OutputValues> {

    OUT execute(final IN input);

    interface InputValues {
    }

    interface OutputValues {
    }
}