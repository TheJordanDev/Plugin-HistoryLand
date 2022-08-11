package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.manager.JetManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.List;

public class Jet {

    @Getter private final String id;
    @Getter private final String name;
    @Getter private final Material material;
    @Getter @Setter private Location location;
    @Getter @Setter private Vector vector;
    @Getter @Setter private boolean active = false;
    @Getter @Setter private Double activationRange = 10D;

    @Getter private JetTwinScheduler twinScheduler;

    public void setTwinScheduler(JetTwinScheduler twinScheduler) {
        if (this.twinScheduler != null && !this.twinScheduler.isCancelled()) this.twinScheduler.cancel();
        this.twinScheduler = twinScheduler;
    }

    public Jet(String id, String name, Location location, Vector originVector, Material material) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.vector = originVector;
        this.material = material;
    }

    public boolean arePlayersNearby(double ray) {
        if (location == null) return false;
        if (!location.getChunk().isLoaded()) return false;
        return !JetUtils.getNearbyEntities(location,ray,ray,ray, List.of(EntityType.PLAYER)).isEmpty();
    }

    public JetCategory parent() {
        return JetManager.instance().getJets().get(id);
    }

}
