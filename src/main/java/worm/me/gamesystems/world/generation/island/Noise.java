package worm.me.gamesystems.world.generation.island;

import java.util.concurrent.ThreadLocalRandom;

public class Noise {

    /** Fills a 2D array with noise values
     * @param size The diameter of the island you are trying to generate
     * @param shape The shape of the island
     * @return A 2D array of floats representing noise values
     */
    public static float[][] fillTaperedPerlinArray (int size, IslandProperties.IslandShape shape) {
        float[][] arr = new float[size][size];

        worm.me.utils.FastNoiseLite noise = new worm.me.utils.FastNoiseLite();
        noise.SetNoiseType(worm.me.utils.FastNoiseLite.NoiseType.Perlin);
        noise.SetSeed(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE));

        float frequency = shape.getFrequency();

        noise.SetFrequency(frequency);

        int radius = size / 2;

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {

                double distance = radius - (radius - (float) Math.sqrt(Math.pow(x - radius, 2) + Math.pow(z - radius, 2)));
                double taper = distance / size;

                float nx = (float) ((double) x /size - 0.5);
                float nz = (float) ((double) z /size - 0.5);
                arr[x][z] = (float) (noise.GetNoise(nx, nz) - taper);
            }
        }

        return arr;

    }

}
