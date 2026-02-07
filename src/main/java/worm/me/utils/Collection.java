package worm.me.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.Set;

public enum Collection {
    DOWN_AND_DIRTY("Down and Dirty", "%%victim%% got grounded by their mother.", null, null, Set.of(Material.ROOTED_DIRT, Material.DIRT, Material.GRASS_BLOCK, Material.MOSS_BLOCK, Material.MUD)),
    ROCK_AND_ROLL("Rock and Roll", "%%victim%% tripped on a rock.", null, null, Set.of(Material.INFESTED_COBBLESTONE, Material.COBBLESTONE, Material.STONE, Material.SMOOTH_STONE, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.CHISELED_STONE_BRICKS)),
    TAKEN_FOR_GRANITE("Taken for Granite", "%%victim%% took life for granite.", null, null, Set.of(Material.GRANITE, Material.POLISHED_GRANITE, Material.DIORITE, Material.POLISHED_DIORITE, Material.ANDESITE, Material.POLISHED_ANDESITE)),
    BACK_ON_COARSE("Back on Coarse", null, "%%victim%% was buried alive by %%attacker%%.", null, Set.of(Material.SUSPICIOUS_GRAVEL, Material.GRAVEL, Material.SUSPICIOUS_SAND, Material.SAND, Material.RED_SAND, Material.CLAY, Material.COARSE_DIRT)),
    BRICKED_UP("Bricked Up", null, "%%attacker%% made %%victim%%'s head look like a pancake.", null, Set.of(Material.PACKED_MUD, Material.MUD_BRICKS, Material.SANDSTONE, Material.RED_SANDSTONE, Material.CALCITE, Material.BRICKS, Material.TUFF)),
    SMOOTH_MOVES("Smooth Moves", null, "%%attacker%% hit the griddy on %%victim%%.", null, Set.of(Material.SMOOTH_SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.CHISELED_RED_SANDSTONE)),
    SALTY("Salty", "And just like that, %%victim%% ruined it. Spamming, hacking, getting the lobby muted, and more.", "%%attacker%% salted %%victim%%'s wound.", "😭", Set.of(Material.QUARTZ, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.SMOOTH_QUARTZ, Material.QUARTZ_PILLAR, Material.CHISELED_QUARTZ_BLOCK)),
    MORNING_WOOD("Morning Wood", null, null, "🌴", Set.of(Material.BAMBOO, Material.AZALEA, Material.FLOWERING_AZALEA, Material.OAK_LOG, Material.OAK_WOOD, Material.OAK_PLANKS, Material.BAMBOO_BLOCK, Material.BAMBOO_PLANKS, Material.BAMBOO_MOSAIC)),
    MAKE_LIKE_A_TREE("Make Like a Tree", "%%victim%% made like a tree and leaf'd.", null, "🍂", Set.of(Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING, Material.MANGROVE_PROPAGULE)),
    CAPTAINS_LOG("Captain's Log", "%%victim%% went down with their ship.", "%%attacker%% made %%victim%% walk the plank.", "🦀", Set.of(Material.SPRUCE_LOG, Material.SPRUCE_PLANKS, Material.BIRCH_LOG, Material.BIRCH_PLANKS, Material.JUNGLE_LOG, Material.JUNGLE_PLANKS, Material.ACACIA_LOG, Material.ACACIA_PLANKS, Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.MANGROVE_LOG, Material.MANGROVE_PLANKS)),
    RESPECT_YOUR_ELDERS("Respect Your Elders", "%%victim%% died of old age.", "%%attacker%% beat %%victim%% with their cane.", "👴", Set.of(Material.PRISMARINE_SHARD, Material.PRISMARINE_CRYSTALS, Material.SEA_LANTERN, Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE, Material.SPONGE, Material.WET_SPONGE)),
    CORAL("CORAL!", "%%victim%% was consumed by the horde.", "%%attacker%% used %%victim%% as zombie bait.", null, Set.of(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL, Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL, Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL, Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL, Material.HORN_CORAL_BLOCK, Material.HORN_CORAL)),
    SEEMS_FISHY("Seems Fishy...", "%%victim%% swam with the fishes.", null, "🐟", Set.of(Material.AXOLOTL_BUCKET, Material.PUFFERFISH_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.SALMON_BUCKET, Material.COD_BUCKET, Material.WATER_BUCKET)),
    GETTING_NAUTY("Getting Naut-y", "%%victim%% was CLEARly in over their head.", "%%victim%% was lured to their death by %%attacker%%.", null, Set.of(Material.LILY_PAD, Material.SEAGRASS, Material.SEA_PICKLE, Material.KELP, Material.DRIED_KELP_BLOCK, Material.INK_SAC, Material.GLOW_INK_SAC, Material.NAUTILUS_SHELL, Material.HEART_OF_THE_SEA, Material.CONDUIT)),
    BE_NIMBLE("Be Nimble!", "%%victim%% jumped a little too far (over the candlestick).", null, "🕯", Set.of(Material.WHITE_CANDLE, Material.LIGHT_GRAY_CANDLE, Material.GRAY_CANDLE, Material.BLACK_CANDLE, Material.BROWN_CANDLE, Material.RED_CANDLE, Material.ORANGE_CANDLE, Material.YELLOW_CANDLE, Material.GREEN_CANDLE, Material.LIME_CANDLE, Material.CYAN_CANDLE, Material.LIGHT_BLUE_CANDLE, Material.BLUE_CANDLE, Material.PURPLE_CANDLE, Material.MAGENTA_CANDLE, Material.PINK_CANDLE)),
    BUDDY_SYSTEM("Buddy System", "%%victim%% lost their buddy.", "%%attacker%% left %%victim%% with a very hungry bear.", null, Set.of(Material.AMETHYST_SHARD, Material.BUDDING_AMETHYST, Material.AMETHYST_BLOCK, Material.SMALL_AMETHYST_BUD, Material.MEDIUM_AMETHYST_BUD, Material.LARGE_AMETHYST_BUD, Material.AMETHYST_CLUSTER, Material.TINTED_GLASS)),
    IN_THE_ENDGAME("In the Endgame", "%%victim%% could NOT do this all day.", "%%attacker%% turned %%victim%% into a cloud of dust.", null, Set.of(Material.NETHERRACK, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, Material.BASALT, Material.POLISHED_BASALT, Material.BLACKSTONE, Material.POLISHED_BLACKSTONE, Material.SOUL_SAND, Material.SOUL_SOIL)),
    PORTAL_PLANTS("Portal Plants", null, "%%attacker%% trapped %%victim%%'s portal.", null, Set.of(Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS, Material.CRIMSON_STEM, Material.CRIMSON_PLANKS, Material.WARPED_STEM, Material.WARPED_PLANKS, Material.SHROOMLIGHT, Material.CRIMSON_ROOTS, Material.WEEPING_VINES, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.TWISTING_VINES)),
    ONE_WITH_THE_VOID("One With the Void", "%%victim%% realized this was not ElytraPvP.", "%%victim%% teleported onto %%attacker%%'s sword.", null, Set.of(Material.END_STONE, Material.END_STONE_BRICKS, Material.CHORUS_FLOWER, Material.CHORUS_FRUIT, Material.POPPED_CHORUS_FRUIT, Material.PURPUR_BLOCK, Material.PURPUR_PILLAR)),
    GET_TO_THE_COPPA("Get to the Coppa!", "%%victim%% will be back.", "%%attacker%% made %%victim%% put the cookie down.", null, Set.of(Material.COPPER_INGOT, Material.COPPER_BLOCK, Material.CUT_COPPER, Material.EXPOSED_COPPER, Material.EXPOSED_CUT_COPPER, Material.WEATHERED_COPPER, Material.WEATHERED_CUT_COPPER, Material.OXIDIZED_COPPER, Material.OXIDIZED_CUT_COPPER)),
    BLOCKED_MESSAGE("1 Blocked Message", "%%victim%%'s game is totally lagging.", "%%attacker%% is just better than %%victim%%.", null, Set.of(Material.COAL_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK)),
    LET_IT_GO("Let it Go!", "%%victim%% forgot to install central heating.", "%%attacker%% finished %%victim%%'s sandwiches.", "❄", Set.of(Material.SNOWBALL, Material.SNOW_BLOCK, Material.POWDER_SNOW_BUCKET, Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE)),
    BREAKING_EARTH("Breaking Earth", "%%victim%%'s efforts were all for nothing.", null, "🌎", Set.of(Material.PODZOL, Material.MYCELIUM, Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS)),
    NETHER_AGAIN("Nether Again...", "%%victim%% forgot to bring obsidian to the roof of the nether.", "%%attacker%% sent %%victim%% racing back to hell.", null, Set.of(Material.GLOWSTONE_DUST, Material.GLOWSTONE, Material.MAGMA_BLOCK, Material.OBSIDIAN, Material.CRYING_OBSIDIAN, Material.RESPAWN_ANCHOR, Material.LODESTONE, Material.EXPERIENCE_BOTTLE)),
    CHOPDUSTED("Cropdusted", null, "%%victim%% smelt it and %%attacker%% dealt it.", "🌽", Set.of(Material.HANGING_ROOTS, Material.DEAD_BUSH, Material.GLOW_LICHEN, Material.SUGAR_CANE, Material.SUGAR, Material.WHEAT_SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS, Material.BEETROOT_SEEDS, Material.COCOA_BEANS, Material.WHEAT, Material.CARROT, Material.POTATO, Material.BEETROOT)),
    READY_ORE_NOT("Ready Ore Not", "%%victim%% dug straight down.", null, null, Set.of(Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE)),
    A_SIGHT_FOR_SORE_EYES("A Sight for S-Ore Eyes", null, "%%victim%% had an unhappy reunion with %%attacker%%.", null, Set.of(Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE)),
    BLANK_SLATE("Blank Slate", "%%victim%% was wiped off the face of the earth.", null, "😶", Set.of(Material.GLASS, Material.TERRACOTTA, Material.CANDLE)),
    TURNING_A_NEW_LEAF("Turning a New Leaf", null, "%%victim%% was blown into the void by %%attacker%%'s leafblower.", "🍁", Set.of(Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES, Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES)),
    HARD_HARVESTS("Hard Harvests", "%%victim%% got caught by the Reaper.", "%%attacker%% turned %%victim%% into a scarecrow.", "🌾", Set.of(Material.PUMPKIN, Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.MELON, Material.NETHER_WART, Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK, Material.PITCHER_POD, Material.TORCHFLOWER_SEEDS)),
    GET_BACK_TO_WORK("Get Back to Work!", "%%victim%% was let go with no severance", "%%attacker%% promoted %%victim%% to customer.", null, Set.of(Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.SMITHING_TABLE, Material.FLETCHING_TABLE, Material.CARTOGRAPHY_TABLE, Material.LOOM, Material.COMPOSTER, Material.LECTERN, Material.STONECUTTER, Material.GRINDSTONE, Material.CAULDRON, Material.ANVIL, Material.NOTE_BLOCK, Material.JUKEBOX, Material.CHISELED_BOOKSHELF, Material.BOOKSHELF, Material.BELL)),
    STUCK_UP("Stuck Up", "%%victim%% landed on the corner of a slime block.", null, "🐝", Set.of(Material.BEE_NEST, Material.BEEHIVE, Material.HONEYCOMB, Material.HONEY_BLOCK, Material.HONEYCOMB_BLOCK, Material.SLIME_BALL, Material.SLIME_BLOCK)),
    SMOKING_POTS("Smoking Pots", "%%victim%% flew too close to the sun.", null, null, Set.of(Material.DECORATED_POT, Material.FLOWER_POT, Material.LIGHTNING_ROD, Material.CAMPFIRE, Material.SOUL_CAMPFIRE)),
    HEINZ_FACTORY("Heinz Factory", "%%victim%% got lost in the sauce.", "%%attacker%% fed %%victim%% beans on toast.", null, Set.of(Material.RAW_IRON_BLOCK, Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK)),
    OVER_EASY("Over Easy", "%%victim%% was poached.", "%%attacker%% scrambled %%victim%%'s brains.", "🥚", Set.of(Material.EGG, Material.SNIFFER_EGG, Material.TURTLE_EGG, Material.FROGSPAWN)),
    WHAT_A_TOOL("What a Tool!", "%%victim%% ran out of durability.", "%%victim%%'s nuts were loosened by %%attacker%%'s wrench.", "🔧", Set.of(Material.SHEARS, Material.FLINT_AND_STEEL, Material.BRUSH, Material.SPYGLASS, Material.COMPASS, Material.CLOCK, Material.CARROT_ON_A_STICK, Material.WARPED_FUNGUS_ON_A_STICK)),
    GET_JUKED("Get Juked!", null, "%%victim%% could not keep up with %%attacker%%", "📻", Set.of(Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_11, Material.MUSIC_DISC_WAIT)),
    FRISBEE("Frisbee", "%%victim%% danced until they were dead.", "%%attacker%% made %%victim%%'s ears bleed.", "&l💿", Set.of(Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_RELIC, Material.MUSIC_DISC_PIGSTEP)),
    ALL_THAT_GLITTERS("All That Glitters", "%%victim%% broke the mold.", "%%victim%% was mugged by %%attacker%%.", null, Set.of(Material.CHARCOAL, Material.COAL, Material.REDSTONE, Material.IRON_NUGGET, Material.IRON_INGOT, Material.GOLD_NUGGET, Material.GOLD_INGOT, Material.LAPIS_LAZULI, Material.EMERALD)),
    SUDDEN_HOSTILITY("Sudden Hostility", null, "%%attacker%% showed no mercy for %%victim%%.", null, Set.of(Material.ROTTEN_FLESH, Material.ARROW, Material.BONE, Material.BONE_MEAL, Material.BONE_BLOCK, Material.STRING, Material.SPIDER_EYE, Material.GUNPOWDER, Material.ENDER_EYE, Material.MAGMA_CREAM, Material.GHAST_TEAR, Material.ENDER_PEARL)),
    PASSIVE_PLUNDERS("Passive Plunders", null, null, "🦋", Set.of(Material.FEATHER, Material.LEATHER, Material.RABBIT_HIDE, Material.RABBIT_FOOT, Material.TURTLE_HELMET, Material.TURTLE_SCUTE)),
    FROGGER("Frogger", "%%victim%% did not look both ways before crossing the road.", "%%attacker%% hit %%victim%% while going 95 in a school zone.", "🐸", Set.of(Material.VERDANT_FROGLIGHT, Material.OCHRE_FROGLIGHT, Material.PEARLESCENT_FROGLIGHT)),
    PRIZED_POSSESSIONS("Prized Possessions", "%%victim%% forgot to mention the way they died.", "%%victim%% was killed by %%attacker%% in the guesthouse with the revolver.", null, Set.of(Material.SCAFFOLDING, Material.TNT, Material.NAME_TAG, Material.SADDLE, Material.LEATHER_HORSE_ARMOR, Material.WRITABLE_BOOK, Material.SPECTRAL_ARROW, Material.TOTEM_OF_UNDYING));

    private final Component name;
    private final Component deathMessage;
    private final Component killMessage;
    private final String tag;
    private final Set<Material> materials;

    Collection(String name, String deathMessage, String killMessage, String tag, Set<Material> materials) {
        this.name = Component.text(name, NamedTextColor.DARK_GRAY);
        this.deathMessage = Component.text(deathMessage, NamedTextColor.GRAY);
        this.killMessage = Component.text(killMessage, NamedTextColor.GRAY);
        this.tag = tag;
        this.materials = materials;
    }

    public Component getName() {
        return name;
    }

    public Component getDeathMessage() {
        return deathMessage;
    }

    public Component getKillMessage() {
        return killMessage;
    }

    public String getTag() {
        return tag;
    }

    public Set<Material> getMaterials() {
        return materials;
    }
}
