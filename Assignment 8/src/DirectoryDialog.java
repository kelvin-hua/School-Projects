import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;

public class DirectoryDialog extends Dialog{
    public DirectoryDialog(Stage primaryStage, String title, Building m, int nR) {
        setTitle(title);

        ArrayList<Room> occupantInfo = new ArrayList<>();
        ArrayList<String> occupantRoom = new ArrayList<>();
        ListView<String> roomOwners = new ListView<String>();

        for (int numFloor = 0; numFloor < m.MAXIMUM_FLOORPLANS; numFloor++) {
            for (int numRoom = 0; numRoom < m.getFloorPlan(numFloor).getNumberOfRooms(); numRoom++) {
                Room person = m.getFloorPlan(numFloor).getRooms()[numRoom];
                occupantInfo.add(person);
                occupantRoom.add(person.getNumber() + " - " + person.getOccupant() + " (" + person.getPosition() + ")");
            }
        }

        roomOwners.setItems(FXCollections.observableArrayList(occupantRoom));
        roomOwners.setPrefSize(600,250);

        ButtonType confirm = new ButtonType("OK",
                ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(confirm);

        Button button = new Button("Search");
        button.setMinWidth(600);

        GridPane gP = new GridPane();
        gP.add(roomOwners,0,0);
        gP.add(button,0,1);
        gP.setPadding(new Insets(10,10,10,10));
        gP.setVgap(10);
        gP.setHgap(10);

        getDialogPane().setContent(gP);

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input Required");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the full name of the person that you are searching for:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    for (int numOccupant=0; numOccupant<occupantRoom.size(); numOccupant++) {
                        Room personelle = occupantInfo.get(numOccupant);
                        if (Objects.equals(personelle.getOccupant(), result.get())) {
                            int floorNum = 0;
                            for (int floorFind=0; floorFind<m.MAXIMUM_FLOORPLANS; floorFind++) {    // FINDING FLOOR NUMBER
                                for (int numRoom = 0; numRoom < m.getFloorPlan(floorFind).getNumberOfRooms(); numRoom++) {
                                    if (personelle == m.getFloorPlan(floorFind).getRooms()[numRoom]) {
                                        floorNum = floorFind;
                                    }
                                }
                            }
                            Alert dialog2 = new Alert(Alert.AlertType.INFORMATION);
                            dialog2.setTitle("Search Results");
                            dialog2.setHeaderText(null);
                            dialog2.setContentText(personelle.getOccupant() + " is our " + personelle.getPosition() + " and can be located on the " + m.getFloorPlan(floorNum).getName() + " in room " + personelle.getNumber());
                            dialog2.showAndWait();
                            break;
                        }
                        else if (numOccupant == occupantRoom.size()-1){
                            Alert dialog3 = new Alert(Alert.AlertType.INFORMATION);
                            dialog3.setTitle("Search Results");
                            dialog3.setHeaderText(null);
                            dialog3.setContentText("That name does not match with anyone in our records, please try again.");
                            dialog3.showAndWait();
                        }
                    }
                }
            }
        });

    }
}
