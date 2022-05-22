package AthoraCore.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Level;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.utils.DyeColor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Helper {

    public static void playSound(String name, Player player, float volume, float pitch) {
        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.name = name;
        playSoundPacket.x = (int) player.x;
        playSoundPacket.y = (int) player.y;
        playSoundPacket.z = (int) player.z;
        playSoundPacket.volume = volume;
        playSoundPacket.pitch = pitch;
        player.dataPacket(playSoundPacket);
    }

    public static boolean levelExists(String name) {
        Level[] levels = Server.getInstance().getLevels().values().toArray(new Level[0]);
        for (Level level : levels) {
            if (level.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static boolean stringContainsItemFromList(String inputString, String[] items) {
        return Arrays.stream(items).anyMatch(inputString::contains);
    }

    public static int getRandomIntBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static void createFireworkParticle(Player player, DyeColor dyeColor) {
        final EntityFirework entityFirework = new EntityFirework(player.getChunk(), Entity.getDefaultNBT(player));
        try {
            final Field lifetimeField = EntityFirework.class.getDeclaredField("lifetime");
            lifetimeField.setAccessible(true);
            lifetimeField.set(entityFirework, 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        final ItemFirework itemFirework = new ItemFirework();
        itemFirework.setCompoundTag(new CompoundTag().putCompound("Fireworks", new CompoundTag().putList(new ListTag<>("Explosions"))));
        itemFirework.clearExplosions();
        itemFirework.addExplosion(new ItemFirework.FireworkExplosion().
                addColor(dyeColor).
                type(ItemFirework.FireworkExplosion.ExplosionType.LARGE_BALL));
        entityFirework.setFirework(itemFirework);
        entityFirework.spawnToAll();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String[] append(String[] array, String value) {
        String[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

    public static Entity[] append(Entity[] array, Entity value) {
        Entity[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

    public static Block[] append(Block[] array, Block value) {
        Block[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

    public static Block[][] append(Block[][] array, Block[] value) {
        Block[][] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

}
