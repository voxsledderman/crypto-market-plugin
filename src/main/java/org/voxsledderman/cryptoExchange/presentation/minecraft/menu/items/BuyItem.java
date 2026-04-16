package org.voxsledderman.cryptoExchange.presentation.minecraft.menu.items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.voxsledderman.cryptoExchange.application.dtos.CryptoAssetDto;
import org.voxsledderman.cryptoExchange.application.usecases.BuyCryptoUseCase;
import org.voxsledderman.cryptoExchange.domain.entities.Wallet;
import org.voxsledderman.cryptoExchange.domain.market.CryptoInfo;
import org.voxsledderman.cryptoExchange.infrastructure.config.manager.AppConfigManager;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.providers.BuyItemProvider;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import java.math.BigDecimal;

public class BuyItem extends AbstractItem {

    private final BuyCryptoUseCase buyCryptoUseCase;
    private final BigDecimal pickedAmount;
    private final JavaPlugin plugin;
    private final Wallet wallet;
    private final String ticker;
    private final CryptoInfo cryptoInfo;
    private boolean error = false;
    private final AppConfigManager appConfigManager;

    public BuyItem(BuyCryptoUseCase buyCryptoUseCase, BigDecimal pickedAmount,
                   JavaPlugin plugin, Wallet wallet, String ticker, CryptoInfo cryptoInfo, AppConfigManager appConfigManager) {
        this.buyCryptoUseCase = buyCryptoUseCase;
        this.pickedAmount = pickedAmount;
        this.plugin = plugin;
        this.wallet = wallet;
        this.ticker = ticker;
        this.cryptoInfo = cryptoInfo;
        this.appConfigManager = appConfigManager;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (error) {
            return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("<red>Error").addLoreLines(" ", "You need to specify amount!");
        }
        return BuyItemProvider.createProvider(cryptoInfo.fullName(), pickedAmount, cryptoInfo.price(), appConfigManager);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player,
                            @NotNull InventoryClickEvent event) {
        if (pickedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            triggerErrorState();
            return;
        }

        boolean success = buyCryptoUseCase.buyCrypto(
                player.getUniqueId(), wallet,
                new CryptoAssetDto(ticker, cryptoInfo.fullName(), cryptoInfo.price(), pickedAmount)
        );

        if (!success) {
            triggerErrorState();
        } else {
            player.closeInventory();
            player.sendMessage("<green>You bought <white>%sx <gold>%s".formatted(pickedAmount, cryptoInfo.fullName()));
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
