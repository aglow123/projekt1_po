package GrupaAA;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that aa.Vector2d and MoveDirection classes are defined.
 *
 * @author aglow123
 *
 */
public interface IWorldMap {
    /**
     * Places an animal on the map, add observer and update mapBoundary.
     *
     * @param animal
     *            The animal to place on the map.
     */
    void place(Animal animal);

    /**
     * Return true if given position on the map is occupied.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return true if given position on the map is planted.
     *
     * @param position
     *            Position to check.
     * @return True if object at given position is instance of Grass.
     */
    boolean isPlanted(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);

    /**
     * Remove dead animals from the map.
     */
    void cleanDeadAnimal();

}