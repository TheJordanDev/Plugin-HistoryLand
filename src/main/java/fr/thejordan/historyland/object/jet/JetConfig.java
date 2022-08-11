package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.object.common.AbstractConfigFolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class JetConfig extends AbstractConfigFolder<JetCategory> {

    public JetConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File folder() {
        return new File(getPlugin().getDataFolder(),"jets");
    }

    @Override
    public boolean filter(File dir, String name) {
        return name.endsWith(".yml");
    }

    @Override
    public List<String> blacklist() {
        return null;
    }

    @Override
    public String name() {
        return "Jets Config";
    }

    @Override
    public Function<YamlConfiguration, JetCategory> loadProcess() {
        return configuration -> {
            String id = configuration.getString("id");
            Map<String,Jet> jets = new HashMap<>();
            ConfigurationSection jetSection = configuration.getConfigurationSection("jets");
            for (String name : jetSection.getKeys(false)) {
                ConfigurationSection section = jetSection.getConfigurationSection(name);
                Vector vector = section.getVector("vector");
                Location location = section.getLocation("location");
                Material material = Material.valueOf(section.getString("material", "GRASS_BLOCK"));
                Double range = section.getDouble("range", 5D);
                Jet jet = new Jet(id, name, location, vector, material);
                jet.setActivationRange(range);
                jets.put(name,jet);
            }
            return new JetCategory(id,jets);
        };
    }

    @Override
    public Consumer<List<JetCategory>> saveProcess() {
        return jets -> {
            for (JetCategory category : jets) {
                File file = new File(folder(),category.getId()+".yml");
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                YamlConfiguration configuration = new YamlConfiguration();
                configuration.set("id",category.getId());
                ConfigurationSection jetsSection = configuration.createSection("jets");
                for (Jet jet : category.getJets().values()) {
                    ConfigurationSection jetSection = jetsSection.createSection(jet.getName());
                    jetSection.set("vector", jet.getVector());
                    jetSection.set("location", jet.getLocation());
                    jetSection.set("material", jet.getMaterial().name());
                    jetSection.set("range", jet.getActivationRange());
                }
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
