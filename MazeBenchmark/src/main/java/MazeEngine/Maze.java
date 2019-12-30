package MazeEngine;


import java.util.*;


public class Maze {

    private HashMap<Node, Node> prec;
    private HashMap<Node, Integer> cost;
    private int[][] grid1;
    private List<Node> grid;
    private int width;
    private int height;


    private Node start;
    private Node end;

    public Node getStartNode(){
        if(start == null){
            start = getStart();

        }
        return start;
    }

    public Node getEndNode(){
        if(end == null){
           end = getEnd();
        }
        return end;
    }

    public Maze( int[][] grid){
        this.grid = toNodeArray(grid);
        this.grid1 = grid;
        width = grid[0].length;
        height = grid.length;
        start = getStart();
        end = getEnd();

    }






    public int getCost(Node from, Node to){
        int xDif = Math.abs(from.x - to.x);
        int yDif = Math.abs(from.y - to.y);
        if(xDif == 1 && yDif == 1){
            return 15;
        }

        return 10;


    }


    /**
     * Suranda kelia labirinte
     * @param start
     * @param goal
     * @return
     */
    public List<Node> getPath( Node start, Node goal){
        traverseA_star(start, goal);
        Node current = goal;
        List<Node> path = new ArrayList<>();

        while (current != start){
            path.add(current);
            current = this.prec.get(current);

        }
        path.add(start);
        Collections.reverse(path);
        return path;

    }


    /**
     * Suranda atstuma nuo tasko iki kito tasko
     * @param from
     * @param to
     * @return
     */
    private int heuristic(Node from, Node to)
    {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    /**
     * Apeina labirinta pagal A* algoritma (su PriorityQueue)
     * @param start
     * @param goal
     */
    public void traverseA_star(Node start, Node goal){
        PriorityQueue<Node> open = new PriorityQueue();

        List closed = new ArrayList();
        HashMap<Node, Node> prec = new HashMap<>();
        HashMap<Node, Integer> cost = new HashMap<>();

        open.add(start);

        while(!open.isEmpty()){
            Node current = open.poll();
            if(current.type == goal.type){

                this.prec = prec;
                this.cost = cost;
                return;
            }

            closed.add(current);

            List<Node> neighbours = getNeighbours(current);
            for (Node neighbour : neighbours) {
                if(!closed.contains(neighbour)){
                    Integer costValue = cost.get(current);
                    if(costValue == null){costValue = 0;}
                    int newCost = costValue + getCost(current, neighbour);


                    if(!cost.containsKey(neighbour) || newCost < cost.get(neighbour) ){
                        neighbour.h = newCost;
                        cost.put(neighbour, newCost);
                        int priority = newCost + heuristic(goal, neighbour);
                        neighbour.f = priority;
                        open.add(neighbour);

                        prec.put(neighbour, current);
                    }}


                
            }
            
            
        }







    }

    /**
     * Apeina labirinta pagal A* algoritma (su LinkedList)
     * @param start
     * @param goal
     */
    public void traverseA_starList(Node start, Node goal){

        LinkedList<Node> open = new LinkedList<Node>();

        List closed = new ArrayList();
        HashMap<Node, Node> prec = new HashMap<>();
        HashMap<Node, Integer> cost = new HashMap<>();

        open.add(start);

        while(!open.isEmpty()){
            open.sort(Node::compareTo);
            Node current = open.poll();
            if(current.type == goal.type){

                this.prec = prec;
                this.cost = cost;
                return;
            }

            closed.add(current);

            List<Node> neighbours = getNeighbours(current);
            for (Node neighbour : neighbours) {
                if(!closed.contains(neighbour)){
                    Integer costValue = cost.get(current);
                    if(costValue == null){costValue = 0;}
                    int newCost = costValue + getCost(current, neighbour);


                    if(!cost.containsKey(neighbour) || newCost < cost.get(neighbour) ){
                        neighbour.h = newCost;
                        cost.put(neighbour, newCost);
                        int priority = newCost + heuristic(goal, neighbour);
                        neighbour.f = priority;
                        open.add(neighbour);

                        prec.put(neighbour, current);
                    }}



            }


        }







    }

    /**
     * int[][] peraso i List<Node>
     * value 0 means road, 1 - wall, 2 - start, 3 - end
     * @param grid
     * @return
     */
    public static List<Node> toNodeArray(int[][] grid){
       List<Node> nodes = new ArrayList();

        for(int i = 0; i <grid.length ; i++){
            for(int j = 0; j < grid[0].length; j++){
                Node newNode = new Node(i,j);
                switch (grid[i][j]){
                    case 0:{
                        newNode.setType(NodeType.ROAD);
                        break;
                    }
                    case 1:{
                        newNode.setType(NodeType.WALL);
                        break;


                    }
                    case 2:{
                        newNode.setType(NodeType.START);
                        break;

                    }
                    case 3:{
                        newNode.setType(NodeType.END);
                        break;


                    }
                }

                nodes.add(newNode);


            }
        }

        return nodes;

    }

    /**
     * Grazina Node tipa
     * @param x
     * @param y
     * @return
     */
    public NodeType getType(int x, int y){
        NodeType type;
        for(int i = 0; i <grid1[0].length ; i++){
            for(int j = 0; j < grid1.length; j++){
               if(i == x && j == y){
                   switch (grid1[i][j]){
                       case 0:{
                           type = NodeType.ROAD;
                           break;
                       }
                       case 1:{
                           type = NodeType.WALL;
                           break;


                       }
                       case 2:{
                           type = NodeType.START;
                           break;

                       }
                       case 3:{
                           type = NodeType.END;
                           break;


                       }

                       default:{
                           type = null;

                       }
                   }

                return  type;
               }



            }
        }

        return null;

    }

    /**
     * Suranda labirinto pradzia
     * @return
     */
    private Maze.Node getStart(){
        for(int i = 0; i < grid.size(); i++ ){
            Node node = grid.get(i);
            if(node.type == NodeType.START){
                return node;
            }
        }

        return null;
    }

    /**
     * Suranda labirinto pabaiga
     * @return
     */
    private Maze.Node getEnd(){
        for(int i = 0; i < grid.size(); i++ ){
            Node node = grid.get(i);
            if(node.type == NodeType.END){
                return node;
            }
        }

        return null;
    }


    /**
     * Grazina Node pagal koordinates
     * @param x
     * @param y
     * @return
     */
    public Maze.Node getNode(int x, int y){
        for(int i = 0; i < grid.size(); i++ ){
            Node node = grid.get(i);
            if(node.x == x && node.y == y){
                return node;
            }
        }

        return null;


    }


    /**
     * Perraso Node reiksme
     * @param x
     * @param y
     * @param val
     */
    public void replace(int x, int y, int val){
        grid1[x][y] = val;
        grid = toNodeArray(grid1);


    }

    /**
     * Suranda Node kaimynus
     * @param node
     * @return
     */
    public List<Node> getNeighbours(Node node){

        int[][] directions ={{1,0},{0,1},{-1,0},{0,-1},{1,-1},{1,1},{-1,-1},{-1,1}};
        List<Node> neighbours = new ArrayList();
        for (int[] direction : directions) {
            Node neighbour = getNode(node.x + direction[0], node.y + direction[1]);
                    //if the index of new does not come outside of grid
            if(neighbour != null && neighbour.type != NodeType.WALL)
                    neighbours.add(neighbour);

        }

        return neighbours;
    }


public static class Node implements Comparable<Node> {

        private int x;
        private int y;
        private int h;// atstumas nuo dabartinio langelio iki finiso
        private int f;//total cost

        private NodeType type;

        public int getX(){
            return x;
        }



        public int getY(){
            return y;
        }


        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }



        public void setType(NodeType type){
            this.type = type;
        }


    public void setType(int type){

        switch (type){

            case 0:{
                this.type = NodeType.ROAD;
                break;

            }
            case 1:{
                this.type = NodeType.WALL;
                break;

            }
            case 2:{
                this.type = NodeType.START;
                break;

            }
            case 3:{
                this.type = NodeType.END;
                break;

            }


        }


    }




    @Override
    public String toString() {
        return this.x +":" + this.y;
    }


    @Override
    public int compareTo(Node o) {
        if(!(o instanceof Node)){
            throw new IllegalArgumentException();
        }

        if((this.f == 0 || ((Node) o).f == 0) || this.f == ((Node) o).f){

            return Integer.compare(this.h, ((Node) o).h);
        }

        else

            return Integer.compare(this.f, ((Node) o).f);

        }




}



}


