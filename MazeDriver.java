import java.util.Scanner;

public class MazeDriver {
	
	public static void main(String[] args) {
		int width, height;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the width of the maze:");
		width = sc.nextInt();
		System.out.println("Enter the height of the maze:");
		height = sc.nextInt();
		sc.close();
		
		Maze maze = new Maze(height, width);
		maze.setDensity(.3);
		
		System.out.println(maze.displayMaze());
		System.out.println("Final dimensions of the maze:");
		System.out.println(maze.getDimensions());
		
		System.out.println("The maze start is marked by an 'S'.");
		System.out.println("The maze finish is marked by an 'F'.");
		System.out.println("The maze walls are marked by any 'x'.");
		System.out.println("The open space is marked by any '0'.");
		
		System.out.println();
		System.out.println("Now solving using a Stack...");
		System.out.println(maze.stackSolve());
		System.out.println(maze.displayMaze());
		
		System.out.println();
		System.out.println("Now solving using a Queue...");
		System.out.println(maze.queueSolve());
		System.out.println(maze.displayMaze());
		
	}
}