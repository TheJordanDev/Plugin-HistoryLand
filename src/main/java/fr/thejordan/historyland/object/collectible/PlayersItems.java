package fr.thejordan.historyland.object.collectible;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

public record PlayersItems(@Getter UUID uuid, @Getter List<String> items) {

}
