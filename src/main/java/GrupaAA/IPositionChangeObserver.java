package GrupaAA;

public interface IPositionChangeObserver {

    /**
     * Change animals position, remove old one and add new in animal list.
     *
     * @param oldPosition position to remove
     * @param newPosition position to add
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}