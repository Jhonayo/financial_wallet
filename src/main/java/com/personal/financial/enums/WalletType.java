package com.personal.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletType {

    EFECTIVO("Efectivo", "fas fa-money-bill-wave", "#28a745"),
    BANCO("Banco", "fas fa-university", "#007bff"),
    TARJETA_CREDITO("Tarjeta de Crédito", "fas fa-credit-card", "#dc3545"),
    AHORRO("Ahorro", "fas fa-piggy-bank", "#6f42c1");

    private final String displayName;
    private final String icon;
    private final String color;
}
