package model;

import controller.Element;
import controller.MainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person implements Serializable {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty placeOfBirth = new SimpleStringProperty();
    private StringProperty dateOfBirth = new SimpleStringProperty();
    private StringProperty dateOfDeath = new SimpleStringProperty();
    private StringProperty burialPlace = new SimpleStringProperty(); //miejsce pochówku
    private StringProperty descritpions = new SimpleStringProperty();
    private Sex sex;
    private StringProperty label = new SimpleStringProperty();

    private Person mother = null;
    private Person father = null;
    private List<Person> children = new ArrayList<Person>();

    private Element element;
    private String uniqueID = UUID.randomUUID().toString();

    public Person()
    {
        sex = Sex.male;
    }

    public Person(MainController mainController) {
        this();
        element = new Element(mainController);

        name.addListener((observable, oldValue, newValue) -> {
            label.setValue(setLabel());
        });

        surname.addListener((observable, oldValue, newValue) -> {
            label.setValue(setLabel());
        });

        dateOfBirth.addListener((observable, oldValue, newValue) -> {
            label.setValue(setLabel());
        });

        dateOfDeath.addListener((observable, oldValue, newValue) -> {
            label.setValue(setLabel());
        });

        familyName.addListener((observable, oldValue, newValue) -> {
            label.setValue(setLabel());
        });
    }

    private String setLabel()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()+"\n");
        builder.append(getSurname()+" ");
        if(getFamilyName()!=null)
        builder.append("("+getFamilyName()+")");
        if(getDateOfBirth()!=null)
            builder.append("\nur. "+getDateOfBirth()+"");
        if(getDateOfDeath()!=null)
            builder.append("\nśp. "+getDateOfDeath());

        return builder.toString();
    }

    public Person(MainController mainController,String name) {
        this(mainController);
        setName(name);
    }

    public Person(MainController mainController,String name, String surname) {
        this(mainController,name);
        setSurname(surname);
    }

    public Person(MainController mainController,String name, String surname, String familyName) {
        this(mainController,name,surname);
        setFamilyName(familyName);
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getFamilyName() {
        return familyName.get();
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }

    public String getPlaceOfBirth() {
        return placeOfBirth.get();
    }

    public StringProperty placeOfBirthProperty() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth.set(placeOfBirth);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getDateOfDeath() {
        return dateOfDeath.get();
    }

    public StringProperty dateOfDeathProperty() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath.set(dateOfDeath);
    }

    public String getBurialPlace() {
        return burialPlace.get();
    }

    public StringProperty burialPlaceProperty() {
        return burialPlace;
    }

    public void setBurialPlace(String burialPlace) {
        this.burialPlace.set(burialPlace);
    }

    public String getDescritpions() {
        return descritpions.get();
    }

    public StringProperty descritpionsProperty() {
        return descritpions;
    }

    public void setDescritpions(String descritpions) {
        this.descritpions.set(descritpions);
    }

    public Person getMother() {
        return mother;
    }

    public void setMother(Person mother) {
        this.mother = mother;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public StringProperty labelProperty()
    {
        return label;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return getName()+" "+getSurname();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }
}
