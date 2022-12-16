package de.athoramine.core.dialogs;

import cn.nukkit.dialog.window.FormWindowDialog;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;

public abstract class DialogBase {

    protected FormWindowDialog createDialog(Entity npc) {
        FormWindowDialog dialog = new FormWindowDialog("TITLE", "CONTENT", npc);
        if (npc instanceof EntityHuman) {
            dialog.setSkinData("{\"picker_offsets\":{\"scale\":[1.75,1.75,1.75],\"translate\":[0,0,0]},\"portrait_offsets\":{\"scale\":[1.75,1.75,1.75],\"translate\":[0,-50,0]}}");
        }
        return dialog;
    }

}
