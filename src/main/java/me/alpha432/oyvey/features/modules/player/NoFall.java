package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

public class NoFall extends Module { // must match file name
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFall() {
        super("PhaseThroughBlocks", "Walk through walls but stay grounded", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.noClip = true; // disable collisions
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
        if (mc.player == null || mc.world == null) return;

        // Enable free movement through blocks
        mc.player.noClip = true;

        // Keep normal floor behavior
        BlockPos floorPos = mc.player.getBlockPos().down();
        boolean onSolidFloor = !mc.world.getBlockState(floorPos).isAir();
        mc.player.onGround = onSolidFloor;

        // Spoof movement packet so the server/client accepts position
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY(),
                mc.player.getZ(),
                mc.player.getYaw(),
                mc.player.getPitch(),
                onSolidFloor,
                false
        ));
    }
}
