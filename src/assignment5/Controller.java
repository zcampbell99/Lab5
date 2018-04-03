package assignment5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Controller {
    private int quantityMake = 0;
    private int width = 0;
    private int height = 0;
    private int numSteps = 0;
    @FXML private TextField width_field;
    @FXML private TextField height_field;
    @FXML private TextField stepNum_field;
    @FXML private ComboBox<Critter> types_of_critters_text;
    @FXML private Button create_btn;

    @FXML
    private void makeWorld(ActionEvent ae) {
        width_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    width_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        height_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    height_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        width = Integer.parseInt(width_field.getText());
        height = Integer.parseInt(height_field.getText());
        System.out.println("Width = " + width);
        System.out.println("Height = " + height);
        Params.world_width = width;
        Params.world_height = height;
        Critter.displayWorld();                         // Need to update this to show the new window with the world
        width_field.setDisable(true);
        height_field.setDisable(true);
        create_btn.setDisable(true);
    }

    // Step the number of times defined in the text field
    @FXML
    private void stepNumAction(ActionEvent ae) {
        stepNum_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    stepNum_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        numSteps = Integer.parseInt(stepNum_field.getText());
        for (int i = 0; i < numSteps; i++) {
            Critter.worldTimeStep();
        }
        numSteps = 0;
    }

    @FXML
    private void step1Action(ActionEvent ae) {
        Critter.worldTimeStep();
    }

    @FXML
    private void step5Action(ActionEvent ae) {
        for (int i = 0; i < 5; i++) {
            Critter.worldTimeStep();
        }
    }

    @FXML
    private void step50Action(ActionEvent ae) {
        for (int i = 0; i < 50; i++) {
            Critter.worldTimeStep();
        }
    }

    @FXML
    private void step100Action(ActionEvent ae) {
        for (int i = 0; i < 100; i++) {
            Critter.worldTimeStep();
        }
    }

    // For the button "make"
    // Use with the critter returned from getCritters and the quantity from the quantity text field
    @FXML
    private void makeAction(ActionEvent ae) {

    }

    // random seed for the world
    @FXML
    private void randomSeed(ActionEvent ae) {
        Critter.setSeed(Critter.getRandomInt(100));
    }

    // For the dropdown menu when making a critter
    @FXML
    private void getCritters(ActionEvent ae) {

    }

    // Quit button
    @FXML
    public void quitWorld(ActionEvent ae) {
        System.exit(0);
    }

}
