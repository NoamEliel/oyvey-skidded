package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NoFall extends Module {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFall() {
        super("NoPistonPush", "LEL", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        // Check if the player is in the air and has fallen a significant distance
        if (!mc.player.isOnGround() && mc.player.getY() < 3) {
            // Send a packet to prevent fall damage
            boolean horizontalCollision = mc.player.horizontalCollision;
            PlayerMoveC2SPacket.Full packet = new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY() + 0.000000001,
                mc.player.getZ(),
                mc.player.getYaw(),
                mc.player.getPitch(),
                false,
                horizontalCollision
            );
            mc.player.networkHandler.sendPacket(packet);
        }

        // Check for pistons pushing the player
        BlockPos playerPos = mc.player.getBlockPos();
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = playerPos.offset(direction);
            BlockState blockState = mc.world.getBlockState(adjacentPos);
            if (blockState.getBlock() == Blocks.PISTON || blockState.getBlock() == Blocks.STICKY_PISTON) {
                // Apply counteraction if the player is in the path of a piston
                applyCounteraction(direction);
            }
        }
    }

    private void applyCounteraction(Direction direction) {
        // Adjust the player's position to counteract the piston movement
        double offsetX = direction.getOffsetX() * 0.1;
        double offsetY = direction.getOffsetY() * 0.1;
        double offsetZ = direction.getOffsetZ() * 0.1;

        mc.player.setVelocity(mc.player.getVelocity().add(offsetX, offsetY, offsetZ));
    }
}
