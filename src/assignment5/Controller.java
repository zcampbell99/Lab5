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

    public void testButton(ActionEvent ae) {
        System.out.println("test");
    }

    public void stepAction(ActionEvent ae) {
        Critter.worldTimeStep();
    }

    public void makeAction(ActionEvent ae) {

    }

    public void getCritters(ActionEvent ae) {

    }

}
