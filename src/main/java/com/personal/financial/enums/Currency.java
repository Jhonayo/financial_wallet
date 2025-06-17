package com.personal.financial.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    CLP("CLP", "Peso Chileno", "$"),
    USD("USD", "Dólar Americano", "$");

    private final String code;
    private final String name;
    private final String symbol;
}
