package worm.me.playersystems.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import worm.me.playersystems.abilities.active.*;
import worm.me.data.levels.Reqs;

import java.util.function.Consumer;

public enum Ability {

    EMPTY(0,
            Material.RED_STAINED_GLASS_PANE,
            "Empty",
            "Empty Slot",
            new Reqs.RequirementBuilder().combat(1).build(),
            0,
            AbilityType.OTHER,
            player -> {}),

    DOUBLE_JUMP(1,
            Material.FEATHER,
            "Double Jump",
            "<dark_gray>double jump!<dark_gray>",
            new Reqs.RequirementBuilder().combat(1).build(),
            8,
            AbilityType.DOUBLE_SPACE,
            player -> new DoubleJump().doubleJump(player)),

    DASH(2,
            Material.IRON_SWORD,
            "Dash",
            "<dark_gray>Dash forward</dark_gray>",
            new Reqs.RequirementBuilder().combat(5).agility(3).build(),
            12,
            AbilityType.HORIZONTAL,
            player -> new Dash().dash(player)),

    LEAP(3,
            Material.IRON_AXE,
            "Leap",
            "<dark_gray>Leap up in the air</dark_gray>",
            new Reqs.RequirementBuilder().combat(5).agility(8).build(),
            12,
            AbilityType.VERTICAL,
            player -> new Leap().leap(player)),

    WALL_JUMP(4,
            Material.IRON_BARS,
            "Wall Jump",
            "<dark_gray>Clutch onto walls</dark_gray>",
            new Reqs.RequirementBuilder().combat(2000).build(),
            0,
            AbilityType.DOUBLE_SPACE,
            player -> new DoubleJump().doubleJump(player)), // place holder

    SCREAM(5,
            Material.GOAT_HORN,
            "SCREAM",
            "<dark_gray>Scream as loud as you can</dark_gray>",
            new Reqs.RequirementBuilder().combat(20).farming(10).fishing(15).agility(10).foraging(20).build(),
            15,
            AbilityType.OTHER,
            player -> new Scream().scream(player)),

    SHADOW_STEP(6,
            Material.TINTED_GLASS,
            "Shadow-Step",
            "<dark_gray>Step through space</dark_gray>",
            new Reqs.RequirementBuilder().combat(35).smithing(5).mining(5).agility(15).build(),
            16,
            AbilityType.HORIZONTAL,
            player -> new ShadowStep().shadowStep(player)),

    FEATHER_FLOAT(8,
            Material.ELYTRA,
            "Feather-Float",
            "<dark_gray>Float through the air swiftly</dark_gray>",
            new Reqs.RequirementBuilder().farming(25).fishing(5).agility(10).build(),
            16,
            AbilityType.VERTICAL,
            player -> new FeatherDash().featherDash(player)),

    SPEED_STEP(9,
            Material.RABBIT_FOOT,
            "Speed-Step",
            "<dark_gray>Speed off in the direction you are looking</dark_gray>",
            new Reqs.RequirementBuilder().combat(5).mining(5).agility(25).foraging(10).build(),
            24,
            AbilityType.HORIZONTAL,
            player -> new SpeedStep().SpeedStep(player)),

    TACTICAL_RETREAT(10,
            Material.BOW,
            "Tactical Retreat",
            "<dark_gray>Gain some distance on your opponent</dark_gray>",
            new Reqs.RequirementBuilder().combat(10).farming(1).mining(2).agility(10).foraging(2).build(),
            8,
            AbilityType.HORIZONTAL,
            player -> new TacticalRetreat().retreat(player)),

    ABILITY_11(11,
            Material.ARROW,
            "Ability 11",
            "<dark_gray>Shoot a weak arrow and ride it</dark_gray>",
            new Reqs.RequirementBuilder().combat(10).farming(1).mining(2).agility(10).foraging(2).build(),
            20,
            AbilityType.VERTICAL,
            player -> new RideArrow().shootRiddenArrow(player)),

    SLIDE(12,
          Material.ICE,
          "Slide",
          "<dark_gray>Slide all about</dark_gray>",
          new Reqs.RequirementBuilder().agility(15).fishing(5).build(),
          8,
          AbilityType.DOUBLE_SPACE,
          player -> new Slide().slide(player)),
    HEAVY_MINER(13,
          Material.COPPER_ORE,
          "Heavy Miner",
                  "<dark_gray>Mine extra fast but its very hard to move and see</dark_gray>",
                  new Reqs.RequirementBuilder().combat(2000).mining(30).foraging(30).build(),
          30,
    AbilityType.VERTICAL,
    player -> new HeavyMiner().heavyMiner(player)),
    Zig_zag(14,
                Material.MAGENTA_GLAZED_TERRACOTTA,
          "Zig zag",
                        "<dark_gray>zip to the right and left</dark_gray>",
                        new Reqs.RequirementBuilder().combat(20).agility(45).build(),
          15,
    AbilityType.HORIZONTAL,
            player -> new ZigZag().zigzag(player));


    private final int id;
    private final Material icon;
    private final String displayName;
    private final String description;
    private final int[] skillRequirements;
    private final int cooldownSeconds;
    private final AbilityType type;
    private final Consumer<Player> executor;

    public static final int SLOT_COUNT = 3;
    public static final int[] DEFAULT_ACTIVE_ABILITIES = new int[SLOT_COUNT];
    public static final long[] DEFAULT_COOLDOWNS = new long[SLOT_COUNT];
    public static final int[] ABILITY_SLOT_POSITIONS = {10, 13, 16};

    Ability(int id, Material icon, String displayName, String description, int[] skillRequirements,
            int cooldownSeconds, AbilityType type, Consumer<Player> executor) {
        this.id = id;
        this.icon = icon;
        this.displayName = displayName;
        this.description = description;
        this.skillRequirements = skillRequirements;
        this.cooldownSeconds = cooldownSeconds;
        this.type = type;
        this.executor = executor;
    }

    public int getId() {
        return id;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int[] getSkillRequirements() {
        int[] copy = new int[skillRequirements.length];
        System.arraycopy(skillRequirements, 0, copy, 0, skillRequirements.length);
        return copy;
    }

    public boolean hasCooldown() {
        return cooldownSeconds > 0;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public AbilityType getType() {
        return type;
    }

    public void execute(Player player) {
        executor.accept(player);
    }

    public static Ability getById(int id) {
        for (Ability ability : values()) {
            if (ability.getId() == id) {
                return ability;
            }
        }
        return null;
    }

    public static AbilityType getRequiredTypeForSlot(int slot) {
        switch (slot) {
            case 0: return AbilityType.DOUBLE_SPACE;
            case 1: return AbilityType.HORIZONTAL;
            case 2: return AbilityType.VERTICAL;
            default: return AbilityType.OTHER;
        }
    }
}