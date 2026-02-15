package worm.me.utils.world;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PoissonDisk {

    public static List<Location> generatePoints(World world, Location corner1, Location corner2, double minDistance, double maxDistance, int maxCandidates) {
        double x1 = corner1.getX();
        double z1 = corner1.getZ();
        double x2 = corner2.getX();
        double z2 = corner2.getZ();

        List<Point2D> points2D = generate(x1, z1, x2, z2, minDistance, maxDistance, maxCandidates, new Random());

        List<Location> locations = new ArrayList<>();
        for (Point2D point : points2D) {
            locations.add(new Location(world, point.x, 0, point.z));
        }

        return locations;
    }

    private static List<Point2D> generate(double x1, double z1, double x2, double z2, double minDistance, double maxDistance, int k, Random random) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);

        double width = maxX - minX;
        double height = maxZ - minZ;

        double cellSize = minDistance / Math.sqrt(2);
        int gridWidth = (int) Math.ceil(width / cellSize);
        int gridHeight = (int) Math.ceil(height / cellSize);

        Point2D[][] grid = new Point2D[gridWidth][gridHeight];

        List<Point2D> activeList = new ArrayList<>();
        List<Point2D> points = new ArrayList<>();

        Point2D initialPoint = new Point2D(
                minX + random.nextDouble() * width,
                minZ + random.nextDouble() * height
        );

        points.add(initialPoint);
        activeList.add(initialPoint);
        addToGrid(grid, initialPoint, minX, minZ, cellSize);

        while (!activeList.isEmpty()) {

            int activeIndex = random.nextInt(activeList.size());
            Point2D activePoint = activeList.get(activeIndex);

            boolean foundPoint = false;

            for (int i = 0; i < k; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double radius = minDistance + random.nextDouble() * (maxDistance - minDistance);

                double newX = activePoint.x + radius * Math.cos(angle);
                double newZ = activePoint.z + radius * Math.sin(angle);

                if (newX < minX || newX >= maxX || newZ < minZ || newZ >= maxZ) {
                    continue;
                }

                Point2D candidate = new Point2D(newX, newZ);

                if (isValidPoint(grid, candidate, minX, minZ, cellSize, gridWidth, gridHeight, minDistance)) {
                    points.add(candidate);
                    activeList.add(candidate);
                    addToGrid(grid, candidate, minX, minZ, cellSize);
                    foundPoint = true;
                    break;
                }
            }

            if (!foundPoint) {
                activeList.remove(activeIndex);
            }
        }

        return points;
    }

    private static void addToGrid(Point2D[][] grid, Point2D point, double minX, double minZ, double cellSize) {
        int gridX = (int) ((point.x - minX) / cellSize);
        int gridZ = (int) ((point.z - minZ) / cellSize);
        grid[gridX][gridZ] = point;
    }

    private static boolean isValidPoint(Point2D[][] grid, Point2D point, double minX, double minZ,
                                        double cellSize, int gridWidth, int gridHeight, double minDistance) {
        int gridX = (int) ((point.x - minX) / cellSize);
        int gridZ = (int) ((point.z - minZ) / cellSize);

        int searchRadius = 2;
        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                int checkX = gridX + dx;
                int checkZ = gridZ + dz;

                if (checkX < 0 || checkX >= gridWidth || checkZ < 0 || checkZ >= gridHeight) {
                    continue;
                }

                Point2D neighbor = grid[checkX][checkZ];
                if (neighbor != null) {
                    double distance = point.distanceTo(neighbor);
                    if (distance < minDistance) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static class Point2D {
        public final double x;
        public final double z;

        public Point2D(double x, double z) {
            this.x = x;
            this.z = z;
        }

        public double distanceTo(Point2D other) {
            double dx = this.x - other.x;
            double dz = this.z - other.z;
            return Math.sqrt(dx * dx + dz * dz);
        }
    }
}