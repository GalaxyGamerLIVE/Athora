package de.athoramine.core.forms.DailyQuest;

import de.athoramine.core.components.Quest;
import de.athoramine.core.util.manager.DailyQuestManager;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class DailyQuestDetailForm {
    public DailyQuestDetailForm(Player player, Quest quest) {
        SimpleForm form = new SimpleForm("§8<======= §l§6Quest§r§8 ========>");
        form.setContent("§fBringe §g" + quest.getAmount() + " " + DailyQuestManager.getItemName(quest) + "§f zu Explorer Finn!\n\n§6Belohnung:\n§7- §a" + quest
                .getMoneyReward() + "§2$\n§7- §6" + quest
                .getRuhmReward() + " §gRuhm");
        form.addButton("§aAnnehmen");
        form.addButton("§cZurück");
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1)
                return;
            int accept = 0;
            int back = 1;
            if (data == accept) {
                if (DailyQuestManager.hasActiveQuest(targetPlayer)) {
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast bereits eine aktive Quest!");
                    return;
                }
                DailyQuestManager.startQuest(targetPlayer, quest);
                player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich eine tägliche Quest angenommen!");
                return;
            }
            if (data == back)
                new DailyQuestForm(targetPlayer);
        });
    }
}
