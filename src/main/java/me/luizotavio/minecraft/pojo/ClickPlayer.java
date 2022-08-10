/*
 * MIT License
 *
 * Copyright (c) [2022] [LUIZ O. F. CORRÊA]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.luizotavio.minecraft.pojo;

import me.luizotavio.minecraft.ClickPlugin;
import me.luizotavio.minecraft.event.impl.PlayerChangeClickingStateEvent;
import me.luizotavio.minecraft.event.impl.PlayerPerformsClickEvent;
import me.luizotavio.minecraft.factory.AnimationPacketFactory;
import me.luizotavio.minecraft.util.Blocks;
import me.luizotavio.minecraft.util.Multipliers;
import me.luizotavio.minecraft.util.Nearby;
import me.luizotavio.minecraft.util.Paralellism;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class ClickPlayer {

    private final UUID uniqueId;

    private boolean isClicking;

    public ClickPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public boolean isClicking() {
        return isClicking;
    }

    public void setClicking(boolean isClicking) {
        Player player = getPlayer();

        if (player != null) {
            PlayerChangeClickingStateEvent event = new PlayerChangeClickingStateEvent(player, this, isClicking).call();

            if (event.isCancelled()) {
                return;
            }

            if (event.isClicking() != isClicking) {
                return;
            }
        }

        this.isClicking = isClicking;
    }

    public void perform() {
        Player player = getPlayer();

        if (player == null) {
            setClicking(false);
            return;
        }

        List<LivingEntity> entities = Nearby.getNearbyEntities(player, ClickPlugin.RADIUS_OF_DISTANCE_TO_PERFORMS, null);

        if (entities.isEmpty()) {
            return;
        }

        final double damage = Multipliers.getCurrentDamage(player);

        Location vector = player.getLocation();
        World world = player.getWorld();

        int y = vector.getBlockY();

        for (LivingEntity entity : entities) {
            if (entity.getType() == EntityType.PLAYER) {
                continue;
            }

            Location entityVector = entity.getLocation();

            int maxX = Math.max(vector.getBlockX(), entityVector.getBlockX()),
                maxZ = Math.max(vector.getBlockZ(), entityVector.getBlockZ()),
                minX = Math.min(vector.getBlockX(), entityVector.getBlockX()),
                minZ = Math.min(vector.getBlockZ(), entityVector.getBlockZ());

            boolean hasBlock = false;

            // Check if there is a block between the player and the entity.
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (Blocks.existsBlocksAt(world, x, y, z ) || Blocks.existsBlocksAt(world, x, y + 1, z)) {
                        hasBlock = true;
                        break;
                    }
                }
            }

            if (hasBlock) {
                continue;
            }

            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
                player,
                entity,
                EntityDamageByEntityEvent.DamageCause.ENTITY_ATTACK,
                damage
            );

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                continue;
            }

            PlayerPerformsClickEvent clickEvent = new PlayerPerformsClickEvent(
                player,
                this,
                entity,
                event.getDamage()
            ).call();

            if (clickEvent.isCancelled()) {
                continue;
            }

            entity.setLastDamageCause(event);

            Paralellism.callToSync(() -> entity.damage(damage, player));
        }

        PacketPlayOutAnimation packet = AnimationPacketFactory.createAnimation(player, 0);

        // Notify packet to all players.
        AnimationPacketFactory.notifyNearby(packet, player, ClickPlugin.RADIUS_OF_DISTANCE_TO_PERFORMS);
    }
}
