package com.heiwanfeng.hwfsum.mixin;

import com.heiwanfeng.hwfsum.HWFUniversalMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    
    private long lastTickTime = 0;
    
    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    private void onTickServer(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        
        if (HWFUniversalMod.IS_TICK_FROZEN) {
            ci.cancel();
            return;
        }
        
        // 控制刻速率
        long currentTime = System.nanoTime();
        long nanoSecondsPerTick = (long) (1000000000 / HWFUniversalMod.SERVER_TICK_RATE);
        
        if (lastTickTime != 0) {
            long elapsed = currentTime - lastTickTime;
            if (elapsed < nanoSecondsPerTick) {
                try {
                    Thread.sleep((nanoSecondsPerTick - elapsed) / 1000000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        lastTickTime = System.nanoTime();
    }
}