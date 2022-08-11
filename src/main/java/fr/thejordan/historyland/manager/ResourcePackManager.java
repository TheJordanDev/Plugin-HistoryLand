package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.command.ResourcePackCommand;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.Translator;
import fr.thejordan.historyland.object.resourcepack.BypassResourcePackConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ResourcePackManager extends AbstractManager {

    private static ResourcePackManager instance;
    public static ResourcePackManager instance() { return instance; }

    private Map<UUID, String> joinRequest = new HashMap<>();
    public Map<UUID, String> getJoinRequest() { return joinRequest; }

    private List<UUID> kickedForResourcepack = new ArrayList<>();
    public List<UUID> getKickedForResourcepack() { return kickedForResourcepack; }

    private List<UUID> bypassList;
    public List<UUID> getBypassList() { return bypassList; }

    private final BypassResourcePackConfig bypassResourcePackConfig;
    public BypassResourcePackConfig getBypassResourcePackConfig() { return bypassResourcePackConfig; }

    public ResourcePackManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.bypassResourcePackConfig = new BypassResourcePackConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.bypassList = this.bypassResourcePackConfig.load();
    }

    @Override
    public void onReload() {
        super.onReload();
        this.bypassList = this.bypassResourcePackConfig.load();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Historyland.log("ONDISABLE");
        this.bypassResourcePackConfig.save(getBypassList());
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new ResourcePackCommand());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendPack(player, true);
        if (bypassList.contains(player.getUniqueId()))
            event.setJoinMessage("§6[§r§eHistory Land§l§6] §r§fBienvenue "+player.getName());
        else {
            joinRequest.put(player.getUniqueId(), (player.hasPlayedBefore()) ? "welcome_message" : "first_join_welcome_message");
            event.setJoinMessage("");
        }
    }

    public boolean sendPack(Player player, boolean checkBypass) {
        if (checkBypass && this.bypassList.contains(player.getUniqueId())) {
            Historyland.log("[+] " + player.getName());
            player.sendMessage(Translator.translate(player, "resourcepack_bypassed"));
        } else {
            String url = MainManager.instance().data().resourcePackUrl();
            if (url.isBlank() || Helper.isStringURL(url).isEmpty())
                player.sendMessage(Translator.translate(player,"resourcepack_not_valid"));
            else {
                player.setResourcePack(url);
                player.sendMessage(Translator.translate(player, "resourcepack_sent"));
            }
        }
        return true;
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        switch (status) {
            case ACCEPTED -> player.sendMessage(Translator.translate(player,"resourcepack_accepted"));
            case SUCCESSFULLY_LOADED -> {
                player.sendMessage(Translator.translate(player,"resourcepack_loaded"));
                if (joinRequest.containsKey(player.getUniqueId())) {
                    String message = joinRequest.remove(player.getUniqueId());
                    Translator.broadcast(message, Map.of("player", player.getName()),Collections.emptyList());
                    Historyland.log("[+] "+player.getName());
                }
            }
            case FAILED_DOWNLOAD -> {
                if (bypassList.contains(player.getUniqueId()) || player.isOp()) player.sendMessage(Translator.translate(player, "resourcepack_failed"));
                else {
                    kickedForResourcepack.add(player.getUniqueId());
                    player.kickPlayer(Translator.translate(player, "resourcepack_failed"));
                }
            }
            case DECLINED -> {
                if (bypassList.contains(player.getUniqueId()) || player.isOp()) player.sendMessage(Translator.translate(player, "resourcepack_declined"));
                else {
                    kickedForResourcepack.add(player.getUniqueId());
                    player.kickPlayer(Translator.translate(player, "resourcepack_declined"));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        if (!kickedForResourcepack.contains(player.getUniqueId())) {
            Translator.broadcast("leave_message", Map.of("player", player.getName()), Collections.emptyList());
            Historyland.log("[-] " + player.getName());
        }
        kickedForResourcepack.remove(player.getUniqueId());
    }

    public void setPack(String s) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getBypassList().contains(player.getUniqueId()))
                Translator.send(player, "resourcepack_updated_bypass");
            else {
                Translator.send(player, "resourcepack_updated");
                Helper.runnable(()-> player.setResourcePack(s)).runTaskLater(Historyland.instance(),20L);
            }
        }
    }
}
