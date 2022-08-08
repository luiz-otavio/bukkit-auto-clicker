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

package me.luizotavio.minecraft;

import me.luizotavio.minecraft.command.ClickCommand;
import me.luizotavio.minecraft.controller.ClickController;
import me.luizotavio.minecraft.listener.BukkitClickHandler;
import me.luizotavio.minecraft.scheduler.ClickPerformScheduler;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Luiz Otávio de Farias Corrêa
 * @since 08/08/2022
 */
public class ClickPlugin extends JavaPlugin {

    public static final int RADIUS_OF_DISTANCE_TO_PERFORMS = 10;
    public static final int SCHEDULER_DELAY_IN_TICKS = 60;

    public static ClickPlugin getInstance() {
        return JavaPlugin.getPlugin(ClickPlugin.class);
    }

    private ClickController clickController;

    @Override
    public void onEnable() {
        clickController = new ClickController();

        getServer().getPluginManager().registerEvents(new BukkitClickHandler(clickController), this);

        getServer().getScheduler()
            .runTaskTimerAsynchronously(
                this,
                new ClickPerformScheduler(clickController),
                0,
                SCHEDULER_DELAY_IN_TICKS
            );

        BukkitFrame bukkitFrame = new BukkitFrame(this);

        bukkitFrame.registerCommands(new ClickCommand(clickController));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        getServer().getScheduler()
            .cancelTasks(this);

        clickController.clear();
    }

    public ClickController getClickController() {
        return clickController;
    }
}
