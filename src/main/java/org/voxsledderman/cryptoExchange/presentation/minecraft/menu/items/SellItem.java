package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.application.usecases.SellCryptoUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.infrastructure.providers.CryptoInfo;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.math.BigDecimal;

public class SellItem extends AbstractItem {

    private final SellCryptoUseCase sellCryptoUseCase;
    private final BigDecimal pickedAmount;
    private final JavaPlugin plugin;
    private final Wallet wallet;
    private final String ticker;
    private final CryptoInfo cryptoInfo;
    private boolean error = false;

    public SellItem(SellCryptoUseCase sellCryptoUseCase, BigDecimal pickedAmount,
                    JavaPlugin plugin, Wallet wallet, String ticker, CryptoInfo cryptoInfo) {
        this.sellCryptoUseCase = sellCryptoUseCase;
        this.pickedAmount = pickedAmount;
        this.plugin = plugin;
        this.wallet = wallet;
        this.ticker = ticker;
        this.cryptoInfo = cryptoInfo;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (error) {
            return new ItemBuilder(Material.BARRIER).setDisplayName("error");
        }
        return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("sell");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player,
                            @NotNull InventoryClickEvent event) {
        if (pickedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            triggerErrorState();
            return;
        }

        boolean success = sellCryptoUseCase.sellCrypto(
                player.getUniqueId(), wallet, ticker, pickedAmount
        );

        if (!success) {
            triggerErrorState();
        } else {
            player.closeInventory();
            player.sendMessage("You sold (%s)x %s".formatted(pickedAmount, cryptoInfo.fullName()));
        }
    }

    private void triggerErrorState() {
        error = true;
        notifyWindows();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            error = false;
            notifyWindows();
        }, 50L);
    }
}