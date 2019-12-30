package ManualTest;



import GUI.MazeGenerator;
import MazeEngine.Maze;

import java.util.List;

public class Test  {


    static int x = 0;

    public static void main(String[] args) {






        int[][] grid0 = {
                {1,1,1,1},
                {2,0,0,1},
                {1,1,0,1},
                {1,1,3,1},
                {1,1,1,1}
        };


        int[][] grid1 = {
                {1,0,0,0,1,1},
                {2,0,0,1,3,1},
                {0,0,0,1,0,0},
                {0,0,0,1,0,0},
                {0,1,1,1,1,1}
        };

        int[][] grid2 = {
                {1,1,1,1,1,1,1,1,1,1},
                {2,0,0,1,0,0,1,0,0,3},
                {1,1,0,1,0,0,1,0,1,1},
                {1,1,0,0,0,1,0,1,0,1},
                {1,1,1,1,1,1,1,0,1,1}
        };



        int[][] grid3= {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1},
                {1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0},
                {1,1,0,1,0,0,1,0,0,0,0,0,1,1,0,1,0,1},
                {1,0,1,0,0,1,0,1,0,1,1,1,0,0,1,0,1,0},
                {1,0,1,0,0,1,0,1,0,1,1,0,1,0,1,0,1,0},
                {0,0,1,0,0,1,0,1,0,1,1,0,0,0,1,0,1,0},
                {1,0,1,1,1,1,0,1,0,1,1,0,1,0,1,0,1,0},
                {0,0,1,0,0,1,0,1,1,1,1,0,0,1,1,0,1,0},
                {3,1,1,1,1,1,1,0,1,1,1,0,0,0,0,1,1,0}
        };


        int[][][] tests = {grid0,grid1,grid2,grid3};


        for (int[][] test :tests
             ) {

            testMaze(test,"PQ");

            System.out.println("|||||||||||||||||||||||||");
            testMaze(test,"LL");

        }





        for(int i = 1; i < 10; i++){
            System.out.println("Prim");
            int[][] maze1 = new MazeGenerator(10 * i,10 * i, "Prim").getGrid();
            testAlgo(maze1);
            System.out.println("Aldous-Broder");
            int[][] maze2 = new MazeGenerator(10 * i,10 * i, "Aldous-Broder").getGrid();
            testAlgo(maze2);
        }















    }

    public static void testAlgo(int[][] k){
        for(int i =0; i< k.length; i++)
        {

            for(int j =0; j< k[i].length; j++)
            {
                System.out.print(k[i][j]);
            }
            System.out.println();
        }
    }

    public static void testMaze(int[][] grid, String type){
        Maze maze = new Maze(grid);

        Maze.Node start = maze.getStartNode();

        Maze.Node end = maze.getEndNode();


        if(type == "PQ"){
            maze.traverseA_star(start, end);
        }
        if(type == "LL"){
            maze.traverseA_starList(start, end);
        }

        List<Maze.Node> path = maze.getPath(start, end);

        System.out.println("TEST" + x++ + ": "+ type);
        System.out.println("------------");
        for (Maze.Node node: path
        ) {
            System.out.println(node.toString());

        }
        System.out.println("------------");





    }

}

