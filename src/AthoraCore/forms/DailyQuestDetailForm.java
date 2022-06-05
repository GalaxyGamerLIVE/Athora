package AthoraCore.forms;

import AthoraCore.components.Quest;
import AthoraCore.util.manager.DailyQuestManager;
import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class DailyQuestDetailForm {
    public DailyQuestDetailForm(Player player, Quest quest) {
        SimpleForm form = new SimpleForm("========>");
        form.setContent("§fBringe §g" + quest.getAmount() + " " + DailyQuestManager.getItemName(quest) + "§f zu Explorer Finn!\n\n§6Belohnung:\n- §a" + quest
                .getMoneyReward() + "§2$\n- §6" + quest
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
