package worm.me.gamesystems.chat;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public enum TutorialTip {

    TUTORIAL("<dark_aqua><b>TUTORIAL</b>", "<yellow>Welcome to 10: Void Civilization! \nThis basic tutorial will help you orient yourself in this world. \nIf you feel like you need more help, feel free to join the <blue>/discord</blue>!"),
    GENS("<green><b>GENERATORS</b>", "<yellow>The most basic items will be generated in your inventory every 10 seconds. They can be swapped out via <gold>/generators</gold>."),
    TOOLS("<aqua><b>TOOLS</b>", "<yellow>You may have noticed that you cannot simply craft stone tools. \nTo get better tools, you need to upgrade them.\nWhen upgrading tools, there is a <gold>10%</gold> chance that it will be enchanted! \nWhen higher-tier tools break, they downgrade into the lower tier. \nIf an enchanted tool breaks, each enchant has a <gold>20%</gold> chance to downgrade."),
    RECIPES("<gold><b>RECIPES</b>", "<yellow>There are many custom recipes on this server. They can be viewed using <gold>/recipes</gold>."),
    LEVELS("<dark_aqua><b>LEVELS</b>", "<yellow>If you used <gold>/generators</gold>, you may have noticed that most of the generators are locked, and have level requirements. Levels can be earned in various ways, and you can see your current levels by using <gold>/levels</gold>."),
    ABILITIES("<red><b>ABILITIES</b>", "<yellow>Abilities are mostly movement-based by design. They can also be unlocked with levels, and can be accessed by doing <gold>/abilities</gold>."),
    COMMANDS("<gold><b>COMMANDS</b>",
            "<yellow><gold>/menu</gold>: Opens the main server menu, by which all other menus are accessible.\n" +
            "<yellow><gold>/hat</gold>: Switches the item on your head with the item in your hand.\n" +
            "<yellow><gold>/sit</gold>: Makes you sit on the ground.\n" +
            "<yellow><gold>/suicide</gold>: Immediately kills you.\n" +
            "<yellow><gold>/togglegens</gold>: Toggles the generation of items in your inventory.\n" +
            "<yellow><gold>/discord</gold>: Sends a clickable Discord link in chat.\n" +
            "<yellow><gold>/rules</gold>: Sends the guidelines in chat to be read."
    );

    private final String title;
    private final String description;

    TutorialTip(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public void send(Player player) {

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                String.format(

                        "\n\n%s\n\n%s\n",
                        getTitle(),
                        getDescription()

                )
        ));

    }
}
