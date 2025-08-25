package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PhaseThroughBlocks extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public PhaseThroughBlocks() {
        super("PhaseThroughBlocks", "Walk through walls but stay grounded", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.noClip = true; // disable block collisions
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.noClip = false; // restore collisions
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) return;

        // Allow free movement through blocks
        mc.player.noClip = true;
        mc.player.setVelocity(0, 0, 0);

        // Spoof a movement packet every tick so the server "accepts" our position
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY(),
                mc.player.getZ(),
                mc.player.getYaw(),
                mc.player.getPitch(),
                true, // pretend we're on ground
                false
        ));
    }
}
