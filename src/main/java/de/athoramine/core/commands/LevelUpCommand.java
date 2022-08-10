package de.athoramine.core.commands;

import de.athoramine.core.forms.LevelUpForm;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;

public class LevelUpCommand extends PluginCommand<Main> {

    public LevelUpCommand(Main owner) {
        super("levelup", owner);
        this.setDescription("Upgrade dein Level!");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        new LevelUpForm(player);
        return true;
    }

}
