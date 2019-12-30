package GUI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MazeGenerator {

    public static final int WALL = 1;
    public static final int ROAD = 0;

    private final int[][] grid1;
    private final int width;
    private final int height;

    public MazeGenerator(final int width, final int height, String method ){
        this.width = width;
        this.height = height;
        this.grid1 = new int[width][height];
        switch(method){
            case "Prim":{
                generatePrims();
                break;

            }

            case "Aldous-Broder":{

                generateAB();
                break;
            }


        }



    }

    public MazeGenerator(final int width, final int height){
        this.width = width;
        this.height = height;
        this.grid1 = new int[width][height];
        generatePrims();

    }

    /**
     * Grazina grida
     * @return
     */
    public int[][] getGrid(){
        return grid1;
    }

    /**
     * Sugeneruoja labirinta pagal Aldous-Broder algoritma
     */
    private void generateAB(){
        int[][] directions ={{1,0},{0,1},{-1,0},{0,-1},{1,-1},{1,1},{-1,-1},{-1,1}};
        Random random = new Random();
        int w = height;
        int h =  width;

        int x = random.nextInt(h);
        int y =random.nextInt(w);

        int count = 0;

        for (int[] row : grid1
        ) {
            Arrays.fill(row, 1);

        }

        for(int i = 1; i< h;i+=2){
            for(int j = 1; j< w;j+=2){
                grid1[i][j] = 2;
                count++;


            }
        }



        int remaining = w*h -1;

        while( remaining > 0 && count > 0){


            List intList = Arrays.asList(directions);
            Collections.shuffle(intList);
            int[][] arr = (int[][]) intList.toArray(directions);



            int xCheck ;
            int yCheck ;

            for (int[] dir: arr
            ) {
                xCheck = x + dir[0];
                yCheck = y + dir[1];
                if(xCheck >= 0 && xCheck < h  && yCheck >=0 && yCheck < w ){

                    if(grid1[xCheck][yCheck] == 2 ){

                        grid1[x][y] = 0;
                        grid1[xCheck][yCheck] = 0;

                        count--;
                        remaining--;
                    }
                    x = xCheck;
                    y = yCheck;
                    break;
                }



            }

        }

        generateStartEnd();


    }

    /**
     * Sugeneruoja labirinta pagal Primo algoritma
     */
    private void generatePrims(){
        for (int[] row : grid1
        ) {
            Arrays.fill(row, 1);

        }

        final LinkedList<int[]> frontiers = new LinkedList<>();
        final Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        frontiers.add(new int[]{x,y,x,y});

        while ( !frontiers.isEmpty() ){
            final int[] f = frontiers.remove( random.nextInt( frontiers.size() ) );
            x = f[2];
            y = f[3];
            if ( grid1[x][y] == WALL )
            {
                grid1[f[0]][f[1]] = grid1[x][y] = ROAD;
                if ( x >= 2 && grid1[x-2][y] == WALL )
                    frontiers.add( new int[]{x-1,y,x-2,y} );
                if ( y >= 2 && grid1[x][y-2] == WALL )
                    frontiers.add( new int[]{x,y-1,x,y-2} );
                if ( x < width-2 && grid1[x+2][y] == WALL )
                    frontiers.add( new int[]{x+1,y,x+2,y} );
                if ( y < height-2 && grid1[x][y+2] == WALL )
                    frontiers.add( new int[]{x,y+1,x,y+2} );
            }
        }

        generateStartEnd();



    }

    /**
     * Apskaiciuoja atstuma nuo vieno tasko iki kito
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    private int heuristic(int fromX,int fromY, int toX, int toY)
    {
        return Math.abs(fromX - toX) + Math.abs(fromY - toY);
    }

    /**
     * Sugeneruoja pradzia/pabaiga
     */
    public void generateStartEnd( ){
        Random random = new Random();


        List<Integer> indices = IntStream.range(0, grid1.length).boxed().collect(Collectors.toList());
        Collections.shuffle(indices);
        int x = indices.get(random.nextInt(indices.size()));
        int y;
        if(indices.get(x) == 0 || indices.get(x) == indices.size() - 1 )
        {
            y = random.nextInt(grid1[0].length);
        }
        else{

            int a = 0;
            int b = grid1[0].length - 1;
            int c = random.nextBoolean() ? a : b;
            y = c;
        }

        grid1[x][y] = 2;
        int longDist = 0;
        int endX = 0;
        int endY = 0;

        for(int i = 0; i< grid1.length; i++){
            for(int j = 0; j< grid1[0].length; j++){
                int heuristics = heuristic(x,y,i,j);
                if(heuristics > longDist /*&&*/){
                    longDist = heuristics;
                    endX = i;
                    endY = j;
                }



            }
        }
        grid1[endX][endY] = 3;



    }

    public static void main(String[] args) {
        MazeGenerator prim = new MazeGenerator(20,20, "Prim");

        for(int i = 0; i<prim.grid1.length; i++){
            for(int j = 0; j<prim.grid1[0].length; j++){
                System.out.print(prim.grid1[i][j] + ",");

            }
            System.out.println();
        }
    }
}