package assignment5;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application {
	private static final int width = 415;
	private static final int height = 560;
	private GraphicsContext gc;
	private ColorPicker colorPicker;
	private Color color = Color.BLACK;
	private Button clrButton;
	BorderPane pane;


	static Scanner kb;	// scanner connected to keyboard input, or input file
	private static String inputFile;	// input file, used instead of keyboard input if specified
	static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
	private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
	static PrintStream old = System.out;	// if you want to restore output to console


	// Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 * Main method.
	 * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
	 * and the second is test (for test output, where all output to be directed to a String), or nothing.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SceneBuilderFile.fxml"));
		stage.setTitle("CritterWorld Controls");
		Controller.disableAll();
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Takes in user input as commands and does the appropriate actions
	 * @param keyboard is the scanned input
	 * @return input array which determines if user input "quit"
	 */
	// Testing committing
	public static ArrayList<String> parse(Scanner keyboard) {
		String s = kb.nextLine();
		String[] command = s.split("\\s+"); //split command up into separate words
		ArrayList<String> input = new ArrayList<>();
		String quit = "quit";
		String show = "show";
		String step = "step";
		String make = "make";
		String seed = "seed";
		String stats = "stats";
		String first = command[0];
		if (first.equals(quit)) {         //return empty array if user inputs "/quit"
			return input;   //input remains empty if user inputs quit
		}
		else {
			input.add(first);
		}if (first.equals(show)) { //input was "show" command
			if(command.length == 1){
				Critter.displayWorld();
			}else{
				System.out.println("error processing: " + s);
			}
		}else if (first.equals(step)) {    //input was "step" command
			if(command.length == 1){
				Critter.worldTimeStep();
			}else if(command.length==2){    //step command has <count>
				try {
					int count = Integer.valueOf(command[1]);
					for(int i = 0; i < count; i++)
						Critter.worldTimeStep();
				}
				catch(NumberFormatException e) {
					System.out.println("error processing: " + s);
				}
			}
		} else if (first.equals(make)) {   //input was "make" command
			if(command.length == 2) {   //make a single critter of specified class
				try {
					Critter.makeCritter(command[1]);
				}
				catch(InvalidCritterException e) {
					System.out.println("error processing: " + s);
				}
			}
			else if(command.length == 3) {  //make <count> number of critters of specified class
				try {
					int count = Integer.valueOf(command[2]);
					for(int i = 0; i < count; i++)
						Critter.makeCritter(command[1]);
				}
				catch(NumberFormatException e) {
					System.out.println("error processing: " + s);
				}
				catch(InvalidCritterException e) {
					System.out.println("error processing: " + s);
				}
			}else{
				System.out.println("error processing: " + s);
			}
		}else if(first.equals(seed)){ //input was "seed" command
			if(command.length == 2) {
				try {
					long thisSeed = Long.valueOf(command[1]);
					Critter.setSeed(thisSeed);
				}
				catch(NumberFormatException e) {
					System.out.println("error processing: " + s);
				}
			}else {
				System.out.println("error processing: " + s);
			}
		}else if(first.equals(stats)) {   //input was "stats" command
			if(command.length == 2) {
				try {
					String critter_class_name = command[1];
					List<Critter> listOfCrits = Critter.getInstances(critter_class_name);
					if(listOfCrits.size() != 0) {   //if the critter class already exists then runStats
						Class<?> critter_class = listOfCrits.get(0).getClass();
						Method runs = critter_class.getMethod("runStats", java.util.List.class);
						runs.invoke(null,listOfCrits);
					} else {  //if the critter class does not already exist then create it and then runStats
						Class<?> critter_class = Class.forName(myPackage + "." + critter_class_name);
						Class<?> critter = Class.forName(myPackage + ".Critter");
						if(critter.isAssignableFrom(critter_class)) {
							Method runs = critter_class.getMethod("runStats", java.util.List.class);
							runs.invoke(null,listOfCrits);
						}
					}
				} catch (InvalidCritterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | NoClassDefFoundError e) {
					System.out.println("error processing: " + s);
				}
			}else{
				System.out.println("error processing: " + s);
			}
		}else{
			System.out.println("invalid command: " + s);
		}
//        System.out.println("Input: " + s);
		return input;
	}
}
