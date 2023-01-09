package GrupaAA;

import java.util.ArrayList;
import java.util.Random;

public abstract class SimulationEngine implements IEngine, Runnable{
    String whichBehavior;
    int minMutation;
    int maxMutation;
    String whichMutation;
    int dailyNewGrass;
    int plantEnergy;

    protected GrassField map;
    protected Vector2d[] positions;

    public SimulationEngine(GrassField map, Vector2d[] positions, int initHP, int birthCost, int minHP, int genLength){
        this.map = map;
        this.positions = positions;
        for (Vector2d position: positions) {
            new Animal(map, position, initHP, birthCost, minHP, genLength);
        }
    }

    @Override
    abstract public void run();

    public ArrayList<Animal> findWinner(ArrayList<Animal> list, int numOfWinners){
        if(list.size() == numOfWinners){
            return list;
        }
        ArrayList<Animal> maxHPAnimals = findMaxHP(list);
        if(maxHPAnimals.size() == numOfWinners){
            return maxHPAnimals;
        }
        else if(maxHPAnimals.size() < numOfWinners){
            ArrayList<Animal> listCopy = (ArrayList<Animal>) list.clone();
            for(Animal animal: maxHPAnimals){
                listCopy.remove(animal);
            }
            maxHPAnimals.addAll(findWinner(listCopy, numOfWinners - maxHPAnimals.size()));
            return maxHPAnimals;
        }
        else{
            ArrayList<Animal> maxAgeAnimals = findMaxAge(maxHPAnimals);
            if(maxAgeAnimals.size() == numOfWinners){
                return maxAgeAnimals;
            }
            else if(maxAgeAnimals.size() < numOfWinners){
                for(Animal animal: maxAgeAnimals){
                    maxHPAnimals.remove(animal);
                }
                maxAgeAnimals.addAll(findWinner(maxHPAnimals, numOfWinners - maxAgeAnimals.size()));
                return maxAgeAnimals;
            }
            else{
                ArrayList<Animal> maxChildrenAnimals = findMaxChildren(maxAgeAnimals);
                if(maxChildrenAnimals.size() == numOfWinners){
                    return maxChildrenAnimals;
                }
                else if(maxChildrenAnimals.size() > numOfWinners){
                    for(int i=0; i<maxChildrenAnimals.size()-numOfWinners; i++){
                        maxChildrenAnimals.remove(new Random().nextInt(maxChildrenAnimals.size()));
                    }
                    return maxChildrenAnimals;
                }
                else{
                    for(Animal animal: maxChildrenAnimals) {
                        maxAgeAnimals.remove(animal);
                    }
                    maxChildrenAnimals.addAll(findWinner(maxAgeAnimals,numOfWinners- maxChildrenAnimals.size()));
                    return maxChildrenAnimals;
                }
            }
        }
    }

    public ArrayList<Animal> findMaxHP(ArrayList<Animal> list){
        int maxHP = 0;
        ArrayList<Animal> maxHPAnimals = new ArrayList<>();
        for(Animal animal: list){
            if(animal.HP > maxHP){
                maxHP = animal.HP;
                maxHPAnimals.clear();
                maxHPAnimals.add(animal);
            }
            else if(animal.HP == maxHP){
                maxHPAnimals.add(animal);
            }
        }
        return maxHPAnimals;
    }

    public ArrayList<Animal> findMaxAge(ArrayList<Animal> list){
        int maxAge = 0;
        ArrayList<Animal> maxAgeAnimals = new ArrayList<>();
        for(Animal animal: list){
            if(animal.age > maxAge){
                maxAge = animal.age;
                maxAgeAnimals.clear();
                maxAgeAnimals.add(animal);
            }
            else if(animal.age == maxAge){
                maxAgeAnimals.add(animal);
            }
        }
        return maxAgeAnimals;
    }

    public ArrayList<Animal> findMaxChildren(ArrayList<Animal> list){
        int maxChildren = 0;
        ArrayList<Animal> maxChildrenAnimals = new ArrayList<>();
        for(Animal animal: list){
            if(animal.children > maxChildren){
                maxChildren= animal.children;
                maxChildrenAnimals.clear();
                maxChildrenAnimals.add(animal);
            }
            else if(animal.children == maxChildren){
                maxChildrenAnimals.add(animal);
            }
        }
        return maxChildrenAnimals;
    }
}
