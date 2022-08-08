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

package me.luizotavio.minecraft.event.impl;

import me.luizotavio.minecraft.event.ClickEvent;
import me.luizotavio.minecraft.pojo.ClickPlayer;
import org.bukkit.entity.Player;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class PlayerChangeClickingStateEvent extends ClickEvent {

    private final Player player;
    private final ClickPlayer clickPlayer;

    private boolean clicking;

    public PlayerChangeClickingStateEvent(Player player, ClickPlayer clickPlayer, boolean clicking) {
        this.player = player;
        this.clickPlayer = clickPlayer;
        this.clicking = clicking;
    }

    public Player getPlayer() {
        return player;
    }

    public ClickPlayer getClickPlayer() {
        return clickPlayer;
    }

    public boolean isClicking() {
        return clicking;
    }

    public void setClicking(boolean clicking) {
        this.clicking = clicking;
    }
}
