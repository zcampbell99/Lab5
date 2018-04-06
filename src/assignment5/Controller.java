package assignment5;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
//import org.controlsfx.control.CheckListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private Timeline timeline = new Timeline();

//    @FXML private CheckListView<String> checkListView;

    // Runs on startup
    public void initialize() {
        for (String s : critters()) {
            types_of_critters_text.getItems().add(s);
            // checkListView stuff
        }
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

    // Step the number of times defined in the text field
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

    // Enable components after world is created
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

    // Disable certain components at startup
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

    @FXML
    private void step1Action(ActionEvent ae) {
        Critter.worldTimeStep();
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void step5Action(ActionEvent ae) {
        for (int i = 0; i < 5; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void step50Action(ActionEvent ae) {
        for (int i = 0; i < 50; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void step100Action(ActionEvent ae) {
        for (int i = 0; i < 100; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
    }

    // For the button "make"
    // Use with the critter returned from getCritters and the quantity from the quantity text field
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
    private String getCritters(ActionEvent ae){
        return types_of_critters_text.getValue();
    }

    @FXML
    private void statsAction(ActionEvent ae) {
        String critterType = ""; //getStats(ae);
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
                stats = "No critters of type " + critterType + " exist.";
            }
            /*Update UI with the stats info*/
            //stats_label.setText(stats);
        } catch (InvalidCritterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoClassDefFoundError e) {
            System.out.println("error processing: " + critterType);
        }
    }

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


//    @FXML
//    private String getStats(ActionEvent ae) {
//        return stats_comboBox.getValue();
//    }

    @FXML
    private void animStart(ActionEvent ae) {
        disableAll();
        animStop_btn.setDisable(false);
        anim_slider.setDisable(false);
        double timeEst = updateSlider(ae);
        KeyFrame k = new KeyFrame(Duration.millis((100*timeEst)/3),
                e -> animation(ae));
        timeline.getKeyFrames().add(k);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        // Run the world at time given by slider
//
//        if(animStop_btn.isPressed()){
//            timeline.stop();
//            enableAll();
//        }
    }

    @FXML
    private void animStop(ActionEvent ae) {
        if (!timeline.equals(null)) {
            timeline.stop();
        }
        enableAll();
    }

    @FXML
    private double updateSlider(ActionEvent ae) {
        while(!anim_slider.isValueChanging()){
            return anim_slider.getValue();
        }
        return 1;
    }

    @FXML
    private void animation(ActionEvent ae){
        double animSpeed = updateSlider(ae);
        for (int i = 0; i < animSpeed; i++) {
            Critter.worldTimeStep();
        }
        Scene scene = new Scene(Critter.displayGUIWorld(), (Params.world_width*worldController.boxSize)+15, (Params.world_height*worldController.boxSize)+15);
        stage.setScene(scene);
        stage.show();
        //TODO add runstats
    }

    // Quit button
    @FXML
    public void quitWorld(ActionEvent ae) {
        System.exit(0);
    }



}
