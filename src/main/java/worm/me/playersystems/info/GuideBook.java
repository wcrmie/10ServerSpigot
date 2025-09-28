package worm.me.playersystems.info;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import java.util.ArrayList;
import java.util.List;

public class GuideBook {
    public static void guideBook(Player p){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        List<BaseComponent[]> pages = new ArrayList<>();

        // Welcome Page
        pages.add(new ComponentBuilder("Welcome to 10: void!").underlined(true)
                .append("\n\nThis book is meant to help you learn about some of the fun and unique features this server offers!\n\nMake sure to follow the rules (").reset()
                .append("/rules").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules"))
                .append(") and have a good time! :)").reset().create());

        // How to Use This Book
        pages.add(new ComponentBuilder("How to use this book").underlined(true)
                .append("\n\nAny time you see a gold button of text [Example] you can click it to go to that section.\n\nAny time you see (").reset()
                .append("/command").color(ChatColor.AQUA).bold(true)
                .append(") clicking on this will exit out of this book and execute that command for you.").reset().create());

        // Table of Contents - All on one page
        pages.add(new ComponentBuilder("Table of Contents").underlined(true)
                .append("\n\n").reset()
                .append("[Rules]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "4"))
                .append("\n").reset()
                .append("[Useful Commands]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "6"))
                .append("\n").reset()
                .append("[Tools]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "7"))
                .append("\n").reset()
                .append("[Item Generation]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "8"))
                .append("\n").reset()
                .append("[Leveling]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "10"))
                .append("\n").reset()
                .append("[Abilities]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "13"))
                .append("\n").reset()
                .append("[Progression]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "16"))
                .append("\n").reset()
                .append("[Communication]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "23"))
                .append("\n").reset()
                .append("[Reputation]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "27"))
                .append("\n").reset()
                .append("[Hats]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "29"))
                .append("\n").reset()
                .append("[Enchantments]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "32"))
                .append("\n").reset()
                .append("[World]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "34")).create());

        // Rules
        pages.add(new ComponentBuilder("Rules").underlined(true)
                .append("\n\n1. No Hacking\n2. Don't be excessively toxic\n3. No racism/bigotry\n4. DON'T SPAM! WE CAN SEE YOUR MESSAGES!\n5. Don't advertise your Server; nobody wants to join it anyways\n\n").reset().create());
        pages.add(new ComponentBuilder("Rules (cont.)").underlined(true)
                .append("\n\nYou can see these rules any time by doing (").reset()
                .append("/rules").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules"))
                .append(")\n\nAlso if you see someone breaking these rules at any time make sure to (").reset()
                .append("/report").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report"))
                .append(") them\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Useful Commands
        pages.add(new ComponentBuilder("Useful Commands").underlined(true)
                .append("\n\n(").reset()
                .append("/menu").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu"))
                .append(")\n(").reset()
                .append("/profile").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile"))
                .append(")\n(").reset()
                .append("/levels").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/levels"))
                .append(")\n(").reset()
                .append("/setgens").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setgens"))
                .append(")\n(").reset()
                .append("/abilities").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/abilities"))
                .append(")\n(").reset()
                .append("/report").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report"))
                .append(")\n(").reset()
                .append("/recipes").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recipes"))
                .append(")\n(").reset()
                .append("/rules").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules"))
                .append(")\n(").reset()
                .append("/sit").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sit"))
                .append(")\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Tools
        pages.add(new ComponentBuilder("Tools").underlined(true)
                .append("\n\nEveryone starts with wooden tools and can't craft others. Upgrade them with cobble or iron by hovering over the tool to see requirements, then place the material onto it inside your inventory.\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Item Generation
        pages.add(new ComponentBuilder("Item Generation").underlined(true)
                .append("\n\nYou get an item every 10 seconds. Each time you receive an item, it will have a chance of coming from 4 categories: Stones, Loose Blocks, Woods, Iron Ore.").reset().create());
        pages.add(new ComponentBuilder("Item Generation (cont.").underlined(true)
                .append("\n\nYou can change what item you get in these categories as you level up your skills with (").reset()
                .append("/setgens").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setgens"))
                .append(").\n\n\n\n\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Leveling
        pages.add(new ComponentBuilder("Leveling").underlined(true)
                .append("\n\nYou gain skill experience, and therefore levels, by doing certain actions related to the area they represent.\n\nThese are:\n1. Agility from using movement abilities.\n2. Combat from killing mobs (not players).\n").reset().create());

        pages.add(new ComponentBuilder("Leveling (cont.)").underlined(true)
                .append("\n\n3. Mining from breaking certain blocks (ores and stones).\n4. Foraging from breaking natural blocks (dirt, tall grass).\n5. Farming from, well, farming.\n6. Fishing from, as you would assume, fishing.").reset().create());
        pages.add(new ComponentBuilder("Leveling (cont.)").underlined(true)
                .append("\n\n7. Smithing from smelting items.\n8. Crafting from crafting items.\n\n\n\n\n\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Abilities
        pages.add(new ComponentBuilder("Abilities").underlined(true)
                .append("\n\nAbilities are tools used to move around the world in different and unique ways, there are 3 abilities slots one for basic abilities, one for horizontal ability and one for vertical abilities.").reset().create());

        pages.add(new ComponentBuilder("Abilities (cont.)").underlined(true)
                .append("\n\nYou start with the basic ability, Double Jump. you can use this now just by pressing your jump button twice in quick succession.").reset().create());
        pages.add(new ComponentBuilder("Abilities (cont.)").underlined(true)
                .append("\n\nTo unlock new abilities, the player must attain the correct skill level requirement. To equip an ability do (").reset()
                .append("/abilities").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/abilities"))
                .append(")\n\n\n\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Progression
        pages.add(new ComponentBuilder("Progression").underlined(true)
                .append("\n\nThere are 3 main resources in this game: iron, gold, and emeralds.\n\nIron is mostly used for upgrading tools, making anvils, and summoning iron golems.").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nGold is the main resource used to trade with wandering traders, villagers, and the easiest way to get it is with a mob farm smelting down the gold armor.").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nEmeralds are gotten from killing endermen and doing raids, they are used for some of the latter trades with villagers and to craft emerald armor.").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nThe main source of mid game items is from the wandering trader which naturally spawns rarely but, you can also fish one up if you really want to trade with one.").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nLate game we have Villagers, they are a little unique on this server, you can cure them to get them as in normal minecraft with a witch or potion and a golden apple. However, on this server that creates a nitwit that you need to teach with a").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nknowledge tome in order for it to take a job.\n\nKnowledge tomes are obtained from placing down an iron golem and killing the mini boss that spawns.").reset().create());

        pages.add(new ComponentBuilder("Progression (cont.)").underlined(true)
                .append("\n\nBoth villagers and wandering traders have completely custom trades.\n\nWhen smelting ores or gear you might want to craft a blast furnace, you get more nuggets.\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Communication
        pages.add(new ComponentBuilder("Communication").underlined(true)
                .append("\n\nThis server uses Simple Voice Chat. If you would like to speak to players without using chat, feel free to install it.").reset().create());

        pages.add(new ComponentBuilder("Communication (cont.)").underlined(true)
                .append("\n\nOn this server, chat is slightly different. When a message is sent through chat, it will display a darker color dependent on how far away you are from the person that sent it.").reset().create());

        pages.add(new ComponentBuilder("Communication (cont.)").underlined(true)
                .append("\n\nWhen you or another player sends a message through chat, the message will be displayed above the head of the player who wrote it.\n\nThis text can become red depending on the player's reputation.").reset().create());

        pages.add(new ComponentBuilder("Communication (cont.)").underlined(true)
                .append("\n\nThis message above the head will also play a sound as it types out.\nThis sound can be changed with the (").reset()
                .append("/setvoice").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setvoice"))
                .append(") command.\n\n\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Reputation
        pages.add(new ComponentBuilder("Reputation").underlined(true)
                .append("\n\nPlayer reputation is a value to show how violent a player is, in terms of the amount of players they have killed in ratio to the amount of time they have spent in their reputation session.").reset().create());

        pages.add(new ComponentBuilder("Reputation (cont.)").underlined(true)
                .append("\n\nThe worse your reputation the more damage you take from players.\nA reputation session is a period of time starting on join and continuing as long as there is never more than an hour of inactivity.\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Hats
        pages.add(new ComponentBuilder("Hats").underlined(true)
                .append("\n\nHelmets are non-existent on this server.\n\nInstead, this server has the feature to put any item or block on your head with the use of (").reset()
                .append("/hat").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hat"))
                .append(")").reset().create());

        pages.add(new ComponentBuilder("Hats (cont.)").underlined(true)
                .append("\n\nSome items, when put on your head, may have additional functionality on top of being cosmetic.\n\nSome examples include:\n\nSaddles allow you to pick up mobs and players, and throw them").reset().create());

        pages.add(new ComponentBuilder("Hats (cont.)").underlined(true)
                .append("\n\nWhen you have a Bell on your head it chimes whenever you take damage.\n\n\n\n\n\n\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Enchantments
        pages.add(new ComponentBuilder("Enchantments").underlined(true)
                .append("\n\nSome enchantments are limited on this server.\n\nUnbroken is limited to level 1 except on armor.").reset().create());

        pages.add(new ComponentBuilder("Enchantments(cont.)").underlined(true)
                .append("\nSharpness, power, protection, and feather falling are all limited to level 3.\n\nWhen a tool or weapon down grades there's a chance for it to downgrade the enchantment level of that tool/weapon.\n\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // World
        pages.add(new ComponentBuilder("World").underlined(true)
                .append("\n\nEvery once in a while islands will spawn in random areas around the map.\nFor the rest of this information you can see the current status of the server right now using (").reset()
                .append("/menu").color(ChatColor.AQUA).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu"))
                .append(") and looking at the enchanted book.").reset().create());

        pages.add(new ComponentBuilder("World (cont.)").underlined(true)
                .append("\n\nThe world resets every 60 to 120 hours.\n\nthe world will expand depending on how many blocks are placed/broken, so in which the density of the world is the expansion percentage.\n").reset()
                .append("[Table of Contents]").color(ChatColor.GOLD).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "3")).create());

        // Set all pages
        for (BaseComponent[] page : pages) {
            meta.spigot().addPage(page);
        }

        meta.setAuthor("§u§7w§do§7r§dm§7y");
        meta.setTitle("10: void Guide Book");
        book.setItemMeta(meta);
        p.openBook(book);
    }
}