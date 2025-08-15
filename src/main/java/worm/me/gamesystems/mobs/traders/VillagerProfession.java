package worm.me.gamesystems.mobs.traders;

public enum VillagerProfession {
    ARMORER("Armorer"),
    BUTCHER("Butcher"),
    CARTOGRAPHER("Cartographer"),
    CLERIC("Cleric"),
    FARMER("Farmer"),
    FISHERMAN("Fisherman"),
    FLETCHER("Fletcher"),
    LEATHERWORKER("Leatherworker"),
    LIBRARIAN("Librarian"),
    MASON("Mason"),
    SHEPHERD("Shepherd"),
    TOOLSMITH("Toolsmith"),
    WEAPONSMITH("Weaponsmith");

    private final String professionName;

    VillagerProfession(String professionName) {
        this.professionName = professionName;
    }

    public String getName() {
        return professionName;
    }
}
