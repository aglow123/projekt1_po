package aa;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver{

    Comparator<Vector2d> compX = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if (o1.x < o2.x) {return -1;}
            if (o1.x > o2.x) {return 1;}
            return (int) Math.signum(o1.y - o2.y);
        }
    };

    Comparator<Vector2d> compY = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d o1, Vector2d o2) {
            if (o1.y < o2.y) {return -1;}
            if (o1.y > o2.y) {return 1;}
            return (int) Math.signum(o1.x - o2.x);
        }
    };

    private SortedSet<Vector2d> ox = new TreeSet<>(compX);
    private SortedSet<Vector2d> oy = new TreeSet<>(compY);


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        remove(oldPosition);
        add(newPosition);
    }

    private void add(Vector2d position) {
        ox.add(position);
        oy.add(position);
    }

    public void add(IMapElement object) {
        add(object.getPosition());
    }

    public void remove(Vector2d position) {
        ox.remove(position);
        oy.remove(position);
    }

    public Vector2d lowerLeft(){
        return new Vector2d(ox.first().x, oy.first().y);
    }

    public Vector2d upperRight(){
        return new Vector2d(ox.last().x, oy.last().y);
    }
}
