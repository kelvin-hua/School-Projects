import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BuildingDialog extends Dialog {
    FloorBuilderView v;
    public BuildingDialog (Stage owner, String title, Building m) {
        setTitle(title);
        Label label1 = new Label("Num Floors:");
        Label label2 = new Label("Num Exits:");
        Label label3 = new Label("Num Rooms:");
        Label label4 = new Label("Total Size:");
        TextField floorText = new TextField(m.getNumFloors() + "");
        floorText.setEditable(false);
        TextField exitText = new TextField(m.getNumExits() + "");
        exitText.setEditable(false);
        TextField roomText = new TextField();
        roomText.setEditable(false);
        TextField sizeText = new TextField();
        sizeText.setEditable(false);
        int nR = 0;
        int sizeReduction = 0;
        int totalSize = m.getNumFloors() * m.getFloorPlan(0).size() * m.getFloorPlan(0).size();
        ButtonType confirm = new ButtonType("OK",
                ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(confirm);

        // GETTING VALUES FOR ALL TEXTFIELDS
        for (int numFloor=0; numFloor<m.MAXIMUM_FLOORPLANS; numFloor++) {
            if (m.getFloorPlan(numFloor) != null) {
                nR += m.getFloorPlan(numFloor).getNumberOfRooms();
            }
            for (int row=0; row<m.getFloorPlan(numFloor).size(); row++) {
                for (int col = 0; col < m.getFloorPlan(numFloor).size(); col++) {
                    if (m.getFloorPlan(numFloor).wallAt(col,row)) {
                        sizeReduction -= 1;
                    }
                }
            }
        }
        totalSize += sizeReduction;
        roomText.setText(nR + "");
        sizeText.setText(totalSize + "");

        Button button = new Button("Directory Listing");
        button.setMinWidth(250);

        GridPane gPane = new GridPane();
        gPane.add(label1,0,0);
        gPane.add(label2,0,1);
        gPane.add(label3,0,2);
        gPane.add(label4,0,3);
        gPane.add(floorText,1,0);
        gPane.add(exitText,1,1);
        gPane.add(roomText,1,2);
        gPane.add(sizeText,1,3);
        gPane.add(button,0,4,2,1);
        gPane.setPadding(new Insets(10,10,10,10));
        gPane.setHgap(10);
        gPane.setVgap(10);
        int sendnR = nR;

        getDialogPane().setContent(gPane);

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Dialog dialog = new DirectoryDialog(owner,"Room Details", m, sendnR);
                dialog.showAndWait();
            }
        });
    }
}
