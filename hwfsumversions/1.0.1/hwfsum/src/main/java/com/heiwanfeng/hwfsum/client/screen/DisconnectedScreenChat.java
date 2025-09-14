package com.heiwanfeng.hwfsum.client.screen;

import com.heiwanfeng.hwfsum.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "hwfsum", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DisconnectedScreenChat {
    
    private static EditBox chatInput;
    private static boolean chatVisible = false;
    private static final List<String> chatHistory = new ArrayList<>();
    private static final int MAX_HISTORY = 50;
    private static String lastServerInfo = "未知服务器";
    
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!ModConfig.ENABLE_DISCONNECTED_CHAT.get()) return;
        
        if (event.getScreen() instanceof DisconnectedScreen) {
            DisconnectedScreen disconnectedScreen = (DisconnectedScreen) event.getScreen();
            
            // 添加聊天输入框
            chatInput = new EditBox(
                Minecraft.getInstance().font,
                disconnectedScreen.width / 2 - 100,
                disconnectedScreen.height - 60,
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
        if (!ModConfig.ENABLE_DISCONNECTED_CHAT.get()) return;
        
        if (event.getScreen() instanceof DisconnectedScreen && chatVisible) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            DisconnectedScreen disconnectedScreen = (DisconnectedScreen) event.getScreen();
            
            // 渲染服务器信息
            guiGraphics.drawString(
                Minecraft.getInstance().font,
                "断开连接的服务器: " + lastServerInfo,
                disconnectedScreen.width / 2 - 100,
                disconnectedScreen.height - 100,
                0xFFFFFF
            );
            
            // 渲染聊天背景
            guiGraphics.fill(
                disconnectedScreen.width / 2 - 102,
                disconnectedScreen.height - 62,
                disconnectedScreen.width / 2 + 102,
                disconnectedScreen.height - 58,
                0x80000000
            );
            
            // 渲染提示文字
            if (chatInput.getValue().isEmpty() && !chatInput.isFocused()) {
                guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "按 T 键聊天（仅本地）",
                    disconnectedScreen.width / 2 - 95,
                    disconnectedScreen.height - 55,
                    0xAAAAAA
                );
            }
            
            // 渲染聊天历史
            int yOffset = disconnectedScreen.height - 85;
            for (int i = Math.max(0, chatHistory.size() - 5); i < chatHistory.size(); i++) {
                guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    chatHistory.get(i),
                    disconnectedScreen.width / 2 - 195,
                    yOffset,
                    0xFFFFFF
                );
                yOffset -= 12;
            }
        }
    }
    
    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (!ModConfig.ENABLE_DISCONNECTED_CHAT.get()) return;
        
        if (event.getScreen() instanceof DisconnectedScreen) {
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
                sendLocalChatMessage(chatInput.getValue());
                chatInput.setValue("");
                chatVisible = false;
                chatInput.setVisible(false);
                event.setCanceled(true);
            }
        }
    }
    
    private static void sendLocalChatMessage(String message) {
        if (!message.trim().isEmpty()) {
            String formattedMessage = "§7[本地] §f" + message.trim();
            chatHistory.add(formattedMessage);
            
            // 限制历史记录大小
            if (chatHistory.size() > MAX_HISTORY) {
                chatHistory.remove(0);
            }
            
            // 在本地显示消息
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal(formattedMessage));
        }
    }
    
    // 清空聊天历史
    public static void clearChatHistory() {
        chatHistory.clear();
    }
    
    // 设置服务器信息
    public static void setLastServerInfo(String info) {
        lastServerInfo = info;
    }
}