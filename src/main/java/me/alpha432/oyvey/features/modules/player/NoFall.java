package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public class PhaseThroughBlocks extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public PhaseThroughBlocks() {
        super("PhaseThroughBlocks", "Walk through walls, pistons, etc., but still stand on the floor", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        // Get the block directly under the player
        BlockPos floorPos = mc.player.getBlockPos().down();
        BlockState floorState = mc.world.getBlockState(floorPos);

        // Ensure we only interact with the floor normally
        if (!floorState.isAir()) {
            mc.player.onGround = true;
        }

        // Phase through all other blocks
        // By sending a movement packet each tick to "legitimize" our position
        PlayerMoveC2SPacket.Full packet = new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY(),
                mc.player.getZ(),
                mc.player.getYaw(),
                mc.player.getPitch(),
                mc.player.isOnGround(),
                mc.player.horizontalCollision
        );
        mc.player.networkHandler.sendPacket(packet);
    }
}
