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

package me.luizotavio.minecraft.listener;

import me.luizotavio.minecraft.controller.ClickController;
import me.luizotavio.minecraft.event.impl.PlayerChangeClickingStateEvent;
import me.luizotavio.minecraft.pojo.ClickPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.luizotavio.minecraft.util.Colors.translate;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class BukkitClickHandler implements Listener {

    private final ClickController clickController;

    public BukkitClickHandler(ClickController clickController) {
        this.clickController = clickController;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location to = event.getTo(),
            from = event.getFrom();
        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        ClickPlayer clickPlayer = clickController.get(player.getUniqueId());

        if (clickPlayer == null) {
            return;
        }

        if (clickPlayer.isClicking()) {
            player.sendMessage(
                translate("&cYou cannot move while you are clicking!")
            );

            event.setTo(from);

            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ClickPlayer clickPlayer = clickController.get(player.getUniqueId());

        if (clickPlayer == null) {
            clickPlayer = new ClickPlayer(player.getUniqueId());

            clickController.put(player.getUniqueId(), clickPlayer);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ClickPlayer clickPlayer = clickController.get(player.getUniqueId());

        if(!clickPlayer.isClicking()) {
            return;
        }

        clickPlayer.setClicking(false);
    }

}
