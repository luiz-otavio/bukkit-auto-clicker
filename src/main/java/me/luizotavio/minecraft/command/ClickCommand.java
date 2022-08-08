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

package me.luizotavio.minecraft.command;

import me.luizotavio.minecraft.controller.ClickController;
import me.luizotavio.minecraft.pojo.ClickPlayer;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import static me.luizotavio.minecraft.util.Colors.translate;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class ClickCommand {

    private final ClickController clickController;

    public ClickCommand(ClickController clickController) {
        this.clickController = clickController;
    }

    @Command(
        name = "autoclick",
        async = true,
        aliases = {"ac"},
        target = CommandTarget.PLAYER
    )
    public void handleAutoClickCommand(Context<Player> context) {
        Player player = context.getSender();

        ClickPlayer clickPlayer = clickController.get(player.getUniqueId());

        if (clickPlayer == null) {
            player.sendMessage(
                translate("&cYou are not registered to use this command!")
            );

            return;
        }

        if (clickPlayer.isClicking()) {
            clickPlayer.setClicking(false);
            player.sendMessage(
                translate("&aYou are no longer clicking!")
            );
        } else {
            clickPlayer.setClicking(true);
            player.sendMessage(
                translate("&aYou are now clicking!")
            );
        }
    }

}
