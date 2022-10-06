package de.athoramine.core.forms.DailyQuest;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.components.Quest;
import de.athoramine.core.dialogs.DailyQuest.DailyQuestDetailDialog;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.DailyQuestManager;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class DailyQuestForm {
    public DailyQuestForm(Player player) {
        SimpleForm form = (new SimpleForm("Explorer Finn - Daily Quest")).setContent("§eHey! ich hab hier eine Liste an Sachen, kannst du mir die Besorgen?\n\n§r§7§o(Täglicher Reset um 2:00 Uhr)");
        Quest[] allQuests = DailyQuestManager.getDailyQuests();
        Quest[] completedQuests = DailyQuestManager.getCompletedQuests(player);
        Quest[] notCompletedQuests = new Quest[]{};
        for (Quest allQuest : allQuests) {
            boolean completed = false;
            for (Quest completedQuest : completedQuests) {
                if (allQuest.getQuestID() == completedQuest.getQuestID()) {
                    completed = true;
                }
            }
            if (!completed) {
                notCompletedQuests = Helper.append(notCompletedQuests, allQuest);
            }
        }
        for (Quest quest : notCompletedQuests) {
            form.addButton(TextFormat.colorize('&', quest.getQuestType().getName()) + "\n§r§o§c§lAb Level " + quest.getQuestType().getRequiredLevel());
        }
        Quest[] finalNotCompletedQuests = notCompletedQuests;
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            Quest selectedQuest = finalNotCompletedQuests[data];
            if (selectedQuest.getQuestType().getRequiredLevel() > AthoraPlayer.getLevel(targetPlayer)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du erfüllst nicht die Level Anforderung!");
                return;
            }
            // new DailyQuestDetailForm(targetPlayer, selectedQuest);

            // TODO find a better solution for finding the npc
            Entity[] entities = targetPlayer.getLevel().getEntities();
            Entity npc = null;
            for (Entity entity : entities) {
                if (entity instanceof EntityHuman && entity.getName().startsWith("§8§oExplorer Finn\n")) {
                    npc = entity;
                }
            }

            if (npc == null) {
                new DailyQuestDetailForm(targetPlayer, selectedQuest);
                Server.getInstance().getLogger().warning("Cannot find NPC 'Explorer Finn'!");
            } else {
                new DailyQuestDetailDialog(targetPlayer, npc, selectedQuest);
            }
        });
    }
}
