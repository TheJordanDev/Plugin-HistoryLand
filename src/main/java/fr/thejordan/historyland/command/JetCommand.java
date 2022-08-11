package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.JetManager;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.ValueWError;
import fr.thejordan.historyland.object.jet.Jet;
import fr.thejordan.historyland.object.jet.JetCategory;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JetCommand extends AbstractCommand {

    @Override
    public String id() {
        return "jet";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String permission() {
        return "historyland.jet";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    sendJetPage(player, 1);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (Helper.isInt(args[1])) {
                        int page = Integer.parseInt(args[1]);
                        return sendJetPage(player, page);
                    } else {
                        if (!JetManager.instance().getJets().containsKey(args[1]))
                            return sendMessageT(player, "§cLe groupe " + args[1] + " n'éxiste pas !");
                        JetCategory category = JetManager.instance().getJets().get(args[1]);
                        return sendCategoryInfo(player, category);
                    }
                } else if (args[0].equalsIgnoreCase("tphere")) {
                    ValueWError value = JetManager.instance().getJet(args[1]);
                    if (value.isNullWithError()) return sendMessageT(commandSender, value.msg());
                    if (value.value() instanceof Jet jet) {
                        jet.setLocation(player.getLocation());
                        return sendMessageT(commandSender, "§eJet "+jet.getName()+" téléporté avec succès !");
                    } else if (value.value() instanceof JetCategory){
                        return sendMessageT(commandSender, "§cVeuillez choisir un jet et pas une catégorie");
                    }
                } else if (args[0].equalsIgnoreCase("tpto")) {
                    ValueWError value = JetManager.instance().getJet(args[1]);
                    if (value.isNullWithError()) return sendMessageT(commandSender, value.msg());
                    if (value.value() instanceof Jet jet) {
                        player.teleport(jet.getLocation());
                        return sendMessageT(commandSender, "§eTéléportation au jet "+jet.getName()+" réussis !");
                    } else if (value.value() instanceof JetCategory){
                        return sendMessageT(commandSender, "§cVeuillez choisir un jet et pas une catégorie");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    ValueWError value = JetManager.instance().getJet(args[1]);
                    if (value.isNullWithError()) return sendMessageT(commandSender, value.msg());
                    if (value.value() instanceof Jet jet) {
                        jet.parent().remove(jet);
                        return sendMessageT(commandSender, "§eJet "+jet.getName()+" supprimé !");
                    } else if (value.value() instanceof JetCategory cat){
                        JetManager.instance().remove(cat);
                        return sendMessageT(commandSender, "§eCatégorie "+cat.getId()+" supprimée");
                    }
                }
            }
        }
        if (args.length >= 3 && args.length <= 4) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!args[1].contains(":") || args[1].split(":").length != 2) return sendMessageT(commandSender,"§cLe nom doit être dans ce format: id:name !");
                if (!Helper.isStringVector(args[2])) return sendMessageT(commandSender, "§cLe vecteur doit être du format: [x;y;z] !");
                String id = args[1].split(":")[0];
                String name = args[1].split(":")[1];
                Vector vector = Helper.vectorFromString(args[2]);
                Material material;
                if (args.length == 4) material = ((Helper.isValueOfEnum(Material.class,args[3].toUpperCase()).isPresent())? Material.valueOf(args[3].toUpperCase()) : Material.BLUE_CONCRETE);
                else material = Material.BLUE_CONCRETE;
                Location location = Helper.getSendersLocation(commandSender);
                JetManager.instance().create(id,name,location,vector,material);

                if (commandSender instanceof Player player) {
                    ComponentBuilder builder = new ComponentBuilder("§eJet créé !");
                    Helper.hoverMessage(builder, Collections.singletonList("§6" + id + ":" + name));
                    player.spigot().sendMessage(builder.create());
                } else {
                    commandSender.sendMessage("Jet créé !");
                }
                return true;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setRange")) {
                ValueWError value = JetManager.instance().getJet(args[1]);
                if (value.isNullWithError()) return sendMessageT(commandSender, value.msg());
                if (!Helper.isDouble(args[2])) return sendMessageT(commandSender, "§cLa range doit être un décimal !");
                double range = Double.parseDouble(args[2]);
                if (value.value() instanceof Jet jet) {
                    jet.setActivationRange(range);
                    return sendMessageT(commandSender, "§eRange pour "+jet.getName()+" définie à "+range+" !");
                } else if (value.value() instanceof JetCategory cat){
                    cat.setRange(range);
                    return sendMessageT(commandSender, "§eRange pour "+cat.getId()+" définie à "+range+" !");
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("move")) {
                ValueWError obj = JetManager.instance().getJet(args[1]);
                if (obj.isNullWithError()) return sendMessageT(commandSender, obj.msg());
                if (obj.value() instanceof JetCategory category) {
                    if (!Helper.isStringVector(args[2])) return sendMessageT(commandSender, "§cLe vecteur doit être du format: [x;y;z] !");
                    if (!Helper.isInt(args[3])) return sendMessageT(commandSender, "§cLe temps doit être un nombre entier !");
                    Vector vector = Helper.vectorFromString(args[2]);
                    Integer seconds = Integer.parseInt(args[3]);
                    for (Jet jet : category.getJets().values()) {
                        JetManager.instance().move(jet,vector.clone(),seconds);
                    }
                    return sendMessageT(commandSender, "§aJet(s) en mouvement !");
                } else if (obj.value() instanceof Jet jet) {
                    if (!Helper.isStringVector(args[2])) return sendMessageT(commandSender, "§cLe vecteur doit être du format: [x;y;z] !");
                    if (!Helper.isInt(args[3])) return sendMessageT(commandSender, "§cLe temps doit être un nombre entier !");
                    Vector vector = Helper.vectorFromString(args[2]);
                    Integer seconds = Integer.parseInt(args[3]);
                    JetManager.instance().move(jet,vector.clone(),seconds);
                    sendMessageT(commandSender, "§aJet en mouvement !");
                    return true;
                }
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!args[1].contains(":") || args[1].split(":").length != 2) return sendMessageT(commandSender,"§cLe nom doit être dans ce format: id:name !");
                if (!Helper.isStringLocation(args[2])) return sendMessageT(commandSender,"§cLa position doit être du format: x;y;z;monde !");
                if (!Helper.isStringVector(args[3])) return sendMessageT(commandSender, "§cLe vecteur doit être du format: [x;y;z] !");
                String id = args[1].split(":")[0];
                String name = args[1].split(":")[1];
                Vector vector = Helper.vectorFromString(args[3]);
                Material material;
                if (args.length == 5) material = ((Helper.isValueOfEnum(Material.class,args[4].toUpperCase()).isPresent())? Material.valueOf(args[4].toUpperCase()) : Material.BLUE_CONCRETE);
                else material = Material.BLUE_CONCRETE;
                Location location = Helper.locationFromString(args[2],Helper.getSendersLocation(commandSender).getWorld());
                JetManager.instance().create(id,name,location,vector,material);

                if (commandSender instanceof Player player) {
                    ComponentBuilder builder = new ComponentBuilder("§eJet créé !");
                    Helper.hoverMessage(builder, Collections.singletonList("§6" + id + ":" + name));
                    player.spigot().sendMessage(builder.create());
                } else {
                    commandSender.sendMessage("Jet créé !");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> returned = new ArrayList<>();
        if (args.length == 1) returned.addAll(Helper.autocomplete(args[0], Arrays.asList("create","move","list","remove","tpto","tphere","setRange")));
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                int separator =  StringUtils.countMatches(args[1],":");
                if (separator == 0) returned.addAll(Helper.autocomplete(args[1], new ArrayList<>(JetManager.instance().getJets().keySet())));
                else if (separator == 1) {
                    String id = args[1].split(":")[0];
                    if (!JetManager.instance().getJets().containsKey(id)) return new ArrayList<>();
                    returned.add(args[1]);
                }
            } else if (args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("tphere") || args[0].equalsIgnoreCase("tpto") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("setRange")) {
                int separator = StringUtils.countMatches(args[1],":");
                if (separator == 0) returned.addAll(Helper.autocomplete(args[1], new ArrayList<>(JetManager.instance().getJets().keySet())));
                else if (separator == 1) {
                    String id = args[1].substring(0,args[1].indexOf(':'));
                    if (!JetManager.instance().getJets().containsKey(id)) return new ArrayList<>();
                    JetCategory category = JetManager.instance().getJets().get(id);
                    List<String> names = category.getJets().values().stream().map(j->j.getId()+":"+j.getName()).collect(Collectors.toList());
                    returned.addAll(Helper.autocomplete(args[1],names));
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                returned.addAll(Arrays.asList("[0.0;0.0;0.0]","0.0;0.0;0.0;world"));
            } else if (args[0].equalsIgnoreCase("move")){
                returned.add("[0.0;0.0;0.0]");
            } else if (args[0].equalsIgnoreCase("setRange")){
                returned.add("décimal");
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                if (Helper.isStringVector(args[2])) {
                    returned.addAll(Helper.autocomplete(args[3], Stream.of(Material.values()).filter(Material::isBlock).map(Enum::name).collect(Collectors.toList())));
                } else if (Helper.isStringLocation(args[2])) {
                    returned.add("[0.0;0.0;0.0]");
                }
            } else if (args[0].equalsIgnoreCase("move")){
                returned.add("secondes");
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create") && Helper.isStringLocation(args[2]) && Helper.isStringVector(args[3])) {
                returned.addAll(Helper.autocomplete(args[4], Stream.of(Material.values()).filter(Material::isBlock).map(Enum::name).collect(Collectors.toList())));
            }
        }
        return returned;
    }

    public boolean sendJetPage(Player player, int page) {
        if (JetManager.instance().getJets().size() == 0) return sendMessageT(player, "§cAucun jet n'éxiste !");
        if (page <= 0) return sendJetPage(player, 1);
        if (page > JetManager.instance().getJets().size()) return sendJetPage(player, JetManager.instance().getJets().size());
        player.sendMessage(" ");
        for (JetCategory category : JetManager.instance().getJets().values()) {
            ComponentBuilder catComp = new ComponentBuilder("- "+category.getId());
            Helper.command(catComp,"/jet list "+category.getId());
            Helper.hoverMessage(catComp, List.of(
                    "§eJets: " + category.getJets().size()
            ));
            player.spigot().sendMessage(catComp.create());
        }
        ComponentBuilder navBar = new ComponentBuilder();
        Helper.addCommandMessage(navBar, ((page > 1)?"≪":"||"),((page > 1)? "/jet list "+(page-1):""));
        navBar.append(" §e("+page+"/"+ JetManager.instance().getJets().size()+") ");
        Helper.addCommandMessage(navBar, ((page < JetManager.instance().getJets().size())?"≫":"||"),((page > 1)? "/jet list "+(page+1):""));
        player.spigot().sendMessage(navBar.create());
        return true;
    }

    public boolean sendCategoryInfo(Player player, JetCategory category) {
        player.sendMessage(" ");
        player.sendMessage("§e["+category.getId()+"]");
        for (Jet jet : category.getJets().values()) {
            ComponentBuilder jetBuilder = new ComponentBuilder(" - "+jet.getName());
            Helper.hoverMessage(jetBuilder,Arrays.asList(
                    "§bMaterial: "+jet.getMaterial().name()+"\n",
                    "§6Range: "+jet.getActivationRange()+"\n",
                    "§aVecteur: "+jet.getVector().toString()+"\n",
                    "§7Position: \n"+Helper.locationToReadableString(jet.getLocation())
            ));
            player.spigot().sendMessage(jetBuilder.create());
        }
        return true;
    }

}