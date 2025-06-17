package com.personal.financial.entity;

import com.personal.financial.enums.Currency;
import com.personal.financial.enums.WalletType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "wallets")
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType type;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 3)
    private Currency currency = Currency.CLP;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /*
     * Constructor de nueva wallet
     * */
    public Wallet(User user, String name, WalletType type, Currency currency) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.isActive = true;
    }


    /**
     * Añade dinero a la wallet
     */
    public void addBalance(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        }
    }

    /**
     * Resta dinero de la wallet
     */
    public boolean subtractBalance(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            if (this.balance.compareTo(amount) >= 0) {
                this.balance = this.balance.subtract(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si tiene fondos suficientes
     */
    public boolean hasSufficientBalance(BigDecimal amount) {
        return amount != null && this.balance.compareTo(amount) >= 0;
    }
}

