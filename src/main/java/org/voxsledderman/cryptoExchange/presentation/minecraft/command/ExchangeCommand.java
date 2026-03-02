package org.voxsledderman.cryptoExchange.presentation.minecraft.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;
import org.voxsledderman.cryptoExchange.presentation.minecraft.MenuContext;
import org.voxsledderman.cryptoExchange.presentation.minecraft.menu.MainMenu;

@Command(name = "exchange", aliases = {"ex", "giełda"})
public class ExchangeCommand {
    private final PriceProvider priceProvider;
    private final MenuContext menuContext;

    public ExchangeCommand(MenuContext menuContext, PriceProvider priceProvider) {
        this.priceProvider = priceProvider;
        this.menuContext = menuContext;
    }

    @Execute
    void openExchange(@Context CommandSender sender){
        if(sender instanceof Player player){
            MainMenu menu = new MainMenu(menuContext ,player, priceProvider);
            menu.openMenu(player);

        }
    }
}
