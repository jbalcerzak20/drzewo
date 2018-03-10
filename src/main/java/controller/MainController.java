package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import model.DataLoader;
import model.Person;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class MainController {

    @FXML
    private HBox hbox;
    @FXML
    private Button add;
    @FXML
    private TableView<Person> tableView;
    private TableColumn columnName;
    private TableColumn columnSurname;


    ////////////////////////////////////////
    @FXML    private TextField name;
    @FXML    private TextField surname;
    @FXML    private TextField familyName;
    @FXML    private TextField placeOfBirth;
    @FXML    private DatePicker dateOfBirth;
    @FXML    private DatePicker dateOfDeath;
    @FXML    private TextField burialPlace;
    @FXML    private TextArea description;
    @FXML    private ComboBox<Person> mother;
    @FXML    private ComboBox<Person> father;
    @FXML    private GridPane gridPane;
    @FXML    private RadioButton men;
    @FXML    private RadioButton women;
             private ToggleGroup toggleGroup;
    ////////////////////////////////////////
    @FXML    private AnchorPane content;
    ////////////////////////////////////////
    public Element elementInUse;
    private HashMap<Element,Person> elements;
    ///////////////////////////
    DataLoader dataLoader = new DataLoader();
    JSONArray jsonArray;
    JSONObject jsonObject;
    ////////////////////////
    MainController mainController = this;



    public MainController() {
        elements = new HashMap<>();
    }

    @FXML
    public void initialize()
    {
        columnName = new TableColumn("Imie");
        columnSurname = new TableColumn("Nazwisko");
        columnName.setCellValueFactory(new PropertyValueFactory<Person,String>("name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<Person,String>("surname"));
        tableView.getColumns().addAll(columnName,columnSurname);

        gridPane.setVisible(false);

        addNewPerson();

        jsonObject = dataLoader.getDataAsJSON();
        jsonArray = jsonObject.getJSONArray("elements");
        inicializeElements();
        add.addEventHandler(MouseEvent.DRAG_DETECTED,actionHandler());
        content.addEventHandler(DragEvent.DRAG_OVER,dragOverHandler());
        content.addEventHandler(DragEvent.DRAG_DROPPED,dragDroppedHandle());

        toggleGroup = new ToggleGroup();
        women.setToggleGroup(toggleGroup);
        men.setToggleGroup(toggleGroup);


        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

//            System.out.println(newValue);

        });


    }


    public EventHandler actionHandler()
    {
        EventHandler handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                unBindAll();
                Dragboard dragboard = add.startDragAndDrop(TransferMode.COPY);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString("add");
                dragboard.setContent(clipboardContent);
                event.consume();
            }
        };
        return handler;
    }

    public EventHandler dragOverHandler()
    {
        EventHandler eventHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
            if(event.getGestureSource()!=content && event.getDragboard().getString().equals("add"))
            {
                event.acceptTransferModes(TransferMode.COPY);
            }

            if(event.getGestureSource()!=content && event.getDragboard().getString().equals("move"))
            {
                event.acceptTransferModes(TransferMode.MOVE);
                elementInUse.setLayoutX(event.getX());
                elementInUse.setLayoutY(event.getY());
            }

            event.consume();
            }
        };

        return eventHandler;
    }

    public EventHandler dragDroppedHandle()
    {
        EventHandler eventHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();

                if(dragboard.getString().equals("add")) {
                    Person person = new Person(mainController,"","");
                    Element element = person.getElement();
                    element.label.textProperty().bindBidirectional(person.labelProperty());
                    element.setLayoutX(event.getX());
                    element.setLayoutY(event.getY());

                    elementInUse = element;
                    tableView.getItems().add(person);
                    elements.put(element,person);

                    //rozpoczecie przesuwania elementu
                    element.setDragDetected();

                    //akcja doubleclick na elemencie
                    element.setMouseClick(gridPane);

                    content.getChildren().add(element);
                }

                if(dragboard.getString().equals("move")) {
                    elementInUse.setLayoutX(event.getX());
                    elementInUse.setLayoutY(event.getY());
                }


                event.setDropCompleted(true);
                event.consume();
            }
        };
        return eventHandler;
    }

    private void addNewPerson()
    {
        father.valueProperty().addListener((observable, oldValue, newValue) -> {
            Person person = getPersonFromElements(elementInUse);
            person.setFather(oldValue);

        });

        mother.valueProperty().addListener((observable, oldValue, newValue) -> {
            Person person = getPersonFromElements(elementInUse);
            person.setMother(oldValue);
        });

    }

    private void setCombobox()
    {
        father.itemsProperty().bindBidirectional(tableView.itemsProperty());
        mother.itemsProperty().bindBidirectional(tableView.itemsProperty());
    }

    public void bindAll()
    {
        setCombobox();
        Person person = getPersonFromElements(elementInUse);

        name.textProperty().bindBidirectional(person.nameProperty());
        surname.textProperty().bindBidirectional(person.surnameProperty());
        familyName.textProperty().bindBidirectional(person.familyNameProperty());
        placeOfBirth.textProperty().bindBidirectional(person.placeOfBirthProperty());

        dateOfBirth.getEditor().textProperty().bindBidirectional(person.dateOfBirthProperty());
        dateOfDeath.getEditor().textProperty().bindBidirectional(person.dateOfDeathProperty());

        mother.getSelectionModel().select(person.getMother());
        father.getSelectionModel().select(person.getFather());

        burialPlace.textProperty().bindBidirectional(person.burialPlaceProperty());
        description.textProperty().bindBidirectional(person.descritpionsProperty());

        elementInUse.updateLine();
    }

    public void unBindAll()
    {
        if(elementInUse==null)
            return;

        elementInUse.dropBorder();
        Person person = getPersonFromElements(elementInUse);
        name.textProperty().unbindBidirectional(person.nameProperty());
        surname.textProperty().unbindBidirectional(person.surnameProperty());

        father.itemsProperty().unbindBidirectional(tableView.itemsProperty());
        mother.itemsProperty().unbindBidirectional(tableView.itemsProperty());

        familyName.textProperty().unbindBidirectional(person.familyNameProperty());
        placeOfBirth.textProperty().unbindBidirectional(person.placeOfBirthProperty());

        dateOfBirth.getEditor().textProperty().unbindBidirectional(person.dateOfBirthProperty());
        dateOfDeath.getEditor().textProperty().unbindBidirectional(person.dateOfDeathProperty());

        mother.getSelectionModel().select(null);
        father.getSelectionModel().select(null);

        burialPlace.textProperty().unbindBidirectional(person.burialPlaceProperty());
        description.textProperty().unbindBidirectional(person.descritpionsProperty());

    }

    public Person getPersonFromElements(Element element)
    {
        return elements.get(element);
    }





    @FXML
    public void confirmOnAction(ActionEvent event)
    {
        unBindAll();
        gridPane.setVisible(false);
    }

    @FXML
    public void formatOnAction()
    {
        content.getChildren().forEach(node -> {
            Element element = (Element)node;
            double x = element.getLayoutX();
            double y = element.getLayoutY();

            double my = y%60;
            double mx = x%10;

            double newY = 0;
            double newX = 0;
            if(my<=30)
                newY = -1*my;
            else
                newY = my;

            if(mx<=5)
                newX = -1*mx;
            else
                newX = mx;

            element.setLayoutY(y+newY);
            element.setLayoutX(x+newX);

        });
    }

    @FXML
    public void saveToFileOnAction()
    {

        JSONObject json = null;
        for(Person person: tableView.getItems())
        {
            json = new JSONObject();
            json.put("name",person.getName());
            json.put("surname",person.getSurname());
            json.put("familyName",person.getFamilyName());
            json.put("placeOfBirth",person.getPlaceOfBirth());
            json.put("dateOfBirth",person.getDateOfBirth());
            json.put("dateOfDeath",person.getDateOfDeath());
            json.put("burialPlace",person.getBurialPlace());
            json.put("description",person.getDescritpions());
            json.put("sex",person.getSex().toString());
            json.put("uniqueID",person.getUniqueID());
            json.put("layoutx",person.getElement().getLayoutX());
            json.put("layouty",person.getElement().getLayoutY());
            jsonArray.put(json);
        }
        jsonObject.put("elements",jsonArray);
        dataLoader.saveJSONToFile(jsonObject);

    }

    public void inicializeElements()
    {
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject js = jsonArray.getJSONObject(i);
                Person person = new Person(this, getValueJson(js,"name"), getValueJson(js,"surname"), getValueJson(js,"familyName"));
                person.setBurialPlace(getValueJson(js,"burialPlace"));
                person.setDescritpions(getValueJson(js,"description"));
                person.setDateOfBirth(getValueJson(js,"dateOfBirth"));
                person.setDateOfDeath(getValueJson(js,"dateOfDeath"));
                person.setPlaceOfBirth(getValueJson(js,"placeOfBirth"));
                person.getElement().setLayoutX(js.getDouble("layoutx"));
                person.getElement().setLayoutY(js.getDouble("layouty"));
                person.setUniqueID(getValueJson(js,"uniqueID"));
                person.getElement().label.textProperty().bindBidirectional(person.labelProperty());
                tableView.getItems().add(person);
                content.getChildren().add(person.getElement());
                elements.put(person.getElement(), person);
                person.getElement().setMouseClick(gridPane);
                person.getElement().setDragDetected();

//                person.getElement().layoutXProperty().add(100);

//                Line line = new Line();
//                line.setStartX(20);
//                line.setStartY(20);
//                line.endXProperty().bind(person.getElement().layoutXProperty().add(60));
//                line.endYProperty().bind(person.getElement().layoutYProperty().add(15));
//                content.getChildren().add(line);
//                line.toBack();

        }

        jsonArray = new JSONArray();


    }

    public String getValueJson(JSONObject json,String key)
    {
        if(json.has(key))
            return json.getString(key);
        else
            return "";
    }

    public void addChild(Node n)
    {
        content.getChildren().add(n);
    }

    public AnchorPane getContent()
    {
        return content;
    }

}
