package GrupaAA;

public class HealthPoints {
    public int HP;

    public HealthPoints(int HP){
        this.HP = HP;
    }

    public int addHP(int HP){
        return this.HP += HP;
    }

    public int removeHP(int HP){
        return this.HP -= HP;
    }
}
