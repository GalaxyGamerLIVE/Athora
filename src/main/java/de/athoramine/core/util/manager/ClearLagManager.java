package de.athoramine.core.util.manager;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;

import java.util.concurrent.atomic.AtomicInteger;

public class ClearLagManager {

    private boolean slapper = false;
    private boolean npc = false;
    private boolean pets = false;
    private boolean holograms = false;
    private boolean nametag = true;

    public static void clearAll() {
        final AtomicInteger killed = new AtomicInteger();

//        Server.getInstance().getScheduler().scheduleAsyncTask(, new AsyncTask() {
//            @Override
//            public void onRun() {
//
//            }
//        });

        Server.getInstance().getLevels().forEach((i, level) -> {
            boolean clearing = true;
            for (Entity entity : level.getEntities()) {

            }
        });
    }

}
