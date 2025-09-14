package com.heiwanfeng.hwfsum.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class OfflinePlayerManager {
    
    // 需要从其他地方获取服务器实例，例如通过参数传递
    public static boolean modifyOfflinePlayer(MinecraftServer server, String playerName, String operation, String value) {
        try {
            if (server == null) return false;
            
            // 获取玩家数据目录
            File playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).toFile();
            File[] playerFiles = playerDataDir.listFiles((dir, name) -> name.endsWith(".dat"));
            
            if (playerFiles == null) return false;
            
            for (File playerFile : playerFiles) {
                CompoundTag playerData = NbtIo.readCompressed(playerFile);
                
                if (playerData != null && playerData.contains("bukkit") && playerData.getCompound("bukkit").contains("lastKnownName")) {
                    String name = playerData.getCompound("bukkit").getString("lastKnownName");
                    
                    if (name.equalsIgnoreCase(playerName)) {
                        // 根据操作类型修改玩家数据
                        switch (operation.toLowerCase()) {
                            case "health":
                                playerData.putFloat("Health", Float.parseFloat(value));
                                break;
                            case "food":
                                playerData.putInt("foodLevel", Integer.parseInt(value));
                                break;
                            case "xp":
                                playerData.putInt("XpLevel", Integer.parseInt(value));
                                break;
                            case "gamemode":
                                playerData.putInt("playerGameType", Integer.parseInt(value));
                                break;
                            case "position":
                                String[] coords = value.split(" ");
                                if (coords.length == 3) {
                                    playerData.putDouble("PosX", Double.parseDouble(coords[0]));
                                    playerData.putDouble("PosY", Double.parseDouble(coords[1]));
                                    playerData.putDouble("PosZ", Double.parseDouble(coords[2]));
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown operation: " + operation);
                        }
                        
                        NbtIo.writeCompressed(playerData, playerFile);
                        return true;
                    }
                }
            }
            
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static UUID getUUIDFromName(MinecraftServer server, String playerName) {
        try {
            if (server != null) {
                return server.getProfileCache().get(playerName).orElseThrow().getId();
            }
        } catch (Exception e) {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        }
        return null;
    }
}