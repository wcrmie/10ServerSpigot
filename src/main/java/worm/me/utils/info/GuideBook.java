package worm.me.utils.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
        List<Component> pages = new ArrayList<>();

        // Welcome Page
        pages.add(MiniMessage.miniMessage().deserialize(
                "Introduction\n" +
                        "-----------\n" +
                        "\n" +
                        "Welcome to 10. This handbook has been created to help players in their trek among <#6432c8>Void Civilization</#6432c8>.\n" +
                        "\n" +
                        "The information contained within will be vital to your success and experience on the server."
        ));

        // How to Use This Book
        pages.add(MiniMessage.miniMessage().deserialize(

                "Introduction (cont.)\n" +
                        "-----------\n" +
                        "\n" +
                        "We hope you enjoy your stay, and remember: You can always <gold><b><click:run_command:/handbook>/handbook</click></b></gold> to read this again any time! \n" +
                        "\n" +
                        "<aqua>Voice Chat is enabled and recommended:</aqua> <hover:show_text:'<gray>https://modrinth.com/plugin/simple-voice-chat</gray>'><click:open_url:https://modrinth.com/plugin/simple-voice-chat><gold><b>[CLICK HERE]</b></gold></click></hover>"

        ));

        // Table of Contents - All on one page
        pages.add(MiniMessage.miniMessage().deserialize(

                "Rules (shortened)\n" +
                        "-----------\n" +
                        "\n" +
                        "1. No Hacking/Exploits\n" +
                        "2. No Racism/Bigotry\n" +
                        "3. No Undue Weirdness\n" +
                        "4. No Spam\n" +
                        "5. No Advertising\n" +
                        "6. Do not ruin the player experience.\n" +
                        "7. USE COMMON SENSE!\n" +
                        "\n" +
                        "Make sure to <red>/report</red> Cheaters!"

        ));

        // Rules
        pages.add(MiniMessage.miniMessage().deserialize(

                "Fundamentals\n" +
                        "-----------\n" +
                        "\n" +
                        "On 10 many vanilla features remain while others have been modified or removed.\n" +
                        "\n" +
                        "Custom <gold><click:run_command:/recipes><hover:show_text:'<gray>[CLICK]</gray> to run /recipes'>/recipes</click></gold> exist, many rebalanced with price reductions. \n" +
                        "\n" +
                        "Some stack sizes have been changed as well."

        ));
        pages.add(MiniMessage.miniMessage().deserialize(

                "Tools\n" +
                        "-----------\n" +
                        "\n" +
                        "You can upgrade some tools and armor by clicking material onto the item in your inventory. \n" +
                        "\n" +
                        "Requirements are listed in the tooltip, seen by hovering over the tool."

        ));

        // Useful Commands
        pages.add(MiniMessage.miniMessage().deserialize(

                "Tools (cont.)\n" +
                        "-----------\n" +
                        "\n" +
                        "If a tool breaks it'll revert to the previous tier - you cannot skip tiers when upgrading. \n" +
                        "\n" +
                        "Throughout 10 there are many sidegrades like Unbreakable copper tools and emerald armor."

        ));

        // Tools
        pages.add(MiniMessage.miniMessage().deserialize(

                "Enchantments (cont.)\n" +
                        "-----------\n" +
                        "\n" +
                        "Upgrading a tool rolls a 10% chance it will gain an enchant related to it's primary use-case.\n" +
                        "\n" +
                        "If an enchanted item breaks there's a 20% chance per enchant it'll reduce the level by one."

        ));

        // Item Generation
        pages.add(MiniMessage.miniMessage().deserialize(

                "Enchantments (cont.)\n" +
                        "-----------\n" +
                        "\n" +
                        "Certain enchantments are capped such as protection and power. \n" +
                        "\n" +
                        "Tool tiers hold the same, with lower tiers being more restricted.\n" +
                        "\n" +
                        "These rarities, even when sold are done so at low levels."

        ));

        pages.add(MiniMessage.miniMessage().deserialize(

                "Leveling\n" +
                        "-----------\n" +
                        "Leveling is a cornerstone of the server with multiple features locked behind it such as movement <click:run_command:/abilities><hover:show_text:'<gray>[CLICK]</gray> to run /abilities'><aqua>/abilities</aqua></hover></click> and block <green>generators</green>.\n" +
                        "\n" +
                        "You gain exp in various categories by doing related actions."

        ));

        // Leveling
        pages.add(MiniMessage.miniMessage().deserialize(

                "Leveling (cont.)\n" +
                        "-----------\n" +
                        "\n" +
                        "Agility - use abilities. Combat - slay mobs.\n" +
                        "Foraging - Break nature blocks.\n" +
                        "Mining - Mine ores and stones.\n" +
                        "Smithing - Smelt.\n" +
                        "Farming - Farm.\n" +
                        "Fishing - Fish.\n" +
                        "Crafting - Craft."

        ));

        pages.add(MiniMessage.miniMessage().deserialize(

                "Abilities\n" +
                        "-----------\n" +
                        "\n" +
                        "Powerful <click:run_command:/abilities><hover:show_text:'<gray>[CLICK]</gray> to run /abilities'><aqua>/abilities</aqua></hover></click> exist, they're based on three categories:\n" +
                        "basic, horizontal, and vertical.\n" +
                        "\n" +
                        "Activate them by tapping the spacebar twice, or crouching and right clicking with \n" +
                        "a Sword/Axe! "

        ));
        pages.add(MiniMessage.miniMessage().deserialize(

                "Generators\n" +
                        "-----------\n" +
                        "\n" +
                        "You <green>generate</green> blocks every <#6432c8>10</#6432c8> seconds from four customizable sections\n" +
                        "and one set block, the % chance is distributed between them.\n"

        ));

        // Abilities
        pages.add(MiniMessage.miniMessage().deserialize(

                "The World\n" +
                        "-----------\n" +
                        "\n" +
                        "The world border grows depending on the amount of blocks placed and broken.\n" +
                        "\n" +
                        "Map resets happen regularly, with the world border being sized within an approved range after."

        ));

        pages.add(MiniMessage.miniMessage().deserialize(

                "The World (cont).\n" +
                        "-----------\n" +
                        "\n" +
                        "Set events happen randomly like island spawns or treasure chests.\n" +
                        "\n" +
                        "If you ever wish to see information about the World please visit <click:run_command:/menu><hover:show_text:'<gray>[CLICK]</gray> to run /menu'><dark_aqua>/menu</dark_aqua></hover></click>."


        ));
        pages.add(MiniMessage.miniMessage().deserialize(

                "Commands\n" +
                        "-----------\n" +
                        "\n" +
                        "<gold><click:run_command:/handbook><hover:show_text:'<gray>[CLICK]</gray> to run /handbook'>/handbook</hover></click></gold> \n" +
                        "<click:run_command:/menu><hover:show_text:'<gray>[CLICK]</gray> to run /menu'><dark_aqua>/menu</dark_aqua></hover></click> \n" +
                        "<click:run_command:/rules><hover:show_text:'<gray>[CLICK]</gray> to run /rules'><red>/rules</red></hover></click>\n" +
                        "<click:suggest_command:/report><hover:show_text:'<gray>[CLICK]</gray> to run /report'><red>/report user</red></hover></click>\n" +
                        "<click:suggest_command:/profile><hover:show_text:'<gray>[CLICK]</gray> to run /profile'><aqua>/profile user</aqua></hover></click>\n" +
                        "\n" +
                        "Commands exist for entries inside <click:run_command:/menu><hover:show_text:'<gray>[CLICK]</gray> to run /menu'><dark_aqua>/menu</dark_aqua></hover></click>. They use the same names listed there."

        ));
        // Set all pages
        for (Component page : pages) {
            meta.addPages(page);
        }

        meta.setAuthor("§u§7w§do§7r§dm§7y");
        meta.setTitle("Void Handbook");
        book.setItemMeta(meta);
        p.openBook(book);
    }
}