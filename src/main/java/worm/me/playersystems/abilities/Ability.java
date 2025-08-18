package worm.me.playersystems.abilities;

import org.bukkit.Material;
import worm.me.playersystems.data.levels.Reqs;

public enum Ability {

    EMPTY(Material.RED_STAINED_GLASS_PANE,
            "Empty",
            "Empty Slot",
            new Reqs.RequirementBuilder().combat(1).build(),
            0),
    DOUBLE_JUMP(Material.FEATHER,
            "Double Jump",
            "<dark_gray>Rapidly press space twice in the air to perform a double jump!<dark_gray>",
            new Reqs.RequirementBuilder().combat(1).build(),
            10),
    DASH(Material.IRON_SWORD,
            "Dash",
            "<dark_gray>Dash forward</dark_gray> <gray>[SNEAK] + [RIGHT-CLICK] + [Holding Sword]</gray>",
            new Reqs.RequirementBuilder().combat(5).agility(3).build(),
            10),
    LEAP(Material.IRON_AXE,
            "Leap",
            "<dark_gray>Leap up in the air</dark_gray> <gray>[SNEAK] + [RIGHT-CLICK] + [Holding Axe]</gray>",
            new Reqs.RequirementBuilder().combat(5).agility(8).build(),
            10),
    WALL_JUMP(Material.IRON_BARS,
            "Wall Jump",
            "<dark_gray>Clutch onto walls using</dark_gray> <gray>[SNEAK]</gray>",
            new Reqs.RequirementBuilder().combat(2000).build(),
            0),
    SCREAM(Material.GOAT_HORN,
            "SCREAM",
            "<dark_gray>Scream as loud as you can using</dark_gray> <gray>[SNEAK] + [LEFT-CLICK] + [Holding Note Block]</gray>",
            new Reqs.RequirementBuilder().combat(20).farming(10).fishing(15).agility(10).foraging(20).build(),
            15),
    SHADOW_STEP(Material.TINTED_GLASS,
            "Shadow-Step",
            "<dark_gray>Step through space using</dark_gray> <gray>[SNEAK] + [LEFT-CLICK] + [Holding Tinted Glass] + [Standing on Ground]</gray>",
            new Reqs.RequirementBuilder().combat(35).smithing(5).mining(5).agility(15).build(),
            20),
    SLIME_BOOTS(Material.SLIME_BALL,
            "Slime Boots",
            "<dark_gray>Bounce</dark_gray> <gray>[Wearing Slime Boots] ||| [SNEAK] to Deactivate</gray>",
            new Reqs.RequirementBuilder().combat(5).fishing(5).smithing(2).agility(10).build(),
            0),
    FEATHER_FLOAT(Material.ELYTRA,
            "Feather-Float",
            "<dark_gray>Float through the air swiftly using</dark_gray> <gray>[SNEAK] + [LEFT-CLICK] + [Holding Feather]</gray>",
            new Reqs.RequirementBuilder().farming(25).fishing(5).agility(10).build(),
            15),
    SPEED_STEP(Material.RABBIT_FOOT,
            "Speed-Step",
            "<dark_gray>Speed off in the direction you are looking using</dark_gray> <gray>[SNEAK] + [LEFT-CLICK] + [Holding Tripwire Hook]</gray>",
            new Reqs.RequirementBuilder().combat(5).mining(5).agility(25).foraging(10).build(),
            20),
    TACTICAL_RETREAT(Material.BOW,
            "Tactical Retreat",
            "<dark_gray>Gain some distance on your opponent using</dark_gray> <gray>[SNEAK] + [DROP] + [Holding Bow]</gray>",
            new Reqs.RequirementBuilder().combat(10).farming(1).mining(2).agility(10).foraging(2).build(),
            10),
    ABILITY_11(Material.ARROW,
            "Ability 11",
            "<dark_gray>Shoot a weak arrow and ride it using</dark_gray> <gray>[SNEAK] + [LEFT-CLICK] + [Holding Bow]</gray>",
            new Reqs.RequirementBuilder().combat(10).farming(1).mining(2).agility(10).foraging(2).build(),
            20);

    private final Material icon;
    private final String displayName;
    private final String description;
    private final int[] skillRequirements;
    private final int cooldownSeconds;

    Ability(Material icon, String displayName, String description, int[] skillRequirements, int cooldownSeconds) {
        this.icon = icon;
        this.displayName = displayName;
        this.description = description;
        this.skillRequirements = skillRequirements;
        this.cooldownSeconds = cooldownSeconds;
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
}