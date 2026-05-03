package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy;

import com.ghostchu.quickshop.api.economy.EconomyProvider;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class EconomyService {

    private final EconomyProvider economyProvider;
    private final QSConfiguration qsConfig;

    @Inject
    EconomyService(
            final EconomyProvider economyProvider,
            final QSConfiguration qsConfig
    ) {
        this.economyProvider = economyProvider;
        this.qsConfig = qsConfig;
    }

    public boolean supportsMultiCurrency() {
        return this.economyProvider.multiCurrency();
    }

    public BigDecimal getBalance(final UserSession user, final String world, final @Nullable String currency) {
        return this.economyProvider.balance(user.qsUser(), world, currency);
    }

    public boolean withdrawMoney(final UserSession user, final BigDecimal amount, final String worldName) {
        return this.economyProvider.withdraw(
                user.qsUser(),
                worldName,
                this.qsConfig.defaultCurrency(),
                amount
        );
    }

    public boolean depositMoney(final UserSession user, final BigDecimal amount, final String worldName) {
        return this.economyProvider.deposit(
                user.qsUser(),
                worldName,
                this.qsConfig.defaultCurrency(),
                amount
        );
    }
}
