package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public class NoFall extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFall() {
        super("PhaseThroughBlocks", "Walk through walls, pistons, etc., but still stand on the floor", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        // Check the block under the player
        BlockPos floorPos = mc.player.getBlockPos().down();
        BlockState floorState = mc.world.getBlockState(floorPos);

        // We only care about the floor - everything else is ignored
        boolean onFloor = !floorState.isAir();

        // Send movement packet to keep server in sync
        PlayerMoveC2SPacket.Full packet = new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY(),
                mc.player.getZ(),
                mc.player.getYaw(),
                mc.player.getPitch(),
                onFloor, // Pretend we're only "on ground" if standing on a floor
                mc.player.horizontalCollision
        );
        mc.player.networkHandler.sendPacket(packet);
    }
}
