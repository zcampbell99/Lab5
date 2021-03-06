package assignment5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.*;

import java.util.*;
import java.util.List;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR,
		FOURPOINT,
		BOLT,
		PENTAGON
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 * Gets the destination for a critter's movement
	 * @param currLoc is the current location of the critter
	 * @param steps is the number of steps the critter wants to move (walk or run, 1 or 2)
	 * @param direction is the direction the critter wants to move in
	 * @return the destination point of the critter
	 */
	public Point getNextPoint(Point currLoc, int steps, int direction){
		Point new_pos = new Point(currLoc.x, currLoc.y);
		switch(direction) {
			case 0:                     // West
				new_pos.x += steps;
				break;
			case 1:                     // Northwest
				new_pos.x += steps;
				new_pos.y -= steps;
				break;
			case 2:                     // North
				new_pos.y -= steps;
				break;
			case 3:                     // Northeast
				new_pos.x -= steps;
				new_pos.y -= steps;
				break;
			case 4:                     // East
				new_pos.x -= steps;
				break;
			case 5:                     // Southeast
				new_pos.x -= steps;
				new_pos.y += steps;
				break;
			case 6:                     // South
				new_pos.y += steps;
				break;
			case 7:                     // Southwest
				new_pos.x += steps;
				new_pos.y += steps;
				break;
			default:
				throw new IllegalArgumentException();   // If it's an invalid direction
		}
		if(new_pos.x >= Params.world_width)
			new_pos.x -= Params.world_width;
		else if (new_pos.x < 0)
			new_pos.x += Params.world_width;
		if(new_pos.y >= Params.world_height)
			new_pos.y -= Params.world_height;
		else if (new_pos.y < 0)
			new_pos.y += Params.world_height;

		return new_pos;
	}

	/**
	 * Looks to see if the spot that the critter wants to move to is occupied or not
	 * @param direction is the direction that the critter wants to move in
	 * @param steps a boolean to see if the critter is walking (false) or running (true)
	 * @return null if the destination is unoccupied or a string of the critter occupying the spot
	 */
	protected final String look(int direction, boolean steps){
		energy -= Params.look_energy_cost;  //critter pays look energy cost
		Point currLoc = new Point(this.x_coord, this.y_coord);
		if(steps == true){  //critter wants to walk or run
			Point nextLoc = getNextPoint(currLoc, 2, direction);    //get the next location from running or walking
			if (grid.containsKey(nextLoc)) {    //if the point is in the grid
				LinkedList<Critter> currCritList = grid.get(nextLoc);   //finds the critter at this point
				if (currCritList.size() >= 1) {
					return currCritList.get(0).toString(); //gets the first critter that was at this point to return
				}else{
					return null;
				}
			}
		}else{
			Point nextLoc = getNextPoint(currLoc, 1, direction);    //get the next location from running or walking
			if (grid.containsKey(nextLoc)) {    //if the point is in the grid
				LinkedList<Critter> currCritList = grid.get(nextLoc);   //finds the critter at this point
				if (currCritList.size() >= 1) {
					return currCritList.get(0).toString(); //gets the first critter that was at this point to return
				}else{
					return null;
				}
			}
		}
		return null;
	}

	// Everything Down from here is from Project 4
	private static LinkedList<Critter> allCritters = new LinkedList<Critter>();
	protected static HashMap<Point, LinkedList<Critter>> grid = new HashMap<Point, LinkedList<Critter>>();
	private static int timestep = 0;
	private int x_coord;
	private int y_coord;
	private int numMoves = 0;
	private static boolean afterInitialMove;
	public static int critter2Deaths = 0;
	public static int critter1Wins = 0;
	public static int repCount = 0;
	public static int distanceTravelled = 0;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }

	private int energy = 0;
	protected int getEnergy() { return energy; }

	/**
	 * Set the current critter's energy
	 * @param e is the new energy
	 */
	protected void setEnergy(int e) {
		this.energy = e;
	}

	protected int getX() {
		return x_coord;
	}

	protected int getY() {
		return y_coord;
	}

	protected void setX(int x) {
		this.x_coord = x;
	}

	protected void setY(int y) {
		this.y_coord = y;
	}

	/**
	 * Moves a critter one step in the given direction
	 * @param direction of movement
	 */
	protected final void walk(int direction) {
		updateLoc(direction,1,++numMoves);
	}

	/**
	 * Moves a critter two steps in the given direction
	 * @param direction of movement
	 */
	protected final void run(int direction) {
		updateLoc(direction,2,++numMoves);
	}

	/**
	 * Core functionality of the move function that deals with energy and fights
	 * @param direction of movement
	 * @param steps to move
	 * @param numMoves done during one timestep
	 */
	private void updateLoc(int direction, int steps, int numMoves) {
		if(steps == 1)
			energy -= Params.walk_energy_cost;
		else
			energy -= Params.run_energy_cost;
		if(numMoves == 1 && isAlive(this)) {
			Point prev_pos = new Point(x_coord, y_coord);
			move(direction,steps, this);
			Point new_pos = new Point(x_coord, y_coord);
			if (this.toString().equals("4"))
				distanceTravelled += steps;
			if(afterInitialMove) {	// if the critter wants to run away in a fight but already moved this timestep
				for(Map.Entry<Point, LinkedList<Critter>> entry : grid.entrySet()) { // iterate through each occupied position
					if(new_pos.equals(new Point(entry.getKey().x, entry.getKey().y))) {
						new_pos = prev_pos;  // if the position is already occupied by a critter don't move this one
						if (this.toString().equals("4"))
							distanceTravelled -= steps;
						break;
					}
				}
			}
			if(!new_pos.equals(prev_pos)) {
				LinkedList<Critter> oldLoc = grid.get(prev_pos);
				oldLoc.remove(this);
				if(oldLoc.size() == 0) {
					grid.remove(prev_pos);
				}
				if(grid.containsKey(new_pos)) {
					grid.get(new_pos).add(this); //add to arraylist if position already has a critter
				} else {
					LinkedList<Critter> newLoc = new LinkedList<>();
					newLoc.add(this);
					grid.put(new_pos, newLoc); //create new key with an arraylist of the 1 critter
				}
			}
			x_coord = new_pos.x; y_coord = new_pos.y; // change the critter position to the new position
		}
	}

	/**
	 * Core functionality of the move function that deals with movement on the grid
	 * @param direction of movement
	 * @param steps to take
	 * @param crit to be moved
	 */
	private static void move(int direction, int steps, Critter crit){
		Point new_pos = new Point(crit.x_coord, crit.y_coord);
		switch(direction) {
			case 0:                     // West
				new_pos.x += steps;
				break;
			case 1:                     // Northwest
				new_pos.x += steps;
				new_pos.y -= steps;
				break;
			case 2:                     // North
				new_pos.y -= steps;
				break;
			case 3:                     // Northeast
				new_pos.x -= steps;
				new_pos.y -= steps;
				break;
			case 4:                     // East
				new_pos.x -= steps;
				break;
			case 5:                     // Southeast
				new_pos.x -= steps;
				new_pos.y += steps;
				break;
			case 6:                     // South
				new_pos.y += steps;
				break;
			case 7:                     // Southwest
				new_pos.x += steps;
				new_pos.y += steps;
				break;
			default:
				throw new IllegalArgumentException();   // If it's an invalid direction
		}

		if(new_pos.x >= Params.world_width)
			new_pos.x -= Params.world_width;
		else if (new_pos.x < 0)
			new_pos.x += Params.world_width;
		if(new_pos.y >= Params.world_height)
			new_pos.y -= Params.world_height;
		else if (new_pos.y < 0)
			new_pos.y += Params.world_height;

		crit.x_coord = new_pos.x; crit.y_coord = new_pos.y;
	}

	/**
	 * Sets the fields for the given object and adds to the babies arraylist
	 * @param offspring of the critter
	 * @param direction of movement
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy) return;
		offspring.energy = this.energy/2;
		this.energy = this.energy/2 + 1;
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		move(direction, 1, offspring);
		babies.add(offspring);
	}


	public abstract void doTimeStep();
	public abstract boolean fight(String opponent);

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name is the string version of the critter's Class
	 * @throws InvalidCritterException if the class does not exist
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		critter_class_name = critter_class_name.trim();
		try {
			Class<?> critter_class = Class.forName(myPackage + "." + critter_class_name);
			Class<?> critter = Class.forName(myPackage + ".Critter");
			if(critter.isAssignableFrom(critter_class)) {
				Critter critter_instance = (Critter)critter_class.newInstance();
				//prepare critter for simulation, create initial position
				critter_instance.x_coord = getRandomInt(Params.world_width);
				critter_instance.y_coord = getRandomInt(Params.world_height); //set position
				critter_instance.energy = Params.start_energy;
				allCritters.addLast(critter_instance);
				addToGrid(critter_instance);
			} else
				throw new InvalidCritterException(critter_class_name);
		} catch (ClassNotFoundException | NoClassDefFoundError | InstantiationException | IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}

	/**
	 * Adds the given critter to the critter grid
	 * @param critter_instance to be added
	 */
	private static void addToGrid(Critter critter_instance) {
		Point critpos = new Point(critter_instance.x_coord, critter_instance.y_coord);
		if(grid.containsKey(critpos)) {
			grid.get(critpos).add(critter_instance);
		} else {
			LinkedList<Critter> crit = new LinkedList<>();
			crit.add(critter_instance);
			grid.put(critpos, crit);
		}
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name is what kind of Critter is to be listed
	 * @return List of Critters.
	 * @throws InvalidCritterException if the class does not exist
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<>();
		try{
			Class<?> critter_class = Class.forName(myPackage + "." +critter_class_name);
			Class<?> critter = Class.forName(myPackage + ".Critter");
			if(critter.isAssignableFrom(critter_class)) {
				for(Critter crit : allCritters) {
					if(critter_class.equals(crit.getClass()) || crit.getClass().isInstance(critter_class)){
						result.add(crit);
					}
				}
			}
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			throw new InvalidCritterException(critter_class_name); //case when critter_class doesn't exist
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
	}

	public static String runStats2(List<Critter> critters) {
		String begin = "" + critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			begin = begin + prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		return begin;
		/*System.out.println();*/
	}

	/** the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here.
	 *
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;

		}

		protected void setX_coord(int new_x_coord) {
			Point prev_pos = new Point(this.getX_coord(),this.getY_coord());
			super.x_coord = new_x_coord;
			Point new_pos = new Point(this.getX_coord(),this.getY_coord());

			if(!new_pos.equals(prev_pos)) {
				LinkedList<Critter> cac = grid.get(prev_pos);
				cac.remove(this);
				if(cac.size() == 0) {
					grid.remove(prev_pos);
				}
				addToGrid(this);
				/*if(grid.containsKey(new_pos)) {
					grid.get(new_pos).add(this); //add to arraylist if position already has a critter
				}
				else {
					LinkedList<Critter> ac = new LinkedList<Critter>();
					ac.add(this);
					grid.put(new_pos, ac); //create new key with an arraylist of the 1 critter
				}*/
			}
		}

		protected void setY_coord(int new_y_coord) {
			Point prev_pos = new Point(this.getX_coord(),this.getY_coord());
			super.y_coord = new_y_coord;
			Point new_pos = new Point(this.getX_coord(),this.getY_coord());

			if(!new_pos.equals(prev_pos)) {
				LinkedList<Critter> cac = grid.get(prev_pos);
				cac.remove(this);
				if(cac.size() == 0) {
					grid.remove(prev_pos);
				}
				addToGrid(this);
			}
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}


		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			population.clear();
			population.addAll(allCritters);
			babies.clear();
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		allCritters.clear();
		population.clear();
		babies.clear();
		grid.clear();
	}

	/**
	 * Has all the critters do doTimeStep and resolve any encounters.
	 * Updates all critter states and clears the dead. Replenishes Algae.
	 */
	public static void worldTimeStep() {
		timestep++;
		afterInitialMove = false;   // To check if critter moved before the encounter
		for(Critter crit : allCritters) {
			crit.numMoves = 0;      // So that each critter has the opportunity to move
			crit.doTimeStep();
		}
		encounters();              // After all critters move, do encounters
		updateRestEnergy();         // update energy of all critters from resting
		for (int i = 0; i < Params.refresh_algae_count; i++) {  // Regenerate algae
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException e) {
				System.out.println("error processing: " + e.offending_class);
			}
		}
		allCritters.addAll(babies); // all babies are now adults
		for(Critter crit : babies) {
			if (crit.toString().equals("3")) {
				repCount++;
			}
			addToGrid(crit);	// move all babies onto grid
		}
		babies.clear();	// clear the babies array for next time-step
		clearDead(); // clear all dead critters
	}

	/**
	 * Decides the outcome of any critters who come into contact (occupy the same position) on the grid
	 * critters will either run away or fight (fight results in a death)
	 */
	private static void encounters() {
		afterInitialMove = true; // if walk/run is called after doTimeStep()
		for(Map.Entry<Point, LinkedList<Critter>> entry : grid.entrySet()) {
			LinkedList<Critter> stackOfCritters = entry.getValue();
			while(stackOfCritters.size() > 1 ) {
				int i = 0;
				Critter A = stackOfCritters.get(i); Critter B = stackOfCritters.get(i+1);
				boolean fightA = A.fight(B.toString()); // checking if this critter is willing to fight its opponent
				boolean fightB = B.fight(A.toString());
				if(isAlive(A) && isAlive(B) && (new Point(A.x_coord, A.y_coord)).equals(new Point(B.x_coord,B.y_coord))) {
					int rollA = 0;
					int rollB = 0;
					if(fightA) rollA = getRandomInt(A.energy);  //getting the "attack power" for A if they are willing to fight
					if(fightB) rollB = getRandomInt(B.energy);  //getting the "attack power" for B if they are willing to fight
					if(rollA >= rollB)  {   //critter A wins
						if(A.toString().equals("1")){
							critter1Wins++;
						}
						A.energy += B.energy/2;
						B.energy = 0;
					}
					else {                  //critter B wins
						if(B.toString().equals("1")){
							critter1Wins++;
						}
						B.energy += A.energy/2;
						A.energy = 0;
					}
				}
				if(!isAlive(A)) {   //if A is dead, remove from grid and critter list
					if(A.toString().equals("2")){
						critter2Deaths++;
					}
					stackOfCritters.remove(A);
					allCritters.remove(A);
				}
				if(!isAlive(B)) {   //if B is dead, remove from grid and critter list
					if(B.toString().equals("2")){
						critter2Deaths++;
					}
					stackOfCritters.remove(B);
					allCritters.remove(B);
				}
			}
		}
	}

	/**
	 * Checks to see if the specified critter is alive (energy > 0)
	 * @param crit is the critter to be checked
	 * @return true if the critter is alive and false if dead
	 */
	private static boolean isAlive(Critter crit) {
		if(crit.energy <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * Used to deduct the rest energy cost of each timestep from the population
	 */
	private static void updateRestEnergy() {
		for(Critter crit : allCritters) {
			crit.energy -= Params.rest_energy_cost;
		}
	}

	/**
	 * Clear the critter list and grid of all dead critters
	 */
	private static void clearDead() {
		Iterator<Critter> it = allCritters.iterator();
		while(it.hasNext()) {
			if(!isAlive(it.next())) {
				if(it.toString().equals("2")){
					critter2Deaths++;
				}
				it.remove();
			}
		}
	}

	/**
	 * Create the shapes for each of the critters in the current package
	 * @param s is the shape of the unique critter
	 * @return a Shape to be used as a Node in a gridPane
	 */
	private static Shape setShape(CritterShape s) {
		switch (s) {
			case SQUARE:				// Craig
				Rectangle r = new Rectangle();
				r.setHeight(15);
				r.setWidth(15);
				return r;
			case STAR:					// Tragic
				Polygon p1 = new Polygon();
				p1.getPoints().addAll(7.5, 0.0, 11.0, 15.0, 0.0, 6.0, 15.0, 6.0, 4.0, 15.0, 7.5, 0.0);
				return p1;
			case CIRCLE:				// Algae
				Circle c = new Circle();
				c.setRadius(7.5);
				return c;
			case DIAMOND:				// Algaephobic
				Polygon p2 = new Polygon();
				p2.getPoints().addAll(7.5, 0.0, 15.0, 7.5, 7.5, 15.0, 0.0, 7.5, 7.5, 0.0);
				return p2;
			case TRIANGLE:				// Critter2
				Polygon p3 = new Polygon();
				p3.getPoints().addAll(7.5, 0.0, 15.0, 15.0, 0.0, 15.0, 7.5, 0.0);
				return p3;
			case FOURPOINT:				// Critter3
				Polygon p4 = new Polygon();
				p4.getPoints().addAll(7.5, 0.0, 8.5, 5.0, 15.0, 7.5, 8.5, 10.0, 7.5, 15.0, 6.5, 10.0, 0.0, 7.5, 6.5, 5.0, 7.5, 0.0);
				return p4;
			case BOLT:					// Critter4
				Polygon p5 = new Polygon();
				p5.getPoints().addAll(15.0, 0.0, 10.0, 5.0, 13.0, 7.0, 0.0, 15.0, 8.0, 7.0, 5.0, 5.0, 15.0, 0.0);
				return p5;
			case PENTAGON:				// Critter1
				Polygon p6 = new Polygon();
				p6.getPoints().addAll(7.5, 0.0, 15.0, 6.0, 11.0, 15.0, 4.0, 15.0, 0.0, 6.0, 7.5, 0.0);
				return p6;
		}
		return new Polygon();
	}

	/**
	 * Prints a display of the grid world into console
	 */
	public static void displayWorld() {
		for (int i = 0; i < Params.world_height + 2; i++) {         // Rows
			for (int j = 0; j < Params.world_width + 2; j++) {      // Columns
				if (i == 0 || i == Params.world_height + 1) {
					if (j == 0 || j == Params.world_width + 1)
						System.out.print("+");
					else
						System.out.print("-");
				} else if (j == 0 || j == Params.world_width + 1) {
					System.out.print("|");
				} else {
					Point p = new Point(j - 1, i - 1);
					if (grid.containsKey(p)) {
						LinkedList<Critter> currCritList = grid.get(p);   //finds the critter at this point to display
						if (currCritList.size() >= 1) {
							System.out.print(currCritList.get(0).toString()); //gets the first critter that was at this point to print
						}
					} else
						System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Displays the world on the UI rather than through the system console
	 * @return the updated gridpane
	 */
	protected static GridPane displayGUIWorld() {
		GridPane g = new GridPane();
		g.setLayoutX(5);
		g.setLayoutY(5);
		g.prefHeight(370);
		g.prefWidth(570);
		for (int i = 0; i < Params.world_width; i++) {
			g.getColumnConstraints().add(new ColumnConstraints(worldController.boxSize));
		}
		for (int i = 0; i < Params.world_height; i++) {
			g.getRowConstraints().add(new RowConstraints(worldController.boxSize));
		}
		g.getChildren().clear();
		g.setGridLinesVisible(true);
		for (int i = 0; i < Params.world_height; i++) {         // Rows
			for (int j = 0; j < Params.world_width; j++) {      // Columns
				Critter.Point p = new Critter.Point(j, i);
				if (Critter.grid.containsKey(p)) {
					LinkedList<Critter> currCritList = Critter.grid.get(p);   //finds the critter at this point to display
					if (currCritList.size() >= 1) {
						Critter.CritterShape currShape = currCritList.get(0).viewShape();
						Shape shape = Critter.setShape(currShape);
						shape.setFill(currCritList.get(0).viewFillColor());
						g.add(shape, j, i);
					} else {
						g.add(null, j, i);
					}
				}
			}
		}
		return g;
	}

	/**
	 * class for a 2D point for the grid of critters
	 **/
	protected static class Point {
		private int x;
		private int y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Method used to compare 2 points.
		 * Required to make HashMap work properly with our Point keys.
		 */
		@Override
		public boolean equals(Object o) {
			Point p = (Point)o;
			return (p.x == x) && (p.y == y);
		}
		/**
		 * Method used to hash the 2 int field of our Point class.
		 * Required to make HashMap work properly with our Point keys.
		 */
		@Override
		public int hashCode() {
			return Objects.hash(x,y);
		}
	}
	
}
