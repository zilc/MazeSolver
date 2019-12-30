package GUI;

// https://hajsoftutorial.com/javafx-tutorial/


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class BaseApps extends Application {
    final static public String nL = "\n"; // alias dėl new Line (length()=1)
    //System.getProperty("line.separator"); -- netinka dėl TextArea
    private HBox currentHBox      = new HBox();
    final private VBox rootLayout = new VBox(currentHBox);

    @Override
    public void start(Stage stage) throws Exception {
        createControls();
        stage.setScene(new Scene(rootLayout));
        stage.show();
    }
    public void addNode(Node node){
        currentHBox.getChildren().add(node);
    }
    public Button addButton(String name, EventHandler<ActionEvent> action){
        Button btn = new Button(name);
        currentHBox.getChildren().add(btn);
        btn.setOnAction(action);
        return btn;
    }
    public Text addText(String text){
        Text txt = new Text(text);
        currentHBox.getChildren().add(txt);
        return txt;
    }
    public TextField addTextField(String name, String defValue, double width){
        Text label = new Text("  " + name);
        currentHBox.getChildren().add(label);
        TextField tf = new TextField(defValue);
        tf.setPrefWidth(width);
        currentHBox.getChildren().add(tf);
        return tf;
    }
    public ComboBox<String> addComboBox(String name,
                                        EventHandler<ActionEvent> action, String... choises){
        Text label = new Text("  " + name);
        currentHBox.getChildren().add(label);
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(choises);
        cb.getSelectionModel().selectFirst();
        cb.setOnAction(action);
        currentHBox.getChildren().add(cb);
        return cb;
    }
    public TextArea addTextArea(String colorName, double width, double height){
        TextArea ta = new TextArea();
        ta.setFont(Font.font ("Courier New", 12));
        ta.setPrefSize(width, height);
        ta.setStyle(".text-area {" +
                "-fx-text-fill:white;" +
                "-fx-control-inner-background:" + colorName + ";" +
                "-fx-highlight-fill:gray;}" );
        currentHBox.getChildren().add(ta);
        return ta;
    }
    public void addNewHBox(){
        currentHBox = new HBox(3.0);
        currentHBox.setPadding(new Insets(3.0));
        currentHBox.setAlignment(Pos.CENTER_LEFT);
        rootLayout.getChildren().add(currentHBox);
    }
    //    public void removeLastControl(){
//        currentHBox .getChildren().remove(currentHBox .getChildren().size()-1);
//    }
    // abstraktus metodas reikalaujantis, kad išvestinėse klasėse
    // būtų konkreti valdymo elementų realizacija
    public abstract void createControls();
}
