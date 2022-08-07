package fr.thejordan.historyland.object;

import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class AbstractPagedGUI extends AbstractGUI {

    @Getter private int page = 1;
    public void setPage(int page) {
        if (page < 1) return;
        this.page = page;
        refresh();
    }

    public AbstractPagedGUI(Player player) {
        super(player);
    }
}
