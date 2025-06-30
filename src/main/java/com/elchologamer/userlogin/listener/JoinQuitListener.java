package com.elchologamer.userlogin.listener;

import com.elchologamer.userlogin.ULPlayer;
import com.elchologamer.userlogin.UserLogin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// ✅ รองรับ Floodgate (Bedrock player)
import org.geysermc.floodgate.api.FloodgateApi;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 🔒 ปิดข้อความ Join หากตั้งไว้
        if (!UserLogin.getPlugin().getConfig().getBoolean("vanillaJoinMessages",
                !UserLogin.getPlugin().getConfig().getBoolean("disableVanillaJoinMessages", true))) {
            event.setJoinMessage(null);
        }

        // ✅ ตรวจสอบ UUID ว่าเป็น UUID ปลอมหรือไม่
        if (player.getUniqueId().toString().startsWith("000000")) {
            // ✅ ถ้าเป็นผู้เล่นจาก Floodgate (Bedrock) → อนุญาตให้เข้าได้
            if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
                player.kick(Component.text("§cไม่สามารถยืนยันตัวตนได้ (UUID ไม่ถูกต้อง - อาจเกิดจากการปลอมชื่อผ่าน Proxy)"));
                Bukkit.getLogger().warning("[UserLogin] ปฏิเสธผู้เล่น UUID ไม่ถูกต้อง: " + player.getName() + " (" + player.getUniqueId() + ")");
                return;
            }
        }

        // ✅ เรียกใช้งานระบบ login ตามปกติ
        ULPlayer.get(player).onJoin(false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ULPlayer.get(event.getPlayer()).onQuit();
    }
}
