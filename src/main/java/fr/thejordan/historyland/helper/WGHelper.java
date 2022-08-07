package fr.thejordan.historyland.helper;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.thejordan.historyland.manager.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WGHelper {

    public static List<Player> getPlayersInRegionOfCode(String code) {
        return getPlayersInRegionOfCode(code, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public static List<Player> getPlayersInRegionOfCode(String code, List<Player> included) {
        Optional<String> _region = LanguageManager.instance().getRegionOfKey(code);
        if (_region.isEmpty()) return included;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        List<Player> returned = new ArrayList<>();

        RegionQuery query = container.createQuery();
        String region = _region.get();
        for (Player player: included) {
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
            Optional<ProtectedRegion> selectedRegion = regions.getRegions().stream().filter(r -> r.getId().equals(region)).findFirst();
            if (selectedRegion.isPresent()) returned.add(player);
        }
        return returned;
    }

}
