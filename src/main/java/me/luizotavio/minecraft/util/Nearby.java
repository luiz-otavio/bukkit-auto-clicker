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

package me.luizotavio.minecraft.util;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.List;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 10/08/2022
 */
public class Nearby {

    public static <T extends Entity> List<T> getNearbyEntities(Entity from, int radius, Class<T> clazz) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) from).getHandle();

        List<net.minecraft.server.v1_8_R3.Entity> entities = nmsEntity.world.a(
            nmsEntity,
            nmsEntity.getBoundingBox().grow(radius, radius, radius),
            target -> target.getBukkitEntity().getClass().isAssignableFrom(clazz)
        );

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return (List<T>) Collections.unmodifiableList(
            Lists.transform(entities, entity -> entity.getBukkitEntity())
        );
    }
}
