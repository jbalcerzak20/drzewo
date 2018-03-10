package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;

import java.util.Objects;

public class Element extends VBox {

    public Label label = new Label();
    public ImageView imageView = new ImageView();
    private Image image;
    private HBox hBox = new HBox();
    private Line line = new Line();
    private MainController mainController;
    //////////special points //////////
    private Pane leftPoint = new Pane();
    private Pane rightPoint = new Pane();
    public  Pane topPoint = new Pane();
    private Pane bottomPoint = new Pane();

    //////////////////////////////
    private static Line link = new Line();

    public Element()
    {
        setWidth(50);
        setHeight(50);
//        setBackground(new Background(new BackgroundFill(Color.AQUA,new CornerRadii(10),null)));
        label.setText("");
        hBox.setBackground(new Background(new BackgroundFill(Color.BISQUE,new CornerRadii(10),null)));

        image = new Image("women.jpg");
        imageView.setImage(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        getChildren().add(hBox);
        hBox.getChildren().add(imageView);
        hBox.getChildren().add(line);
        hBox.getChildren().add(label);
        hBox.setSpacing(3);
        hBox.setPadding(new Insets(5,5,5,5));
//        hBox.setMaxWidth(100);

        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(50);
        label.setPadding(new Insets(0,5,0,5));
        label.setFont(new Font(10));

        getChildren().add(0,topPoint);
        getChildren().add(2,bottomPoint);
        hBox.getChildren().add(0,leftPoint);
        hBox.getChildren().add(4,rightPoint);

        initialPanes();

        setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

    }

    public Element(MainController mainController)
    {
        this();
        this.mainController = mainController;
    }

    public void setBorder()
    {
        super.setBorder(new Border(new BorderStroke(Color.BLUE,BorderStrokeStyle.DASHED,null,BorderWidths.DEFAULT)));
    }

    public void dropBorder()
    {
        super.setBorder(null);
    }

    private void initialPanes()
    {
        leftPoint.setBackground(backgroundFactory(Color.TRANSPARENT));
        rightPoint.setBackground(backgroundFactory(Color.TRANSPARENT));
        topPoint.setBackground(backgroundFactory(Color.TRANSPARENT));
        bottomPoint.setBackground(backgroundFactory(Color.TRANSPARENT));

        leftPoint.setPrefSize(10,20);
        rightPoint.setPrefSize(10,20);
        topPoint.setPrefSize(10,20);
        bottomPoint.setPrefSize(10,20);

        leftPoint.addEventHandler(MouseEvent.MOUSE_ENTERED,mouseEnterHandle());
        leftPoint.addEventHandler(MouseEvent.MOUSE_EXITED,mouseExitHandle());

        rightPoint.addEventHandler(MouseEvent.MOUSE_EXITED,mouseExitHandle());
        rightPoint.addEventHandler(MouseEvent.MOUSE_ENTERED,mouseEnterHandle());

        topPoint.addEventHandler(MouseEvent.MOUSE_ENTERED,mouseEnterHandle());
        topPoint.addEventHandler(MouseEvent.MOUSE_EXITED,mouseExitHandle());

        bottomPoint.addEventHandler(MouseEvent.MOUSE_ENTERED,mouseEnterHandle());
        bottomPoint.addEventHandler(MouseEvent.MOUSE_EXITED,mouseExitHandle());


        leftPoint.addEventHandler(MouseEvent.DRAG_DETECTED,lineDragDetect());
        leftPoint.addEventHandler(DragEvent.DRAG_OVER,lineDragOver());
        leftPoint.addEventHandler(DragEvent.DRAG_DROPPED,lineDragDropped());
    }


    private EventHandler lineDragDetect()
    {
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard dragboard = leftPoint.startDragAndDrop(TransferMode.ANY);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString("left");
                dragboard.setContent(clipboardContent);

                link = new Line();
                link.startXProperty().bind(layoutXProperty().add(30));
                link.startYProperty().bind(layoutYProperty().add(30));
                link.setEndX(getLayoutX());
                link.setEndY(getLayoutY());
                mainController.addChild(link);

                event.consume();
            }
        };

        return eventHandler;
    }

    private EventHandler lineDragOver()
    {
        EventHandler eventHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if(event.getDragboard().getString().equals("left")) {
                    event.acceptTransferModes(TransferMode.ANY);
                }

                event.consume();
            }
        };

        return eventHandler;
    }

    private EventHandler lineDragDropped()
    {
        EventHandler eventHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                link.endXProperty().bind(layoutXProperty().add(20));
                link.endYProperty().bind(layoutYProperty().add(30));
                link.toBack();
//                System.out.println(getLayoutX());
//                mainController.addChild(link);
                link=null;
                event.setDropCompleted(true);

                event.consume();
            }
        };

        return eventHandler;
    }




    private EventHandler mouseEnterHandle()
    {
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Pane pane = (Pane) event.getSource();
                pane.setBackground(backgroundFactory(Color.gray(0.93)));
            }
        };

        return  eventHandler;
    }

    private EventHandler mouseExitHandle()
    {
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Pane pane = (Pane) event.getSource();
                pane.setBackground(backgroundFactory(Color.TRANSPARENT));
            }
        };
        return  eventHandler;
    }


    private Background backgroundFactory(Color color)
    {
        return new Background(new BackgroundFill(color,null,null));
    }

    public void updateLine()
    {
        line.setEndY(hBox.getHeight());
    }

    @Override
    public String toString() {
        return String.valueOf(Objects.hashCode(this));
    }


    public void setDragDetected()
    {
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainController.elementInUse = (Element) event.getSource();
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString("move");
                dragboard.setContent(clipboardContent);
                event.consume();
            }
        };

        addEventHandler(MouseEvent.DRAG_DETECTED,eventHandler);
    }

    public void setMouseClick(GridPane gridPane)
    {
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2)
                {
                    mainController.unBindAll();
                    mainController.elementInUse = (Element) event.getSource();
                    setBorder();
                    mainController.bindAll();
                    gridPane.setVisible(true);
                }
            }
        };

        addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
    }

}
