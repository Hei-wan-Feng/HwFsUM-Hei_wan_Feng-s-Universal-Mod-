package com.heiwanfeng.hwfsum.client.screen;

import com.heiwanfeng.hwfsum.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "hwfsum", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathScreenChat {
    
    private static EditBox chatInput;
    private static boolean chatVisible = false;
    
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!ModConfig.ENABLE_DEATH_CHAT.get()) return;
        
        if (event.getScreen() instanceof DeathScreen) {
            DeathScreen deathScreen = (DeathScreen) event.getScreen();
            
            // 添加聊天输入框
            chatInput = new EditBox(
                Minecraft.getInstance().font,
                deathScreen.width / 2 - 100,
                deathScreen.height - 30,
                200, 20,
                Component.literal("在此输入聊天内容...")
            );
            
            chatInput.setMaxLength(256);
            chatInput.setVisible(false);
            
            event.addListener(chatInput);
        }
    }
    
    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!ModConfig.ENABLE_DEATH_CHAT.get()) return;
        
        if (event.getScreen() instanceof DeathScreen && chatVisible) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            DeathScreen deathScreen = (DeathScreen) event.getScreen();
            
            // 渲染聊天背景
            guiGraphics.fill(
                deathScreen.width / 2 - 102,
                deathScreen.height - 32,
                deathScreen.width / 2 + 102,
                deathScreen.height - 28,
                0x80000000
            );
            
            // 渲染提示文字
            if (chatInput.getValue().isEmpty() && !chatInput.isFocused()) {
                guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "按 T 键聊天",
                    deathScreen.width / 2 - 95,
                    deathScreen.height - 25,
                    0xAAAAAA
                );
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (!ModConfig.ENABLE_DEATH_CHAT.get()) return;
        
        if (event.getScreen() instanceof DeathScreen) {
            if (event.getKeyCode() == 84) { // T 键
                chatVisible = !chatVisible;
                chatInput.setVisible(chatVisible);
                
                if (chatVisible) {
                    chatInput.setFocused(true);
                    Minecraft.getInstance().mouseHandler.releaseMouse();
                    Minecraft.getInstance().screen.setFocused(chatInput);
                } else {
                    chatInput.setFocused(false);
                    Minecraft.getInstance().mouseHandler.grabMouse();
                }
                
                event.setCanceled(true);
            }
            
            // 回车键发送消息
            if (chatVisible && event.getKeyCode() == 257) { // 回车键
                sendChatMessage(chatInput.getValue());
                chatInput.setValue("");
                chatVisible = false;
                chatInput.setVisible(false);
                event.setCanceled(true);
            }
        }
    }
    
    private static void sendChatMessage(String message) {
        if (!message.trim().isEmpty()) {
            // 检查是否是命令
            if (message.startsWith("/")) {
                String command = message.substring(1).split(" ")[0].toLowerCase();
                List<? extends String> allowedCommands = ModConfig.ALLOWED_DEATH_COMMANDS.get();
                
                // 检查命令是否在白名单中
                if (allowedCommands.contains(command)) {
                    // 发送命令到服务器
                    Minecraft.getInstance().player.connection.sendCommand(message.substring(1));
                } else {
                    // 显示错误消息
                    Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("§c您不能在死亡界面执行此命令"),
                        false
                    );
                }
            } else {
                // 发送普通聊天消息
                Minecraft.getInstance().player.connection.sendChat(message.trim());
            }
        }
    }

    public static void hideChat() {
        chatVisible = false;
        if (chatInput != null) {
            chatInput.setVisible(false);
            chatInput.setFocused(false);
        }
    }
}