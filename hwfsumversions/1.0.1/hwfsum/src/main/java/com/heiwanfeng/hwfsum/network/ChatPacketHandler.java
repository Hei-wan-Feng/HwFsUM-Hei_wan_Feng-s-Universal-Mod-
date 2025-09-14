package com.heiwanfeng.hwfsum.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ChatPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(com.heiwanfeng.hwfsum.HWFUniversalMod.MOD_ID + ":chat"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    private static int packetId = 0;
    
    public static void register() {
        INSTANCE.registerMessage(packetId++, DeathChatMessage.class,
            DeathChatMessage::encode,
            DeathChatMessage::decode,
            DeathChatMessage::handle);
    }
    
    public static class DeathChatMessage {
        private final String message;
        
        public DeathChatMessage(String message) {
            this.message = message;
        }
        
        public static void encode(DeathChatMessage msg, FriendlyByteBuf buffer) {
            buffer.writeUtf(msg.message);
        }
        
        public static DeathChatMessage decode(FriendlyByteBuf buffer) {
            return new DeathChatMessage(buffer.readUtf());
        }
        
        public static void handle(DeathChatMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // 处理死亡界面的聊天消息
                net.minecraft.client.Minecraft.getInstance().gui.getChat()
                    .addMessage(net.minecraft.network.chat.Component.literal(msg.message));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}