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

package me.luizotavio.minecraft.factory;

import me.luizotavio.minecraft.util.Nearby;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class AnimationPacketFactory {

    /**
     * Create an entire packet of animation with specific id and entity.
     *
     * @param entity The entity to send the packet.
     * @param type   The animation type. [0 - swing arm, 1 - take damage]
     * @return The packet.
     */
    public static PacketPlayOutAnimation createAnimation(Entity entity, int type) {
        return new PacketPlayOutAnimation(
            ((CraftEntity) entity).getHandle(),
            type
        );
    }

    /**
     * Notify all players in the world of the packet.
     * @param packet The packet to send.
     * @param entity The entity to check nearby players.
     * @param radius The radius to check.
     */
    public static void notifyNearby(Packet<?> packet, Entity entity, int radius) {
        for (Entity player : Nearby.getNearbyEntities(entity, radius, EntityType.PLAYER)) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

}
