package de.athoramine.core.dialogs.DailyQuest;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.dialog.window.FormWindowDialog;
import cn.nukkit.entity.Entity;
import cn.nukkit.network.protocol.NPCRequestPacket;
import cn.nukkit.utils.TextFormat;
import de.athoramine.core.components.Quest;
import de.athoramine.core.dialogs.DialogBase;
import de.athoramine.core.forms.DailyQuest.DailyQuestForm;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.DailyQuestManager;
import de.athoramine.core.util.manager.MineManager;

public class DailyQuestDetailDialog extends DialogBase {

    public DailyQuestDetailDialog(Player player, Entity npc, Quest quest) {
        FormWindowDialog dialog = createDialog(npc);
        dialog.setTitle("§l" + TextFormat.AQUA + "Explorer Finn");
        dialog.setContent("§fBring mir §g" + quest.getAmount() + " " + DailyQuestManager.getItemName(quest) +
                "§f dann bekommst du als §6Belohnung§f:\n\n§7- §a" +
                quest.getMoneyReward() + "§2$\n§7- §6" + quest.getRuhmReward() + " §gRuhm");
        dialog.addButton("§l§2Annehmen");
        dialog.addButton("§l§cZurück");
        dialog.addHandler((p, r) -> {
            if (r.getRequestType() == NPCRequestPacket.RequestType.EXECUTE_ACTION) {
                if (r.getClickedButton().getText().equalsIgnoreCase("§l§2Annehmen")) {
                    if (DailyQuestManager.hasActiveQuest(p)) {
                        p.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast bereits eine aktive Quest!");
                    } else {
                        DailyQuestManager.startQuest(p, quest);
                        player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich eine tägliche Quest angenommen!");
                    }
                }
                if (r.getClickedButton().getText().equalsIgnoreCase("§l§cZurück")) {
                    Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                        new DailyQuestForm(p);
                    }, 3, true);
                }
            }
        });
        player.showDialogWindow(dialog);
    }

}
