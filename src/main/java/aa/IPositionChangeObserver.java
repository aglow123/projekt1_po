package aa;

public interface IPositionChangeObserver {

    /**
     * Change animals position, remove old one and add new in animal list.
     *
     * @param oldPosition
     * @param newPosition
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
