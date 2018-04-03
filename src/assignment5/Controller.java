package assignment5;

import javafx.event.ActionEvent;

public class Controller {
    private int quantityMake = 0;
    private int width = 0;
    private int height = 0;

    // Not sure what to put here just yet
    public void makeWorld(ActionEvent ae) {
        Critter.displayWorld();
    }

    // One standard world time step on the step button
    public void stepAction(ActionEvent ae) {
        Critter.worldTimeStep();
    }

    // For the button "make"
    // Use with the critter returned from getCritters and the quantity from the quantity text field
    public void makeAction(ActionEvent ae) {

    }

    // random seed for the world
    public void randomSeed(ActionEvent ae) {
        Critter.setSeed(Critter.getRandomInt(100));
    }

    // For the dropdown menu when making a critter
    public void getCritters(ActionEvent ae) {

    }

}
