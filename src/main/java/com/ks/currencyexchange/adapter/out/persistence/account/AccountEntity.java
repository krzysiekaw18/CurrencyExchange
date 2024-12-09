package com.ks.currencyexchange.adapter.out.persistence.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AccountEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PLN_BALANCE")
    private BigDecimal plnBalance;

    @Column(name = "USD_BALANCE")
    private BigDecimal usdBalance;
}
