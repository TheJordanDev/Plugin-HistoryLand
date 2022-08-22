package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.LanguageChooserCommand;
import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.helper.WGHelper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.common.Translator;
import fr.thejordan.historyland.object.language.Language;
import fr.thejordan.historyland.object.language.LanguageConfig;
import fr.thejordan.historyland.object.language.PhrasesPerRegionsConfig;
import fr.thejordan.historyland.object.language.SpokenLanguageConfig;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;

import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageManager extends AbstractManager {

    private static LanguageManager instance;
    public static LanguageManager instance() { return instance; }

    @Getter private final LanguageConfig languageConfig;
    @Getter private final SpokenLanguageConfig spokenLanguageConfig;
    @Getter private final PhrasesPerRegionsConfig phrasesPerRegionsConfig;

    private final Map<String, Language> languageMap = new HashMap<>();
    public Map<String, Language> languages() { return languageMap; }

    private final Map<UUID, String> spokenLanguages = new HashMap<>();

    private final Map<String, List<String>> phrasesPerRegions = new HashMap<>();

    private final List<String> phrases = new ArrayList<>();
    public List<String> phrases() { return phrases; }


    public LanguageManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        languageConfig = new LanguageConfig(plugin);
        spokenLanguageConfig = new SpokenLanguageConfig(plugin);
        phrasesPerRegionsConfig = new PhrasesPerRegionsConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        createDefaultLangIfNone();
        languageConfig.load().forEach(this::registerLanguage);
        spokenLanguageConfig.load().forEach(this::registerSpokenLanguage);
        phrasesPerRegionsConfig.load().forEach(this::registerRegions);
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("LANG");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Language language = LanguageManager.instance().getLanguage(player);
            objective.getScore(player.getName()).setScore(language.getObjective_value());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        spokenLanguageConfig.save(spokenLanguages);
    }

    @Override
    public void onReload() {
        languageMap.clear();
        phrasesPerRegions.clear();
        createDefaultLangIfNone();
        languageConfig.load().forEach(this::registerLanguage);
        phrasesPerRegionsConfig.load().forEach(this::registerRegions);
    }

    public void createDefaultLangIfNone() {
        InputStreamReader defaultLanguageFile = FileHelper.getStreamFromResource("lang.yml");
        File defaultLanguageInFile = new File(plugin().getDataFolder(),"/languages/default.yml");
        if (defaultLanguageFile != null && !defaultLanguageInFile.exists()) {
            try { YamlConfiguration.loadConfiguration(defaultLanguageFile).save(defaultLanguageInFile); }
            catch (Exception ignored) {}
        }
    }
    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(
                new LanguageChooserCommand()
        );
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Language language = LanguageManager.instance().getLanguage(event.getPlayer());
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("LANG");
        objective.getScore(event.getPlayer().getName()).setScore(language.getObjective_value());
    }

    public void broadcastMessage(BCLanguage language, BCType type, String message) {
        List<Player> targets;
        if (language == BCLanguage.ALL) targets = new ArrayList<>(Bukkit.getOnlinePlayers());
        else {
            if (language.target==null) targets = new ArrayList<>    (Bukkit.getOnlinePlayers());
            else targets = Bukkit.getOnlinePlayers().stream().filter(p -> getLanguage(p) == language.target).collect(Collectors.toList());
        }
        if (message.startsWith("%") && message.endsWith("%")) { //Envoie d'une phrase
            String key = message.substring(1, message.length() - 1);
            WGHelper.getPlayersInRegionOfCode(key,targets).forEach(p -> {
                if (!Translator.canTranslate(p,key)) return;
                String sent = ChatColor.translateAlternateColorCodes('&', Translator.translate(p, key));
                if (sent.isEmpty()) return;
                if (type == BCType.CHAT) p.sendMessage(sent);
                else if (type == BCType.ACTION) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(sent));
            });
        } else { //Envoie d'un message simple
            String sent = ChatColor.translateAlternateColorCodes('&', message);
            targets.forEach(p -> {
                if (type == BCType.CHAT)
                    p.sendMessage(sent);
                else if (type == BCType.ACTION)
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(sent));
            });
        }
    }

    public Optional<Language> getLanguage(String id) {
        return Optional.ofNullable(languageMap.get(id));
    }

    public Optional<Language> getLanguageFromName(String name) {
        return languageMap.values().stream().filter(l -> l.getName().equalsIgnoreCase(name)).findFirst();
    }
    public Language getLanguage(Player player) {
        if (!this.spokenLanguages.containsKey(player.getUniqueId())) this.spokenLanguages.put(player.getUniqueId(), "default");
        return languageMap.getOrDefault(this.spokenLanguages.get(player.getUniqueId()), languageMap.get("default"));
    }
    public void setLanguage(Player player, Language language) {
        this.spokenLanguages.put(player.getUniqueId(), language.getId());
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("LANG");
        objective.getScore(player.getName()).setScore(language.getObjective_value());
    }
    public Optional<String> getRegionOfKey(String key) {
        return phrasesPerRegions.keySet().stream().filter(k -> phrasesPerRegions.get(k).contains(key)).findFirst();
    }
    public void registerLanguage(Language language) {
        languageMap.put(language.id(), language);
        language.messages().keySet().forEach(key -> { if (!phrases.contains(key)) phrases.add(key); });
    }
    private void registerSpokenLanguage(UUID uuid, String language) {
        spokenLanguages.put(uuid, language);
    }
    private void registerRegions(String region, List<String> keys) {
        phrasesPerRegions.put(region, keys);
    }

    public enum BCLanguage {
        ALL,
        SET;
        @Getter private Language target;
        public BCLanguage target(Language language) { this.target = language; return this; }

    }
    public enum BCType { CHAT, ACTION }

}
