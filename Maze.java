import java.util.LinkedList;
import java.util.Stack;

public class Maze {
	private int width, height;
	private double density;
	private String start, finish;
	private int[][] maze;
	
	Maze(int y, int x) {
		this(y, x, .35);
	}
	
	Maze(int y, int x, double dens) {
		height = y + 2;
		width = x + 2;
		density = dens;
		maze = new int[height][width];
		generateMaze();
	}
	
	public void setDensity(double dens) {
		density = dens;
	}
	
	public double getDensity() {
		return density;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getDimensions() {
		return height + " x " + width;
	}
	
	public void generateMaze() {
		int y, x;
		boolean set;
		
		// Generate top and bottom borders
		for(int w = 0; w < width; w++) {
			maze[0][w] = 1;
			maze[height - 1][w] = 1;
		}
		
		// Generate left and right borders
		for(int h = 0; h < height; h++) {
			maze[h][0] = 1;
			maze[h][width - 1] = 1;
		}
		
		// Fill the maze with random blocks (1s)
		for(int h = 1; h < height - 1; h++) {
			for(int w = 1; w < width - 1; w++) {
				if(Math.random() < density)
					maze[h][w] = 1;
				else
					maze[h][w] = 0;
			}
		}
		
		// Set the start position (9)
		y = (int) (Math.random() * height);
		x = (int) (Math.random() * width);
		set = false;
		
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				if(maze[y][x] == 0) {
					maze[y][x] = 9;
					set = true;
					start = y + "," + x;
					break;
				}
				else
					x = (x + 1) % width;
			}
			if(set)
				break;
			else
				y = (y + 1) % height;
		}
		
		// Set the finish position (8)
		y = (int) (Math.random() * height);
		x = (int) (Math.random() * width);
		set = false;
		
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				if(maze[y][x] == 0) {
					maze[y][x] = 8;
					set = true;
					finish = y + "," + x;
					break;
				}
				else
					x = (x + 1) % width;
			}
			if(set)
				break;
			else
				y = (y + 1) % height;
		}
	}
	
	public String queueSolve() {
		resetMaze();
		int y, x, depth = 0;
		String element;
		LinkedList<String> navigate = new LinkedList<String>();
		
		navigate.add(start);
		
		while(!navigate.isEmpty()) {
			element = navigate.remove();
			
			y = Integer.parseInt(element.split(",")[0]);
			x = Integer.parseInt(element.split(",")[1]);
			
			if(maze[y - 1][x] == 0 || maze[y - 1][x] == 8) {
				if(maze[y - 1][x] == 0) {
					navigate.add((y - 1) + "," + x);
					maze[y - 1][x] = (maze[y][x] + 1);
				}
				else {
					depth = maze[y][x];
					break;
				}
			}
			
			if(maze[y + 1][x] == 0 || maze[y + 1][x] == 8) {
				if(maze[y + 1][x] == 0) {
					navigate.add((y + 1) + "," + x);
					maze[y + 1][x] = (maze[y][x] + 1);
				}
				else {
					depth = maze[y][x];
					break;
				}
			}
			
			if(maze[y][x + 1] == 0 || maze[y][x + 1] == 8) {
				if(maze[y][x + 1] == 0) {
					navigate.add(y + "," + (x + 1));
					maze[y][x + 1] = (maze[y][x] + 1);
				}
				else {
					depth = maze[y][x];
					break;
				}
			}
			
			if(maze[y][x - 1] == 0 || maze[y][x - 1] == 8) {
				if(maze[y][x - 1] == 0) {
					navigate.add(y + "," + (x - 1));
					maze[y][x - 1] = (maze[y][x] + 1);
				}
				else {
					depth = maze[y][x];
					break;
				}
			}
		}
		
		return readInstructions(queueInstructions(depth));
	}
	
	public String stackSolve() {
		resetMaze();
		boolean done = false;
		int y = Integer.parseInt(start.split(",")[0]);
		int x = Integer.parseInt(start.split(",")[1]);
		Stack<String> navigate = new Stack<String>();
		navigate.push(start);
		
		// Direction Priority: NSEW
		while(!done && !navigate.empty()) {
			
			// Check North for empty space or finish
			if(maze[y - 1][x] == 0 || maze[y - 1][x] == 8) {
				y -= 1;
				navigate.push(y + "," + x);
			}
			
			// Check South for empty space or finish
			else if(maze[y + 1][x] == 0 || maze[y + 1][x] == 8) {
				y += 1;
				navigate.push(y + "," + x);
			}
			
			// Check East for empty space or finish
			else if(maze[y][x + 1] == 0 || maze[y][x + 1] == 8) {
				x += 1;
				navigate.push(y + "," + x);
			}
			
			// Check West for empty space or finish
			else if(maze[y][x - 1] == 0 || maze[y][x - 1] == 8) {
				x -= 1;
				navigate.push(y + "," + x);
			}
			
			// Surrounded by walls or visited spaces
			else {
				navigate.pop();
				if(maze[y][x] != 9)
					maze[y][x] = 2;
				if(!navigate.empty()) {
					y = Integer.parseInt(navigate.peek().split(",")[0]);
					x = Integer.parseInt(navigate.peek().split(",")[1]);
				}
			}
			
			if(maze[y][x] == 8)
				done = true;
			else if(maze[y][x] == 0)
				maze[y][x] = 5;
		}
		
		return readInstructions(stackInstructions(navigate));
	}
	
	private Stack<String> stackInstructions(Stack<String> coordinates) {
		int y, x, yPrev, xPrev;

		Stack<String> instructions = new Stack<String>();
		
		while(!coordinates.empty()) {
			yPrev = Integer.parseInt(coordinates.peek().split(",")[0]);
			xPrev = Integer.parseInt(coordinates.pop().split(",")[1]);
			if(coordinates.empty()) {
				y = Integer.parseInt(start.split(",")[0]);
				x = Integer.parseInt(start.split(",")[1]);
			}
			else {
				y = Integer.parseInt(coordinates.peek().split(",")[0]);
				x = Integer.parseInt(coordinates.peek().split(",")[1]);
			}
			
			y = yPrev - y;
			x = xPrev - x;
			
			if(y == 1)
				instructions.push("Go South. ");
			else if(y == -1)
				instructions.push("Go North. ");
			else if(x == 1)
				instructions.push("Go East. ");
			else if(x == -1)
				instructions.push("Go West. ");
		}
		
		return instructions;
	}
	
	private Stack<String> queueInstructions(int depth) {
		int y = Integer.parseInt(finish.split(",")[0]);
		int x = Integer.parseInt(finish.split(",")[1]);
		Stack<String> instructions = new Stack<String>();
		
		for(int i = depth; i >= 9; i--) {
			if(maze[y - 1][x] == i) {
				instructions.push("Go South. ");
				if(maze[y][x] != 8)
					maze[y][x] = 5;
				y -= 1;
			}
		
			else if(maze[y + 1][x] == i) {
				instructions.push("Go North. ");
				if(maze[y][x] != 8)
					maze[y][x] = 5;
				y += 1;
			}
			
			else if(maze[y][x + 1] == i) {
				instructions.push("Go West. ");
				if(maze[y][x] != 8)
					maze[y][x] = 5;
				x += 1;
			}
			
			else {
				instructions.push("Go East. ");
				if(maze[y][x] != 8)
					maze[y][x] = 5;
				x -= 1;
			}
		}
		
		return instructions;
	}
	
	private void resetMaze() {
		for(int h = 1; h < height - 1; h++) {
			for(int w = 1; w < width - 1; w++) {
				switch (maze[h][w]) {
				case 1:	 break;
				case 8:	 break;
				case 9:  break;
				default: maze[h][w] = 0;
						 break;
				}
			}
		}
	}
	
	private void cleanupMaze() {
		for(int h = 1; h < height - 1; h++) {
			for(int w = 1; w < width - 1; w++) {
				if(maze[h][w] == 2)
					maze[h][w] = 0;
				else if(maze[h][w] > 9) {
					maze[h][w] = 0;
				}
			}
		}
	}
	
	public String readInstructions(Stack<String> instructions) {
		StringBuilder formatted = new StringBuilder();
		
		if(instructions.empty())
			return "No possible solution. ";
		
		formatted.append("Maze solved... Solution printed:\n");
		while(!instructions.empty()) {
			formatted.append(instructions.pop());
			if(!instructions.empty()) 
				formatted.append(instructions.pop());
			if(!instructions.empty()) 
				formatted.append(instructions.pop());
			if(!instructions.empty()) 
				formatted.append(instructions.pop());
			if(!instructions.empty()) 
				formatted.append(instructions.pop());
			formatted.append("\n");
		}
		
		return formatted.toString();
	}
	
	public String displayMaze() {
		return toString();
	}
	
	public String toString() {
		cleanupMaze();
		StringBuilder config = new StringBuilder("");
		for(int h = 0; h < height; h++) { // Step through each row
			for(int w = 0; w < width; w++) { // Step through each element in the row
				switch (maze[h][w]) {
				case 1:  config.append("x ");
						 break;
				case 5:  config.append("- ");
						 break;
				case 8:  config.append("F ");
						 break;
				case 9:  config.append("S ");
						 break;
				default: config.append((Integer.toString(maze[h][w]) + " "));
						 break;
				}

				if(w == width - 1)
					config.append("\n");
			}
		}
		return config.toString();
	}
	
}
