import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class FloorBuilderView extends GridPane {
    private Label labelFL,labelSE,labelFS;
    private RadioButton walls, exits, room, selectRoom;
    private Button overview, colourChange;
    private Building model;
    private TextField botText;
    private ToggleGroup buttons;
    private FloorPlan completeFloor;
    private RadioButton[] radios = new RadioButton[4];
    private int currentColour=0;
    private Button[][] floor;
    private GridPane radioButtons, floorBoard;
    private Menu selectFloor;
    private MenuItem fourthF;
    private MenuItem thirdF;
    private MenuItem secondF;
    private MenuItem mainF;
    private MenuItem basementF;
    private MenuBar menuBar = new MenuBar();
    private int currentFloor=0;
    private Alert alert;
    public static final String[] ROOM_COLORS =
            {"ORANGE", "YELLOW", "LIGHTGREEN", "DARKGREEN", "LIGHTBLUE", "BLUE", "CYAN", "DARKCYAN", "PINK", "DARKRED", "PURPLE", "GRAY"};


    public FloorBuilderView(Building m){

        model = m;

        radioButtons = new GridPane();
        floor = new Button[model.getFloorPlan(0).size()][model.getFloorPlan(0).size()];
        floorBoard = new GridPane();

        fourthF = new MenuItem(model.getFloorPlan(3).getName());
        thirdF = new MenuItem(model.getFloorPlan(2).getName());
        secondF = new MenuItem(model.getFloorPlan(1).getName());
        mainF = new MenuItem(model.getFloorPlan(0).getName());
        basementF = new MenuItem(model.getFloorPlan(4).getName());

        selectFloor = new Menu("Select Floor");
        selectFloor.getItems().addAll(getFourthF(), getThirdF(), getSecondF(), getMainF(), new SeparatorMenuItem(), getBasementF());
        menuBar.getMenus().add(selectFloor);
        add(menuBar,0,0,2,1);

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Selection");
        alert.setHeaderText(null);
        alert.setContentText("You must select a tile that belongs to a room.");


        for (int row=0; row<model.getFloorPlan(0).size(); row++) {
            for (int col = 0; col < model.getFloorPlan(0).size(); col++) {
                floor[col][row] = new Button();
                floor[col][row].setPrefWidth(Integer.MAX_VALUE);
                floor[col][row].setPrefHeight(Integer.MAX_VALUE);
                floor[col][row].setStyle("-fx-base: WHITE;");
                floor[col][row].setFocusTraversable(false);
                floorBoard.add(floor[col][row], col, row);
            }
        }
        add(floorBoard,0,2);


        labelFL = new Label("FLOOR LAYOUT");
        add(labelFL,0,1);

        labelSE = new Label("SELECT/EDIT:");
        add(labelSE,1,1);

        labelFS = new Label("FLOOR SUMMARY:");
        add(labelFS,0,3);

        buttons = new ToggleGroup();
        radios = new RadioButton[4];
        String[] buttonLabels = {"Walls", "Exits", "Room Tiles", "Select Room"};
        int i;
        for (i=0; i<4; i++) {
            radios[i] = new RadioButton(buttonLabels[i]);
            radios[i].setMinWidth(100);
            radios[i].setMinHeight(30);
            radioButtons.add(radios[i],0,i);
            radioButtons.setMargin(radios[i], new Insets(0, 0, 5, 0));
            radios[i].setToggleGroup(buttons);
        }
        radios[0].setSelected(true);


        overview = new Button("Building Overview");
        radioButtons.add(overview,0,5,2,1);

        colourChange = new Button();
        colourChange.setMinWidth(25);
        colourChange.setMinHeight(25);
        colourChange.setDisable(true);
        radioButtons.add(colourChange,1,2);
        colourChange.setStyle("-fx-base:" + ROOM_COLORS[currentColour] + "");

        add(radioButtons,1,2);
        radioButtons.setMinWidth(155);
        radioButtons.setVgap(10);
        radioButtons.setPadding(new Insets(10,10,10,10));
        radioButtons.setMargin(overview, new Insets(10,0,0,0));

        botText = new TextField("Main Floor with 0 rooms.");
        add(botText,0,4,2,1);
        //botText.setMinWidth(20);

        setPadding(new Insets(0,10,10,10));
        setHgap(10);
        setVgap(10);
        setValignment(overview, VPos.TOP);



        update();
    }


    public RadioButton getWalls() {
        return walls;
    }

    public RadioButton getExits() {
        return exits;
    }

    public RadioButton getRoom() {
        return room;
    }

    public RadioButton getSelectRoom() {
        return selectRoom;
    }

    public Button getOverview() {
        return overview;
    }

    public Button getColourChange() {
        return colourChange;
    }

    public ToggleGroup getButtons() {
        return buttons;
    }

    public RadioButton[] getRadios() {
        return radios;
    }

    public int getCurrentColour() {
        return currentColour;
    }

    public String[] getRoomColors() {
        return ROOM_COLORS;
    }

    public void setCurrentColour(int x) {
        currentColour += x;
    }

    public Button[][] getFloor() {
        return floor;
    }

    public Menu getSelectFloor() { return selectFloor;}

    public GridPane getFloorBoard() {
        return floorBoard;
    }

    public MenuItem getFourthF() {
        return fourthF;
    }

    public MenuItem getThirdF() {
        return thirdF;
    }

    public MenuItem getSecondF() {
        return secondF;
    }

    public MenuItem getMainF() {
        return mainF;
    }

    public MenuItem getBasementF() {
        return basementF;
    }

    public Alert getAlert() { return alert;
    }

    public int getCurrentFloor() { return currentFloor;}
    public void setCurrentFloor(int newFloor) {
        currentFloor = newFloor;
    }


    public void update() {
        colourChange.setDisable(!radios[2].isSelected());

        for (int row=0; row<model.getFloorPlan(currentFloor).size(); row++) {
            for (int col = 0; col < model.getFloorPlan(currentFloor).size(); col++) {
                if (model.getFloorPlan(currentFloor).wallAt(col,row)){
                    floor[row][col].setStyle("-fx-base: BLACK");
                    floor[row][col].setText("");

                }
                else {
                    floor[row][col].setStyle("-fx-base: WHITE");
                    floor[row][col].setText("");
                }
                if (model.hasExitAt(col,row)){
                    floor[row][col].setStyle("-fx-base: RED");
                    floor[row][col].setText("EXIT");
                }

                if (model.getFloorPlan(currentFloor).roomAt(col,row) != null) {
                    floor[row][col].setStyle("-fx-base: " + ROOM_COLORS[model.getFloorPlan(currentFloor).roomAt(col, row).getColorIndex()] + "");
                }
            }

            botText.setText(model.getFloorPlan(currentFloor).getName() + " with " + model.getFloorPlan(currentFloor).getNumberOfRooms() + " rooms.");
        }

    }

}