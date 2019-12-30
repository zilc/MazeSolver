

package GUI;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public abstract class BaseGraphics extends BaseApps {
    private Color canvasColor = Color.CORAL.brighter();
    protected double canvasW = 500, canvasH = 300;
    protected Canvas canvas = null;
    protected GraphicsContext gc = null; // naudojamas piešimui ant drobės
    protected ObservableList<Node> nodes = null;  // Alias dėl vaikų sąrašo
    private static final Random rnd = new Random(System.currentTimeMillis());

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(canvasW, canvasH);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        Group baseGroup   = new Group();
        Group shapesGroup = new Group();
        nodes = shapesGroup.getChildren();  // Alias dėl vaikų sąrašo užrašo
        baseGroup.getChildren().addAll(canvas, shapesGroup);
        addNode(baseGroup);
        addNewHBox();
        if(stage.getTitle() == null)
            stage.setTitle("BaseGraphics");
        super.start(stage);
    }
    public void setCanvas(Color backColor, double width, double height){
        this.canvasColor = backColor;
        this.canvasW = width;
        this.canvasH = height;

    }
    public void clearCanvas(){
        gc.setFill(canvasColor);
        gc.fillRect(0, 0, canvasW, canvasH);
    }
    public void baseGrid() {
        double step = 50;
        gc.setLineWidth(0.5);         // linijos plotis galimai mažesnis
        for(double u = step; u < Math.max(canvasW, canvasH); u += step) {
            gc.setStroke(u%100==0? Color.GREY: Color.GREY.brighter());
            gc.strokeLine(0, u, canvasW, u);   // horizontalios linijos
            gc.strokeLine(u, 0, u, canvasH);   // vertikalios linijos
        }
    }
    public static Color randomColor(){
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }
    public static Color randomColor(double opacity){
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255),
                opacity);
    }

}
