package com.heiwanfeng.hwfsum.screen;

import com.heiwanfeng.hwfsum.HWFUniversalMod;
import com.heiwanfeng.hwfsum.config.ModConfig;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModConfigScreen extends Screen {
    private final Screen parentScreen;
    
    public ModConfigScreen(Screen parent) {
        super(GameNarrator.NO_TITLE);
        this.parentScreen = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // 死亡聊天开关
        this.addRenderableWidget(Button.builder(
            Component.literal(ModConfig.ENABLE_DEATH_CHAT.get() ? "死亡界面聊天: 启用" : "死亡界面聊天: 禁用"), 
            button -> {
                ModConfig.ENABLE_DEATH_CHAT.set(!ModConfig.ENABLE_DEATH_CHAT.get());
                button.setMessage(Component.literal(
                    ModConfig.ENABLE_DEATH_CHAT.get() ? "死亡界面聊天: 启用" : "死亡界面聊天: 禁用"
                ));
            }
        ).bounds(this.width / 2 - 100, this.height / 6 + 24, 200, 20).build());
        
        // 断开连接聊天开关
        this.addRenderableWidget(Button.builder(
            Component.literal(ModConfig.ENABLE_DISCONNECTED_CHAT.get() ? "断开连接界面聊天: 启用" : "断开连接界面聊天: 禁用"), 
            button -> {
                ModConfig.ENABLE_DISCONNECTED_CHAT.set(!ModConfig.ENABLE_DISCONNECTED_CHAT.get());
                button.setMessage(Component.literal(
                    ModConfig.ENABLE_DISCONNECTED_CHAT.get() ? "断开连接界面聊天: 启用" : "断开连接界面聊天: 禁用"
                ));
            }
        ).bounds(this.width / 2 - 100, this.height / 6 + 48, 200, 20).build());
        
        // 快捷搭路开关
        this.addRenderableWidget(Button.builder(
            Component.literal(HWFUniversalMod.QUICK_BRIDGE_ENABLED ? "快捷搭路: 启用" : "快捷搭路: 禁用"), 
            button -> {
                HWFUniversalMod.QUICK_BRIDGE_ENABLED = !HWFUniversalMod.QUICK_BRIDGE_ENABLED;
                button.setMessage(Component.literal(
                    HWFUniversalMod.QUICK_BRIDGE_ENABLED ? "快捷搭路: 启用" : "快捷搭路: 禁用"
                ));
            }
        ).bounds(this.width / 2 - 100, this.height / 6 + 72, 200, 20).build());
        
        // 返回按钮
        this.addRenderableWidget(Button.builder(Component.literal("完成"), button -> {
            // 保存配置
            ModConfig.SPEC.save();
            this.minecraft.setScreen(this.parentScreen);
        }).bounds(this.width / 2 - 100, this.height / 6 + 168, 200, 20).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, Component.literal("Hei_wan_Feng's Universal Mod 设置"), this.width / 2, 15, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}