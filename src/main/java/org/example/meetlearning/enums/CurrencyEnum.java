package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {

    CU001("RMB", "人民币"),
    CU002("USD", "美元"),
    CU003("PHP", "菲利宾"),
    ;

    private final String name;
    private final String symbol;

    public static CurrencyEnum getBySymbol(String symbol) {
        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            if (currencyEnum.symbol.equals(symbol)) {
                return currencyEnum;
            }
        }
        return null;
    }

}
