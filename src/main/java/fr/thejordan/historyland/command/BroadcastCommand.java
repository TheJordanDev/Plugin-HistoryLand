package fr.thejordan.historyland.command;

import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.LanguageManager;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BroadcastCommand extends AbstractCommand {

    @Override
    public String id() {
        return "hbroadcast";
    }
    @Override
    public String description() { return null; }
    @Override
    public String usage() {
        return "/hbroadcast <langue> <chat/action> <message/code>";
    }
    @Override
    public String permission() {
        return "historyland.broadcast";
    }
    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return sendMessageF(sender,Historyland.GAME_PREFIX+"§c/hbroadcast <langue> <chat/action> <message/code>");
        } else {
            LanguageManager.BCLanguage lang;
            Optional<Language> language = LanguageManager.instance().getLanguageFromName(args[0]);
            if (language.isEmpty()) {
                if (args[0].equals("all") || args[0].equals("*")) lang = LanguageManager.BCLanguage.ALL;
                else return sendMessageF(sender,Historyland.GAME_PREFIX+"§cCette langue n'existe pas.");
            } else lang = LanguageManager.BCLanguage.SET.target(language.get());

            LanguageManager.BCType type;
            Optional<LanguageManager.BCType> msgType = Helper.isValueOfEnum(LanguageManager.BCType.class, args[1]);
            if (msgType.isPresent()) type = msgType.get();
            else return sendMessageF(sender,Historyland.GAME_PREFIX+"§cCe type de message n'existe pas.");

            String message = String.join(" ",Arrays.copyOfRange(args, 2, args.length));

            LanguageManager.instance().broadcastMessage(lang, type, message);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> autocompleted;
        if (args.length == 0) return List.of();
        else if (args.length == 1) autocompleted = Helper.addToList(LanguageManager.instance().languages().values().stream().map(Language::name).collect(Collectors.toList()),"*","all");
        else if (args.length == 2) autocompleted = Arrays.stream(LanguageManager.BCType.values()).map(LanguageManager.BCType::name).collect(Collectors.toList());
        else if (args.length == 3) autocompleted = LanguageManager.instance().phrases().stream().map((s)-> "%"+s+"%").collect(Collectors.toList());
        else autocompleted = List.of();
        return Helper.autocomplete(args[args.length-1], autocompleted);
    }

}
