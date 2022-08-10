package de.athoramine.core.commands;

import de.athoramine.core.forms.SecretsCollectionForm;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;

public class SecretsCommand extends PluginCommand<Main> {

    public SecretsCommand(Main owner) {
        super("secrets", owner);
        setDescription("Secrets Sammlung");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        new SecretsCollectionForm(player);
        return true;
    }
}
