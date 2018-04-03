package assignment5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class Controller {
    private int quantityMake = 0;
    private int width = 0;
    private int height = 0;
    private int numSteps = 0;
    @FXML private TextField width_field;
    @FXML private TextField height_field;
    @FXML private TextField stepNum_field;
    @FXML private TextField makeNum_field;
    @FXML private ComboBox<String> types_of_critters_text;
    @FXML private ComboBox<String> stats_comboBox;
    @FXML private Button create_btn;
    @FXML private Button step1_btn;
    @FXML private Button step5_btn;
    @FXML private Button step50_btn;
    @FXML private Button step100_btn;
    @FXML private Button stepNum_btn;
    @FXML private Button seed_btn;
    @FXML private Button animStart_btn;
    @FXML private Button animStop_btn;
    @FXML private Button runStats_btn;
    @FXML private Slider anim_slider;


    // NOT DONE YET
    @FXML
    private void makeWorld(ActionEvent ae) {
        types_of_critters_text.getItems().addAll("Craig", "Algae", "AlgaephobicCritter", "Critter1", "Critter2", "Critter3", "Critter4", "TragicCritter");
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

    protected void disableAll() {
        step1_btn.setDisable(true);
        step5_btn.setDisable(true);
        step50_btn.setDisable(true);
        step100_btn.setDisable(true);
        seed_btn.setDisable(true);
        stepNum_btn.setDisable(true);
        types_of_critters_text.setDisable(true);
        makeNum_field.setDisable(true);
        anim_slider.setDisable(true);
        animStart_btn.setDisable(true);
        animStop_btn.setDisable(true);
        stats_comboBox.setDisable(true);
        runStats_btn.setDisable(true);
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
        String critterType = "";
        makeNum_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    makeNum_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        quantityMake = Integer.parseInt(makeNum_field.getText());
        try {
            critterType = getCritters(ae);
            if (makeNum_field.getText().length() != 0) {
                for (int i = 0; i < quantityMake; i++) {
                    Critter.makeCritter(critterType);
                }
            } else
                Critter.makeCritter(critterType);
        } catch (InvalidCritterException e) {
            System.out.println("error processing: " + critterType);
        }
    }

    // random seed for the world
    @FXML
    private void randomSeed(ActionEvent ae) {
        Critter.setSeed(Critter.getRandomInt(100));
    }

    private static String myPackage;
    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    // For the dropdown menu when making a critter
    @FXML
    private String getCritters(ActionEvent ae) throws InvalidCritterException {
        try {
            Class<?> critter = Class.forName(myPackage + ".Critter");
            Class<?> critter_class = Class.forName(myPackage + "." + types_of_critters_text.getEditor().getText()/* entered text? */);
            if (critter.isAssignableFrom(critter_class)) {
                types_of_critters_text.setValue(types_of_critters_text.getEditor().getText());
            } else
                throw new InvalidCritterException(types_of_critters_text.getEditor().getText());
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new InvalidCritterException(types_of_critters_text.getEditor().getText());
        }
        return types_of_critters_text.getValue();
    }

    // Quit button
    @FXML
    public void quitWorld(ActionEvent ae) {
        System.exit(0);
    }

}
