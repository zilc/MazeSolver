package GUI;

import MazeEngine.Maze;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class MazeScene extends BaseGraphics {

    private VisualParameters visualParameters;
    private HBox currentHBox;
    private static Pane currentPane;
    private static GridPane root;
    private Maze currentMaze;
    //kai solveCount = 2, jau galima spresti, nes yra ir pradzia ir pab.
    private int solveCount = 0;
    private double width;
    //Langelio spalvos pasirinkimas
    private static ComboBox<String> cbColor;
    //Labirinto generavimo algoritmo pasirinkimas
    private static ComboBox<String> mazeGen;
    private static Label benchLabel;


    public Button addButton(String name, EventHandler<ActionEvent> action){
        Button btn = new Button(name);
        currentHBox.getChildren().add(btn);
        btn.setOnAction(action);
        return btn;
    }

    public void addNewHBox(){
        currentHBox = new HBox(3.0);
        currentHBox.setPadding(new Insets(10.0));
        currentHBox.setAlignment(Pos.BOTTOM_CENTER);
        root.addRow(1,currentHBox);


    }


    public void createControls() {

        addNewHBox();

        visualParameters = new VisualParameters();

        cbColor = visualParameters.addComboBox1("Tile type", "Start", "End", "Wall");
        mazeGen = visualParameters.addComboBox1("Maze gen. algorithm", "Prim's", "Aldous-Broder");
        benchLabel = new Label("");
        benchLabel.setPadding(new Insets(20,0,0,10));
        addButton("Generate grid", e -> generateGrid(visualParameters.getW(), visualParameters.getH(),false));
        addButton("Generate maze", e -> generateGrid(visualParameters.getW(), visualParameters.getH(),true));

        addButton("Solve maze", e-> { solveMaze();
        }).disableProperty().setValue(true);
        currentHBox.getChildren().addAll(new Label("Time: "));

        currentHBox.getChildren().add(benchLabel);


    }


    /**
     * Issprendzia labirinta
     */
    public void solveMaze() {

        System.gc();
        System.gc();
        System.gc();
        long t1 = System.nanoTime();
        List<Maze.Node> path = currentMaze.getPath(currentMaze.getStartNode(), currentMaze.getEndNode());
        long t2 = System.nanoTime();
        long time = (t2 - t1);
        double timesec = time/1_000_000;
        double timesec2 = timesec/1000;
        benchLabel.setText(String.valueOf(timesec2) +"s");

        for(int i = 0; i < path.size(); i++){

            Rectangle rec = new Rectangle();
            rec.setX(path.get(i).getX() * width);
            rec.setY(path.get(i).getY() * width);
            rec.setFill(Color.BLUE);

            rec.setWidth(width*0.7);
            rec.setHeight(width*0.7);
            rec.setArcHeight(200);
            rec.setArcWidth(200);

            currentPane.getChildren().add(rec);


        }


    }


    /**
     * Sukuria grida piesimui arba sugeneruoja labirinta
     * @param w width
     * @param h height
     * @param maze maze if true, grid if false
     * @return pane
     */
    public Pane generateGrid(int w, int h, boolean maze){

        if(maze == true){
            currentHBox.getChildren().get(10).disableProperty().setValue(false);
        }


        if(root.getChildren().contains(currentPane)){
            root.getChildren().remove(currentPane);
        }

        //tile width
        width = 550/(w);
        Pane p = new Pane();
        p.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));


        Rectangle [][] rec = new Rectangle [w][h];
        MazeGenerator newMaze = null;

        if(maze != false){
            if(mazeGen.getSelectionModel().getSelectedItem() == "Prim's"){
                newMaze = new MazeGenerator(w,h,"Prim");
            }
            if(mazeGen.getSelectionModel().getSelectedItem() == "Aldous-Broder"){
                newMaze = new MazeGenerator(w,h,"Aldous-Broder");
            }
            currentMaze = new Maze(newMaze.getGrid());

        }

        else{
            currentMaze = new Maze(new int[w][h]);
            solveCount = 0;
        }


        for(int i=0; i<w; i++){
            for(int j=0; j<h; j++){

                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * width);
                rec[i][j].setY(j * width);
                rec[i][j].setWidth(width);
                rec[i][j].setHeight(width);
                if(maze!= false){
                    switch (newMaze.getGrid()[i][j]){
                        case 0:{
                            rec[i][j].setFill(null);
                            break;

                        }
                        case 1:{
                            rec[i][j].setFill(Color.BLACK);
                            rec[i][j].setStroke(Color.BLACK);
                            break;

                        }
                        case 2:{
                            rec[i][j].setFill(Color.GREEN);
                            break;

                        }
                        case 3:{
                            rec[i][j].setFill(Color.RED);
                            break;

                        }
                    }

                }
                else{
                    rec[i][j].setFill(null);
                    rec[i][j].setStroke(Color.BLACK);
                }


                p.getChildren().add(rec[i][j]);

            }
        }



       EventHandler draw = (EventHandler<MouseEvent>) me -> {

           double posX = me.getX();
           double posY = me.getY();

           int colX = (int)(posX / width);
           int colY = (int) (posY / width);
           if(me.getButton() == MouseButton.SECONDARY){

               if( rec[colX][colY].getFill()==Color.RED ||  rec[colX][colY].getFill()==Color.GREEN ){
                   solveCount--;
               }
               rec[colX][colY].setFill(Color.LIGHTGREY);
               currentMaze.replace(colX,colY,0);

               return;
           }

           switch (cbColor.getValue()){
               case "Start":{
                   rec[colX][colY].setFill(Color.GREEN);
                   //negali buti 2 pradzios


                   solveCount++;

                   currentMaze.replace(colX,colY,2);
                   cbColor.getSelectionModel().select("End");
                   break;
               }
               case "End":{
                   rec[colX][colY].setFill(Color.RED);
                   //negali buti 2 pabaigos


                   currentMaze.replace(colX,colY,3);
                   solveCount++;
                  cbColor.getSelectionModel().select("Wall");
                   break;
               }
               case "Wall":{
                   rec[colX][colY].setFill(Color.BLACK);
                   currentMaze.replace(colX,colY,1);
                   break;
               }



           }

           if(solveCount >= 2){
               currentHBox.getChildren().get(10).disableProperty().setValue(false);
           }

       };


        p.setOnMouseDragged(draw);
        p.setOnMouseClicked(draw);
        currentPane = p;


        p.setMinSize(0,0);
        root.add(currentPane,0,0);


        //Pritaikome lango dydi pagal labirinta
        adjustWindowSize(p,h,w);

        return p;
    }

    private void adjustWindowSize(Pane p, int h, int w){
        if(((Stage)p.getScene().getWindow()).getMinHeight()  < width* h ||
                ((Stage)p.getScene().getWindow()).getMinWidth() < width * w ){
            ((Stage)p.getScene().getWindow()).setHeight(width * h + 120);
            ((Stage)p.getScene().getWindow()).setWidth(width * w +20);

        }
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Maze");
        stage.setMinHeight(650);
        stage.setMinWidth(750);
        stage.setResizable(true);

        root = new GridPane();
        root.setPadding(new Insets(0,0,-25,5));
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root,Color.RED);
        createControls();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:logo.png"));
        stage.show();

    }

    private class VisualParameters{
        final private TextField Height = addTextField1("Height=", "20", 35);
        final private TextField Width = addTextField1("Width=", "20", 35);

        int getH(){
            return Integer.parseInt(Height.getText());
        }
        int getW() {
            return Integer.parseInt(Width.getText());
        }

        public TextField addTextField1(String name, String defValue, double width){
            Text label = new Text("  " + name);
            currentHBox.getChildren().add(label);
            TextField tf = new TextField(defValue);
            tf.setPrefWidth(width);
            currentHBox.getChildren().add(tf);
            return tf;
        }

        public ComboBox<String> addComboBox1(String name,
                                             String... choices){
            Text label = new Text("  " + name);
            currentHBox.getChildren().add(label);
            ComboBox<String> cb = new ComboBox<>();
            cb.getItems().addAll(choices);
            cb.getSelectionModel().selectFirst();

            currentHBox.getChildren().add(cb);
            return cb;
        }


    }

}
