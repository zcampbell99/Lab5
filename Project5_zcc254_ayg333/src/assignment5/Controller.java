package assignment5;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
//import org.controlsfx.control.CheckListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

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
    @FXML private Button create_btn;
    @FXML private Button step1_btn;
    @FXML private Button step5_btn;
    @FXML private Button step50_btn;
    @FXML private Button step100_btn;
    @FXML private Button stepNum_btn;
    @FXML private Button seed_btn;
    @FXML private Button animStart_btn;
    @FXML private Button animStop_btn;
    @FXML private Button make_btn;
    protected Stage stage;
    @FXML private Slider anim_slider;
    @FXML private Label error_label;
    @FXML private MenuButton split_menu;
    @FXML private TextArea stats_textArea;
    private ArrayList<String> statsEnabled = new ArrayList<>();
    private Timeline timeline = new Timeline();

    /**
     * Initialize the world. Set all the lists of critters based on the files in the current package
     * Set some of the text fields to only accept integers as input
     * Disable some buttons until the world is created
     */
    public void initialize() {
        for (String s : critters()) {
            types_of_critters_text.getItems().add(s);
            MenuItem m = new MenuItem(s);
            m.setId(s + "_menu");
            split_menu.getItems().add(m);
        }
        initSplitMenu();
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
        makeNum_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    makeNum_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        stepNum_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    stepNum_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        disableAll();
    }

    /**
     * The method for when the create button is pressed
     * Sets the Params of the world and creates a new window to display the world grid
     * @param ae is the action of pressing the create button
     * @throws IOException
     */
    @FXML
    private void makeWorld(ActionEvent ae) throws IOException{
        width = Integer.parseInt(width_field.getText());
        height = Integer.parseInt(height_field.getText());
        Params.world_width = width;
        Params.world_height = height;
        width_field.setDisable(true);
        height_field.setDisable(true);
        create_btn.setDisable(true);
        enableAll();

        // Create the new window for the grid
        Parent world = FXMLLoader.load(getClass().getResource("World.fxml"));
        Stage worldStage = new Stage();
        worldStage.setTitle("The Land of Critters");
        Scene gridScene = new Scene(world, (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        worldStage.setScene(gridScene);
        stage = worldStage;
        worldStage.show();
    }

    /**
     * Step the number of times defined in the text field adjacent to it
     * @param ae is the action of pressing the button
     */
    @FXML
    private void stepNumAction(ActionEvent ae) {
        if (stepNum_field.getText().equals("")) {
            Critter.worldTimeStep();
            return;
        }
        numSteps = Integer.parseInt(stepNum_field.getText());
        for (int i = 0; i < numSteps; i++) {
            Critter.worldTimeStep();
        }
        numSteps = 0;
    }

    /**
     * Enable certain components after the world is created
     */
    private void enableAll() {
        stepNum_field.setDisable(false);
        make_btn.setDisable(false);
        step1_btn.setDisable(false);
        step5_btn.setDisable(false);
        step50_btn.setDisable(false);
        step100_btn.setDisable(false);
        seed_btn.setDisable(false);
        stepNum_btn.setDisable(false);
        types_of_critters_text.setDisable(false);
        makeNum_field.setDisable(false);
        anim_slider.setDisable(false);
        animStart_btn.setDisable(false);
        animStop_btn.setDisable(false);
    }

    /**
     * Disable certain components until the world is created
     */
    private void disableAll() {
        stepNum_field.setDisable(true);
        make_btn.setDisable(true);
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
    }


    /**
     * Do one worldTimeStep with the press of this button
     * @param ae is the action of the button being pressed
     */
    @FXML
    private void step1Action(ActionEvent ae) {
        Critter.worldTimeStep();
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Do 5 time steps with the press of this button
     * @param ae is the action of the button being pressed
     */
    @FXML
    private void step5Action(ActionEvent ae) {
        for (int i = 0; i < 5; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Do 50 time steps when this button is pressed
     * @param ae is the action of the button being pressed
     */
    @FXML
    private void step50Action(ActionEvent ae) {
        for (int i = 0; i < 50; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Do 100 time steps when this button is pressed
     * @param ae is the action of the button being pressed
     */
    @FXML
    private void step100Action(ActionEvent ae) {
        for (int i = 0; i < 100; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create new critter(s) using the drop down menu of critters or typing in a type of critter
     * Use of the text field to determine how many new critters are created is optional
     * A blank text field will create one new critter
     * @param ae is the action of pressing the make button
     */
    @FXML
    private void makeAction(ActionEvent ae) {
        String critterType = "";
        if (makeNum_field.getText().equals("")) {
            quantityMake = 0;
        } else {
            quantityMake = Integer.parseInt(makeNum_field.getText());
        }
        try {
            critterType = getCritters(ae);
            if (critterType == null) {
                error_label.setText("Please enter a valid critter type");
                return;
            }
            if (makeNum_field.getText().length() != 0) {
                for (int i = 0; i < quantityMake; i++) {
                    Critter.makeCritter(critterType);
                    error_label.setText(null);
                }
            } else {
                Critter.makeCritter(critterType);
                error_label.setText(null);
            }
        } catch (InvalidCritterException e) {
            error_label.setText("Error processing: " + critterType);
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Set a random seed for the world by pressing this button
     * @param ae is the action of the button being pressed
     */
    @FXML
    private void randomSeed(ActionEvent ae) {
        Critter.setSeed(Critter.getRandomInt(100));
    }

    private static String myPackage;
    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }


    /**
     * Gets the type of critter from the dropdown menu when making a new critter
     * @param ae is the action of selecting a critter from the dropdown menu
     * @return a string of the selected critter type
     */
    @FXML
    private String getCritters(ActionEvent ae){
        return types_of_critters_text.getValue();
    }

    /**
     * Add the stats of the critter given by critterType into the text area in the stats block of the UI
     * @param critterType is the name of the critter whose stats we want
     */
    private void statsAdd(String critterType) {
        String stats;
        if (critterType == null) {
            //stats_label.setText("Select a valid critter type");
            return;
        }
        try {
            List<Critter> listOfCrits = Critter.getInstances(critterType);
            if(listOfCrits.size() != 0) {   //if the critter class already exists then runStats
                Class<?> critter_class = listOfCrits.get(0).getClass();
                Method runs = critter_class.getMethod("runStats2", java.util.List.class);
                stats = (String)runs.invoke(null,listOfCrits);
            } else {
                if (critterType.equals("Algae"))
                    stats = "No critters of type @ exist.";
                else
                    stats = "No critters of type " + critterType + " exist.";
            }
            /*Update UI with the stats info*/
            String text = stats_textArea.getText();
            stats_textArea.setText(text + stats + "\n");
        } catch (InvalidCritterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoClassDefFoundError e) {
            System.out.println("error processing: " + critterType);
        }
    }

    /**
     * Get the list of viable critters from the current package
     * @return an ArrayList of Strings containing the names of the acceptable critter types
     */
    private ArrayList<String> critters() {
        ArrayList<String> critterList = new ArrayList<>();
        File folder = new File("./src/assignment5");
        String[] srcFiles = folder.list();
        try {
            Class<?> critterClass = Class.forName("assignment5.Critter");
            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].length() >= 5) {
                    String className = srcFiles[i].substring(0, srcFiles[i].length() - 5);
                    String qualifiedName = "assignment5." + className;
                    try {
                        Class<?> c = Class.forName(qualifiedName);
                        if (critterClass.isAssignableFrom(c)) {
                            critterList.add(className);
                        }
                    } catch (ClassNotFoundException e) {
                    }

                }
            }
        } catch (ClassNotFoundException e) {}
        critterList.remove("Critter");
        return critterList;
    }

    /**
     * Set up the handlers for the menu to determine which critter's stats are visible
     */
    private void initSplitMenu() {
        stats_textArea.setWrapText(false);
        stats_textArea.setPrefRowCount(split_menu.getItems().size());
        for (MenuItem m : split_menu.getItems()) {
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if (!statsEnabled.contains(m.getText())) {
                        statsEnabled.add(m.getText());
                        statsAdd(m.getText());
                    } else {
                        statsEnabled.remove(m.getText());
                        statsRemove(m.getText());
                    }
                }
            });
        }
    }

    /**
     * Remove the text representing the stats of the critter given by s
     * @param s is the type of critter to remove stats
     */
    private void statsRemove(String s) {
        String[] lines = stats_textArea.getText().split("\n");
        stats_textArea.clear();
        int remLine = 0;
        String searchFor = s;
        if (s.equals("Algae"))
            searchFor = "@";
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(searchFor)) {
                remLine = i;
                break;
            }
        }
        if (remLine == 0) {
            String text = "";
            for (int i = 1; i < lines.length; i++) {
                text = text + lines[i] + "\n";
                stats_textArea.setText(text);
            }
        } else {
            String text = "";
            for (int i = 0; i < remLine; i++) {
                text = text + lines[i] + "\n";
                stats_textArea.setText(text);
            }
            for (int j = remLine + 1; j < lines.length; j++) {
                text = text + lines[j] + "\n";
                stats_textArea.setText(text);
            }
        }
    }

    /**
     * The button to start the animation using a new KeyFrame and playing through a timeline
     * @param ae is the action of pressing the start button for the animation
     */
    @FXML
    private void animStart(ActionEvent ae) {
        disableAll();
        animStop_btn.setDisable(false);
        anim_slider.setDisable(false);
        double timeEst = updateSlider(ae);
        KeyFrame k = new KeyFrame(Duration.millis((100*timeEst)/3), e -> animation(ae));
        timeline.getKeyFrames().add(k);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * The button to stop the animation started in animStart
     * @param ae is the action of pressing the stop button
     */
    @FXML
    private void animStop(ActionEvent ae) {
        timeline.stop();
        enableAll();
    }

    /**
     * The slider through which the speed of the animation is determined
     * @param ae is the action of moving the slider
     * @return the value of the slider's current position
     */
    @FXML
    private double updateSlider(ActionEvent ae) {
        while (!anim_slider.isValueChanging()) {
            return anim_slider.getValue();
        }
        return 1;
    }

    /**
     * Animate the grid and update the stats of the desired critters
     * @param ae is the action of animation
     */
    private void animation(ActionEvent ae) {
        double animSpeed = updateSlider(ae);
        for (int i = 0; i < animSpeed; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
        for (String s : statsEnabled) {
            statsRemove(s);
            statsAdd(s);
        }
    }

    /**
     * The button used to terminate the program
     * @param ae is the action of pressing the button
     */
    @FXML
    private void quitWorld(ActionEvent ae) {
        System.exit(0);
    }

}
