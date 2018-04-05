/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

package assignment5;

import javafx.scene.paint.Color;

public class Critter4 extends Critter {
    // Gorilla Critter

    @Override
    public String toString() { return "4"; }

    private int dir;

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return Color.GREENYELLOW;
    }

    @Override
    public javafx.scene.paint.Color viewFillColor() {
        return Color.GREENYELLOW;
    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.SQUIGGLE;
    }

    /**
     * Critter4 Constructor
     * Set the direction randomly, set the start energy, and it has not reproduced yet
     */
    public Critter4() {
        dir = Critter.getRandomInt(8);
    }

    /**
     * Determine if Critter4 should fight another critter
     * If the opponent is another Critter4 then it wont fight
     * @param opponent is the critter that this critter has encountered
     * @return true if this critter wants to fight
     */
    public boolean fight(String opponent) {
        if (opponent.equals("4") || opponent.equals("C")) {
//            for(int i=0; i<8; i++){
//                String occupied = look(this.dir, true);
//                if(occupied.equals(null)){  //if the destination is unoccupied, run away
//                    run(this.dir);
//                    break;
//                }
//            }
            return false;
        }
        return true;
    }

    /**
     * Override the abstract doTimeStep() method in Critter
     * Will only move half of the time
     * When it moves, it moves 3 spaces with the same cost as a walk
     * Energy to reproduce is 100
     */
    @Override
    public void doTimeStep() {
        int moving = Critter.getRandomInt(10);
        if (moving <= 4) {
            setEnergy(getEnergy()-Params.rest_energy_cost);
        } else if (moving <= 7) {
            this.setEnergy(this.getEnergy() + Params.run_energy_cost);
            run(Critter.getRandomInt(8));
            walk(Critter.getRandomInt(8));
        } else {
            run(Critter.getRandomInt(8));
        }
        if (getEnergy() >= 100) {
            Critter4 child = new Critter4();
            reproduce(child, Critter.getRandomInt(8));
        }
    }

    public static String runStats2(java.util.List<Critter> critter4) {
        return "There are " + critter4.size() + " Critter4s \n" + "Critter4s have traveled a distance of " + distanceTravelled + " spaces";
    }

    public static void runStats(java.util.List<Critter> critter4) {
        System.out.println("There are " + critter4.size() + " Critter4s");
        System.out.println("Critter4s have traveled a distance of " + distanceTravelled + " spaces");
    }
}
