
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;


public class RoomInfoDialog extends Dialog {

    /* private Label label1,label2,label3,label4,label5;
    private TextField occupantText, positionText, numberText, floorText, sizeText;
    private Button colourButton;
    private ButtonType confirm;*/

    FloorBuilderView v;

    public RoomInfoDialog(Stage owner, String title, Room room, int floorNum, Building m) {

        setTitle(title);
        TextField occupantText = new TextField();
        TextField positionText = new TextField();
        TextField numberText = new TextField();
        occupantText.setPromptText("Person who occupies this room");
        positionText.setPromptText("Job position/title of this person");
        numberText.setPromptText("The room number");
        occupantText.setText(room.getOccupant());
        positionText.setText(room.getPosition());
        numberText.setText(room.getNumber());
        TextField floorText = new TextField(m.getFloorPlan(floorNum).getName());
        floorText.setEditable(false);
        TextField sizeText = new TextField(room.getNumberOfTiles() + " Sq. Ft.");
        floorText.setEditable(false);
        Button colourButton = new Button();
        colourButton.setMinWidth(145);
        colourButton.setStyle("-fx-base:" + v.ROOM_COLORS[room.getColorIndex()] + "");
        colourButton.setFocusTraversable(false);
        Label label1 = new Label("Occupant:");
        Label label2 = new Label("Position:");
        Label label3 = new Label("Number:");
        Label label4 = new Label("Floor:");
        Label label5 = new Label("Size:");
        ButtonType confirm = new ButtonType("OK",
                ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);

        GridPane dialogContents = new GridPane();
        dialogContents.add(label1,0,0);
        dialogContents.add(label2, 0,1);
        dialogContents.add(label3,0,2);
        dialogContents.add(label4, 0,3);
        dialogContents.add(label5,0,4);
        dialogContents.add(occupantText,1,0,2,1);
        dialogContents.add(positionText,1,1,2,1);
        dialogContents.add(numberText,1,2,1,1);
        dialogContents.add(colourButton, 2,2);
        dialogContents.add(floorText,1,3,2,1);
        dialogContents.add(sizeText,1,4,2,1);
        dialogContents.setPadding(new Insets(10,10,10,10));
        dialogContents.setVgap(10);
        dialogContents.setHgap(10);
        getDialogPane().setContent(dialogContents);

        //Update information if the ok button is pressed
        setResultConverter(new Callback<ButtonType, Room>() {
            public Room call(ButtonType b) {
                if (b == confirm) {
                    room.setOccupant(occupantText.getText());
                    room.setPosition(positionText.getText());
                    room.setNumber(numberText.getText());
                }
                return room;
            }
        });
    }
}


