package com.ks.currencyexchange.infrastructure.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiProblemDetail extends ProblemDetail {

    public static ApiProblemDetail of(final HttpStatus httpStatus,
                                      final String detail) {
        ApiProblemDetail apiProblemDetail = new ApiProblemDetail();
        apiProblemDetail.setStatus(httpStatus);
        apiProblemDetail.setDetail(detail);
        return apiProblemDetail;
    }
}
