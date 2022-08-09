package fr.thejordan.historyland.object;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.function.Function;

public class Skin {

    @Getter private final String texture;
    @Getter private final String signature;

    public Skin(String texture) {
        this.texture = texture;
        this.signature = "";
    }

    public Skin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public ItemStack asHead(Function<ItemStack, BItem> item) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull = item.apply(skull).stack();
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", getTexture()));
        return setSkinField(skull, meta, profile);
    }
    public static ItemStack setSkinField(ItemStack skull, SkullMeta meta, GameProfile profile) {
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

    public enum Skins {

        DEFAULT("ewogICJ0aW1lc3RhbXAiIDogMTY1OTkxMDQ0MjY5NywKICAicHJvZmlsZUlkIiA6ICI5MGM0NzZmMzhmZjM0ZDcyYmFlOTE4MjM0ODM2Yzk3ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVTa2ludGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ3ZmQxNzY1NjMwMzJkM2JlNDBkOTk5MWU0ZDFkMmFkYmJiNzg0MWM5YWUzNjM0YWNmZWM3NzM5ZWI0ZWMxOGUiCiAgICB9CiAgfQp9",
                "dexfbdVUqcu+mjrO7/HanyBDAZnbjackR/W0mrBsA5O8UFI5OcP1eXS3UCTjIMbG48VUrWNFU4DFQ003uwr2rMJu4Ch/Bukxg4VqdiycQ5OJITOXXnwrE5sVqIpRvYHMEKVSij7voGH4FDB1AZvgT26evkX09Wj6WIfgyzTUIIbT9ZT0iDVdxUsCzccCyeXoXeMwi8+PYqYFlQfqoIJtWheNRK4GAd7d5iWc+Rf8h3pN7uvNlBMmoXLIk3AARI1wvHp7LwnBXD87NL6AP3NVx5qcjNd0ySaRiNAFircSYt/dR/biNRZt7uTVdVcxjFTJyyED/xT3/zp9R3qLzA69sV+mClrlMcPpqTBop1MPm388llsojr/zcilYlP8Am5H7RoWgW/EBJH5GDYLjgthVM4CaCEiDHLTqJeg6o+sYm/CO7c2jOwCkBu731fAghxDQigGllZfmPMPc+dCzWAe8TLoI1619uKAnTtcDj1SFb3Sf/mBz2WQUdqUiO07LTE4wr061rJKKvFD/w/UfcqI8w08X8Eu4jq0MvWFUdwph8uiGUYOaTLQ79tMO9ZF2C0qhgzlx191O0waAuXtWWUjEuuBn2sZ7HvBd42hOMiG0HO60vE1tPLH0FGC9t9kKaRdNn0OMDKDBuEhqTCVA1//bASmuZ4HBhCnKGNn6ECola7A="),
        arrow_right("ewogICJ0aW1lc3RhbXAiIDogMTYwMzIyMTYxODExNCwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzcyZjg4MzM1MTk1ZDk5OTRkNzExYjkwMGRlMjUxYzllNTliYTU5NmZlNjQ0MWRmNDA5YTAyOGZhZDIzMmI1ZjYiCiAgICB9CiAgfQp9",
                "tBA4YrX+G6l5YIfi0StyxBy6r5MPTenlVGtyukMUBT1reeEYXcE4aElDXUtvNt3wRH8kLsOH6afMrlBw/xSNJUdBZNOJKtP9kfz6Fii3/ZGS4Jg+RABOE7oLA344N6032ac193O4AA9EeQRGnzjzEYDF4HJZXd69MSlz9Iy4GIp+C3Mk6yEHbKHxw4JR3lelx//qB3hDoh/o9ttVF9ig5yBxiQ4RCuVwSPjNYsQI29iXV3WYyXy/QnSQuQKIHCpg0iDrzzyAIgiHRP7JzmSvd4M4AVpkGL+akywRkrY4FNPlNtNFgBiVw9XXoHO5pPKZN+9yXp5LGAnsHOjZMwB+Lodslcz+YdUDFDuQ6XqsELXMH0BU/2JS1NAcswI7mjRT/wN1J8UPB5CcP3zC3ow9O4Gwt3e9RO37hN9Vsg2Rh17dvrBj7lus/dO74kZiX9oaXVgDIQ0Z/eVn691f/KyZOIoyazMK28Ob5P/e/l2aVBQfvZC9TXeUNq4JGo1JYglBXP0tzzct1pCXXkAbF0O2Frg6vXD/xTwrMhgXMe6a1CQwvjVj5iT4ETlp1P7hb4m6JP5ibsWWjWTmYaW6IIqnjIKKxYedThlyeDIuySMxGdOQ5a44Zi4XaAlYWTMM3cRgDleWWlXNmogmyWwuJ1PnRR/lalclVc8bYg8tb2BNepE="),
        arrow_left("ewogICJ0aW1lc3RhbXAiIDogMTYwMzIyMTU1ODA4OCwKICAicHJvZmlsZUlkIiA6ICI4MmM2MDZjNWM2NTI0Yjc5OGI5MWExMmQzYTYxNjk3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3ROb3RvcmlvdXNOZW1vIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYyZmZhOTg0MTVmZTVlMDE4MDE0NzIwNGI2MTg0YmQ3OWMxNjM2ODYyYjI2Nzc0MTM1YjM0NWFhNmYxNDU0NzkiCiAgICB9CiAgfQp9",
                "ppJM2ZMWvNsnjY1uy/yoo5DY7YBd7dXhxyDsZEUc/48MSb5MeoqSUhBekn1C3A8dDlDkdkwdcD7KRc4GTLDZnDVme3ewgmoo3Sck/6UZYH2oALVsD7BcWsxkHSFP1x13zbfXxs1t+WHZwNooeQVqWKBbnfHKHEAaVl2F2g/8A/OBuI/ExyN22AG3yHL41gxrwhpbYsMmrgwUR+8+FkQHovocX2TDBCR+NbZV7e/YjBk6cawfmXDISFBuudzsmZD6EoXOAPtMSP43Ao+Rp1ZFXLr7hcrM+U9gzt7FprXEPCcA6Nlr+ku9ioDw9rD2syAORaOw2XU+bxlAU9ZyFqglUi4749p1mMgRYbFKSwdiNuB7J9mUAtKWYVa3D1JSn3ATLjyOnJUxDAYNXZUrnt3zWKqEt43TRDiVq0U2zx5gfhhqtTfw3zB6uWrzoLs4pRLRfyupm43Z1lbnT53Shwt3mpZYgvblca9put0y9f5aZz0rTzwVbX6R/dNcWAM3uuv0ik8OXKzUfDiPBZu3Se2lcgzUSqbwZDDNRKErKO0k1EQqvVQtlwqj9QAkgp4NZ7TY6aOz9Ntht7+PnLElx8jcVDfe3u3s3fkU7nNfVLKP2AnWb1IIxLfzbGg2Wt+HkvjbcATgYQG3P9TgPPHPkUx6b3nrfSMkhh6ZwoQvj99/A6g="),
        arrow_backwards("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YTJkZDViZWYwYjA3M2IxMzI3MWE3ZWViOWNmZWE3YWZlODU5M2M1N2E5MzgyMWU0MzE3NTU3MjQ2MTgxMiJ9fX0=","");

        @Getter private final String texture;
        @Getter private final String signature;

        Skins(String texture, String signature) {
            this.texture = texture;
            this.signature = signature;
        }

        public Skin asSkin() {
            return new Skin(texture, signature);
        }

    }

}
