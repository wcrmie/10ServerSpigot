package worm.me.gamesystems.world.generation.poisson;

import org.bukkit.Location;
import org.bukkit.World;
import worm.me.Server;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PoissonDisc {

    public static ArrayList<Location> generatePoints(World world, Location corner1, Location corner2, double minDistance, double maxDistance, int maxCandidates, @Nullable List<Location> validLocations, @Nullable Integer amount, @Nullable Location startingPoint) {
        double x1 = Math.floor(corner1.getX());
        double z1 = Math.floor(corner1.getZ());
        double x2 = Math.floor(corner2.getX());
        double z2 = Math.floor(corner2.getZ());

        if (validLocations == null) {
            validLocations = new ArrayList<>();
        }

        List<Point2D> validPoints = validLocations.stream().map(loc -> new Point2D(Math.floor(loc.getX()), Math.floor(loc.getZ()))).toList();

        for (Point2D point : validPoints) {
            Server.getPlugin().getLogger().info(point.x + " " + point.z);
        }

        Point2D startingPoint2D;

        if (startingPoint == null) startingPoint2D = null;
        else startingPoint2D = new Point2D(Math.floor(startingPoint.getX()), Math.floor(startingPoint.getZ()));

        List<Point2D> points2D = generate(x1, z1, x2, z2, minDistance, maxDistance, maxCandidates, validPoints, amount, startingPoint2D);

        ArrayList<Location> locations = new ArrayList<>();
        for (Point2D point : points2D) {
            locations.add(new Location(world, point.x, 0, point.z));
        }

        return locations;
    }

    private static boolean invalidPoint(List<Point2D> validPoints, Point2D point) {

        for (var pointA : validPoints) {

            if (pointA.equals(point)) return false;

        }

        return true;
    }

    /**
     *
     * @param x1 x-coordinate of corner 1
     * @param z1 z-coordinate of corner 1
     *
     * @param x2 x-coordinate of corner 2
     * @param z2 z-coordinate of corner 2
     *
     * @param minDistance minimum distance between points
     * @param maxDistance maximum distance between points
     *
     * @param maxCandidates max attempts before an active point is dropped (higher values mean more even dist.)
     *
     * @param validPoints list of points that will be kept if they are generated
     *
     *
     * @return list of generated points
     *
     */
    private static List<Point2D> generate(double x1, double z1, double x2, double z2, double minDistance, double maxDistance, int maxCandidates, List<Point2D> validPoints, Integer amount, @Nullable Point2D startingPoint) {
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

        Point2D initialPoint = startingPoint;

        while (initialPoint == null) {

            double x = minX + ThreadLocalRandom.current().nextDouble() * width;
            double z = minZ + ThreadLocalRandom.current().nextDouble() * height;

            initialPoint = new Point2D(

                    (Math.floor(x)),
                    (Math.floor(z))

            );

            if (validPoints.isEmpty()) break;

            if (invalidPoint(validPoints, initialPoint)) initialPoint = null;


        }

        points.add(initialPoint);
        activeList.add(initialPoint);
        addToGrid(grid, initialPoint, minX, minZ, cellSize);

        while (!activeList.isEmpty() && (amount == null || points.size() < amount)) {

            int activeIndex = ThreadLocalRandom.current().nextInt(activeList.size());
            Point2D activePoint = activeList.get(activeIndex);

            boolean foundPoint = false;

            for (int i = 0; i < maxCandidates; i++) {
                double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
                double radius = minDistance + ThreadLocalRandom.current().nextDouble() * (maxDistance - minDistance);

                double newX = activePoint.x + radius * Math.cos(angle);
                double newZ = activePoint.z + radius * Math.sin(angle);

                if (newX < minX || newX >= maxX || newZ < minZ || newZ >= maxZ) {
                    continue;
                }

                Point2D candidate = new Point2D(Math.floor(newX), Math.floor(newZ));

                if (!validPoints.isEmpty() && invalidPoint(validPoints, candidate)) continue;

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
        public double x;
        public double z;

        public Point2D(double x, double z) {
            this.x = x;
            this.z = z;
        }

        public double distanceTo(Point2D other) {
            double dx = this.x - other.x;
            double dz = this.z - other.z;
            return Math.sqrt(dx * dx + dz * dz);
        }

        public boolean equals(Point2D other) {

            return this.x == other.x && this.z == other.z;

        }
    }
}
