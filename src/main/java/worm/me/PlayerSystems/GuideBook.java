package worm.me.PlayerSystems;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class GuideBook {

    public void guideBook(Player p){

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        List<BaseComponent[]> pages = new ArrayList<>();

        pages.add(new ComponentBuilder("Welcome to 10!").underlined(true).append("\n\nThis book is meant to help you learn about some of the fun and unique features this server offers!\n\nMake sure to follow the rules (").reset().append("/rules").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules")).append(") and have a good time! ☺").reset().create());
        pages.add(new ComponentBuilder("INTRODUCTION").underlined(true).append("\n\nWhenever you see the Table of Contents button at the bottom of a page, you can click it to return.\n\nIn the Table of Contents, you can click any section label to go there.").reset().create());
        pages.add(new ComponentBuilder("INTRODUCTION").underlined(true).append("\n\nAlso, you can click on any colored commands in the book to use them in chat.\n\n\n\n\n\n\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("\nTable of Contents").underlined(true).append("\n\n1. Generator settings").color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "5")
        ).append("\n2. Leveling System").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "6")).append("\n3. Quests").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "8"))
                .append("\n4. Abilities").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "10")).append("\n5. Communication").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "10"))
                .append("\n6. Recipes").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "13")).append("\n7. Reputation").reset().color(ChatColor.GOLD).underlined(true).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "15")).append("\n\nClick the Table of Contents button to return here.").reset().create());
        pages.add(new ComponentBuilder("GENERATOR SETTINGS").underlined(true).append("\n\nAn item is generated every 10 seconds. By default, these items include Cobblestone, Dirt, Oak Planks, Coal, and Iron Ore.\n\nSome can be changed with the ").reset()
                .append("/setgens").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setgens")).append(" command.\n\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("LEVELING SYSTEM").underlined(true).append("\n" +
                "\n" +
                "Levels are earned through associated activities or quests. \n" +
                "\n" +
                "At certain milestones, abilities and recipes may be unlocked. Level unlocks may be overlapping, such that you would need two or more levels to unlock something.").reset().create());
        pages.add(new ComponentBuilder("LEVELING SYSTEM").underlined(true).append("\n\n" +
                "Some abilities may provide skill exp upon use.\n" +
                "\n" +
                "They are not meant to make one incredibly overpowered, but to provide more methods to do something.\n" +
                "\n" +
                "\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("QUESTS").underlined(true).append("\n\n" +
                "There are two types of quests, Daily and Weekly.\n" +
                "\n" +
                "Quests are randomized, so you will not always have the same quest as someone else. They may grant various rewards, ranging from skill exp to items.").reset().create());
        pages.add(new ComponentBuilder("QUESTS").underlined(true).append("\n\n" +
                "Daily quests reset at 12:00 AM EST, and Weekly quests at 12:00 AM EST on Sundays.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("COMMUNICATION").underlined(true).append("\n\nIf you haven't noticed, no messages are sent in chat. Instead, they are typed out above the player's head. Each player can choose their own voice, according to an ID and pitch, with /setvoice. An example would be\n").reset()
                .append("/setvoice 13 0.5").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setvoice 13 0.5")).create());
        pages.add(new ComponentBuilder("COMMUNICATION").underlined(true).append("\n\nThere also exists a /shout command which has a 15 minute cooldown, but does not send a message above the player's head. This is more for trading purposes than chatting.").reset().create());
        pages.add(new ComponentBuilder("COMMUNICATION").underlined(true).append("\n\nSkins are changed and names hidden, so it would be mindful to create a \"password\" betweem friends.\n\n\n\n\n\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("RECIPES").underlined(true).append("\n\nThere are a few recipes immediately granted to the player upon joining. Other recipes are granted with levels. \n" +
                "\n" +
                "A player may not use a recipe until they have unlocked it, but they can use the item, though.").reset().create());
        pages.add(new ComponentBuilder("RECIPES").underlined(true).append("\n\nRecipes may be viewed with ").reset().append("/recipes").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recipes")).append(".\n\n\n\n\n\n\n\n\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());
        pages.add(new ComponentBuilder("REPUTATION").underlined(true).append("\n\nReputation is what a player is known for doing. It will change a player's chat color accordingly. There are 3 different types of activities a player may take part in: Combat, Skill Leveling, and Survival.").reset().create());
        pages.add(new ComponentBuilder("REPUTATION").underlined(true).append("\n\nCombat is associated with Red, Skill Leveling is associated with Blue, and Survival is associated with Green.\n" +
                "\n" +
                "A player who spends most of their time grinding Skill Levels and Survival Activities will have a turquoise chat tint.").reset().create());
        pages.add(new ComponentBuilder("REPUTATION").underlined(true).append("\n\nHowever, a player who has a red chat tint is someone who practices combat often, and should be treated with caution.\n\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n").reset().append(" [TABLE OF CONTENTS]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4")).create());


        for (BaseComponent[] page : pages) {
            meta.spigot().addPage(page);
        }

        meta.setAuthor("§u§7w§do§7r§dm§7y");
        meta.setTitle("10 Guide Book");

        book.setItemMeta(meta);

        p.openBook(book);
    }
}
