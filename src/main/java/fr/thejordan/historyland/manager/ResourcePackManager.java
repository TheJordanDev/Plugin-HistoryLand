package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.ResourcePackCommand;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.Translator;
import fr.thejordan.historyland.object.resourcepack.BypassResourcePackConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class ResourcePackManager extends AbstractManager {

    private static ResourcePackManager instance;
    public static ResourcePackManager instance() { return instance; }

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
    public void onDisable() {
        super.onDisable();
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
    }

    public boolean sendPack(Player player, boolean checkBypass) {
        if (checkBypass && this.bypassList.contains(player.getUniqueId()))
            player.sendMessage(Translator.translate(player,"resourcepack_bypassed"));
        else {
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
            case SUCCESSFULLY_LOADED -> player.sendMessage(Translator.translate(player,"resourcepack_loaded"));
            case FAILED_DOWNLOAD -> player.sendMessage(Translator.translate(player,"resourcepack_failed"));
            case DECLINED -> player.sendMessage(Translator.translate(player,"resourcepack_declined"));
        }
    }


}
