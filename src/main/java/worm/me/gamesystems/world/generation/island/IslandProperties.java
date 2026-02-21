package worm.me.gamesystems.world.generation.island;

import org.bukkit.Material;

import java.util.List;

public class IslandProperties {

    public enum IslandShape {
        //FLAT(1.5f/3.0f, 7.5f, 0.5f),
        STRAWBERRY(7.0f/12.0f, 4.5f, 1.8f),
        SPIKY(9.0f/18.0f, 6f, 1.8f);

        private final float lerpMix;
        private final float frequency;
        private final float amplifier;

        IslandShape(float lerpMix, float frequency, float amplifier){
            this.lerpMix = lerpMix;
            this.frequency = frequency;
            this.amplifier = amplifier;
        }

        public float getLerpMix() {
            return lerpMix;
        }

        public float getFrequency() {
            return frequency;
        }

        public float getAmplifier() {
            return amplifier;
        }



    }

    public enum IslandTopper {

        GRASSY(List.of(Material.GRASS_BLOCK), List.of(Material.DIRT, Material.COARSE_DIRT)),
        SANDY(List.of(Material.SAND, Material.SUSPICIOUS_SAND, Material.SMOOTH_SANDSTONE), List.of(Material.SAND, Material.SMOOTH_SANDSTONE, Material.SANDSTONE)),
        SNOWY(List.of(Material.SNOW_BLOCK), List.of(Material.DIRT, Material.COARSE_DIRT)),
        ROCKY(List.of(Material.GRAVEL, Material.STONE, Material.TUFF, Material.CYAN_TERRACOTTA), List.of(Material.STONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE)),
        MUSHROOM(List.of(Material.MYCELIUM), List.of(Material.DIRT, Material.COARSE_DIRT)),
        PODZOL(List.of(Material.PODZOL, Material.BROWN_TERRACOTTA, Material.COARSE_DIRT, Material.DIRT), List.of(Material.DIRT, Material.COARSE_DIRT));

        private final List<Material> top;
        private final List<Material> transition;

        IslandTopper(List<Material> top, List<Material> transition) {
            this.top = top;
            this.transition = transition;
        }

        public List<Material> getTopMaterials() {
            return top;
        }

        public List<Material> getTransitionMaterials() {
            return transition;
        }

    }

    public enum IslandBottom {

        STONE(List.of(Material.STONE, Material.TUFF, Material.CYAN_TERRACOTTA, Material.GRAY_CONCRETE)),
        GRANITE(List.of(Material.GRANITE, Material.TERRACOTTA)),
        ANDESITE(List.of(Material.LIGHT_GRAY_CONCRETE_POWDER, Material.ANDESITE, Material.LIGHT_GRAY_CONCRETE)),
        DIORITE(List.of(Material.WHITE_CONCRETE_POWDER, Material.CALCITE, Material.DIORITE)),
        DRIPSTONE(List.of(Material.JUNGLE_PLANKS, Material.BROWN_MUSHROOM_BLOCK, Material.PACKED_MUD, Material.LIGHT_GRAY_TERRACOTTA, Material.DRIPSTONE_BLOCK)),
        BASALT(List.of(Material.CRACKED_DEEPSLATE_TILES, Material.SMOOTH_BASALT, Material.COBBLED_DEEPSLATE, Material.DEEPSLATE));

        private final List<Material> gradientMaterials;


        IslandBottom(List<Material> gradientMaterials) {
            this.gradientMaterials = gradientMaterials;
        }

        public List<Material> getGradientMaterials() {
            return gradientMaterials;
        }
    }

    public enum IslandBiome {
        ;

        IslandBiome() {
        }
    }
}
