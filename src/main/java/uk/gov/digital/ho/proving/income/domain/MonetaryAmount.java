package uk.gov.digital.ho.proving.income.domain;

import java.math.BigDecimal;

/**
 * Created by andrewmoores on 17/03/2016.
 */
public class MonetaryAmount {
    private BigDecimal amount;
    private String isoCurrencyCode;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIsoCurrencyCode() {
        return isoCurrencyCode;
    }

    public void setIsoCurrencyCode(String isoCurrencyCode) {
        this.isoCurrencyCode = isoCurrencyCode;
    }
}
