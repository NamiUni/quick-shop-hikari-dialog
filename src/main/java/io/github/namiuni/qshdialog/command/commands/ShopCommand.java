package io.github.namiuni.qshdialog.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogConfiguration;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.user.QSHUserService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopCommand implements QSHCommand {

    private final ConfigurationHolder<DialogConfiguration> creationConfig;
    private final ConfigurationHolder<DialogConfiguration> modificationConfig;
    private final QSHUserService userService;

    public ShopCommand(
            final ConfigurationHolder<DialogConfiguration> creationConfig,
            final ConfigurationHolder<DialogConfiguration> modificationConfig,
            final QSHUserService userService
    ) {
        this.creationConfig = creationConfig;
        this.modificationConfig = modificationConfig;
        this.userService = userService;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("dialogshop")
                // .requires()
                .then(this.createNode())
                // .then(this.modificationNode())
                .build();
    }

    @Override
    public List<String> aliases() {
        return QSHCommand.super.aliases();
    }

    @Override
    public String description() {
        return QSHCommand.super.description();
    }

    private CommandNode<CommandSourceStack> createNode() {
        return Commands.literal("create")
                // .requires(source -> Commands.restricted())
                .executes(context -> {
                    if (context.getSource().getExecutor() instanceof Player player) {
                        final QSHUser owner = this.userService.getUser(player);
                        final ItemStack product = player.getInventory().getItemInMainHand();
                        final var dialogContext = new DialogProviderContext(owner, QSHUser.empty(), product);

                        final DialogLike dialog = this.creationConfig.getConfig().createDialog(dialogContext);
                        owner.showDialog(dialog);

                        return Command.SINGLE_SUCCESS;
                    }

                    return SINGLE_FAILED;
                })
                .build();
    }

    private CommandNode<CommandSourceStack> modificationNode() {
        return null;
    }
}
