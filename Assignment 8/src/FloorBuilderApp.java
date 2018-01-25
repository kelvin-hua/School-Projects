import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;

public class FloorBuilderApp extends Application{
    private Building model;
    private FloorBuilderView view;


    public void start(Stage primaryStage) {
        model = Building.example();
        view = new FloorBuilderView(model);

        for (int row=0; row<model.getFloorPlan(view.getCurrentFloor()).size(); row++) {
            for (int col = 0; col < model.getFloorPlan(view.getCurrentFloor()).size(); col++) {
                view.getFloor()[col][row].setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent actionEvent) {
                        handleGridClick(actionEvent, primaryStage);
                        view.update();
                    }
                });
            }
        }

        for (int i=0; i<4; i++) {
            view.getRadios()[i].setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    view.update();
                }
            });
        }

        view.getColourChange().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                if (view.getCurrentColour() == 11) {
                    view.setCurrentColour(-11);
                    view.getColourChange().setStyle("-fx-base:" + view.getRoomColors()[view.getCurrentColour()] + "");
                }
                else {
                    view.getColourChange().setStyle("-fx-base:" + view.getRoomColors()[view.getCurrentColour() + 1] + "");
                    view.setCurrentColour(1);
                }
            }
        });

        view.getFourthF().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                view.setCurrentFloor(3);
                view.update();
            }
        });

        view.getThirdF().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                view.setCurrentFloor(2);
                view.update();
            }
        });

        view.getSecondF().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                view.setCurrentFloor(1);
                view.update();
            }
        });

        view.getMainF().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                view.setCurrentFloor(0);
                view.update();
            }
        });

        view.getBasementF().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                view.setCurrentFloor(4);
                view.update();
            }
        });

        view.getOverview().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Dialog buildingOverview = new BuildingDialog(primaryStage, "Building Overview", model);
                buildingOverview.showAndWait();
            }
        });

        primaryStage.setTitle("Floor Plan Builder");
        primaryStage.setScene(new Scene(view, 1000,1020));
        primaryStage.show();

        view.update();
    }

    private void handleGridClick(ActionEvent actionEvent, Stage primaryStage) {
        for (int row=0; row<model.getFloorPlan(view.getCurrentFloor()).size(); row++) {
            for (int col = 0; col < model.getFloorPlan(view.getCurrentFloor()).size(); col++) {
                if (actionEvent.getSource() == view.getFloor()[col][row]) {
                    if (view.getRadios()[0].isSelected()) {
                        if (model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col) != null) {
                            int tileColour = model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex();
                            model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).removeTile(row,col);
                            if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(tileColour).getNumberOfTiles() == 0) {
                                model.getFloorPlan(view.getCurrentFloor()).removeRoom(model.getFloorPlan(view.getCurrentFloor()).roomWithColor(tileColour));
                            }
                        }
                        model.getFloorPlan(view.getCurrentFloor()).setWallAt(row,col,!model.getFloorPlan(view.getCurrentFloor()).wallAt(row,col));
                    }
                    else if (view.getRadios()[1].isSelected()) {
                        if (model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col) != null) {
                            int tileColour = model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex();
                            model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).removeTile(row,col);
                            if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(tileColour).getNumberOfTiles() == 0) {
                                model.getFloorPlan(view.getCurrentFloor()).removeRoom(model.getFloorPlan(view.getCurrentFloor()).roomWithColor(tileColour));
                            }
                        }
                        if (model.hasExitAt(row,col)) {
                            model.removeExit(row,col);
                        }
                        else {
                            model.addExit(row,col);
                        }
                    }
                    else if (view.getRadios()[2].isSelected()) {
                        if (!model.hasExitAt(row,col) && !model.getFloorPlan(view.getCurrentFloor()).wallAt(row,col)) {
                            if(model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col) == null) {
                                if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()) != null) {
                                    model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()).addTile(row,col);
                                }
                                else {
                                    model.getFloorPlan(view.getCurrentFloor()).addRoomAt(row, col).setColorIndex(view.getCurrentColour());
                                }
                            }
                            else if (model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex() == view.getCurrentColour()) {
                                model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).removeTile(row,col);
                                if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()).getNumberOfTiles() == 0) {
                                    model.getFloorPlan(view.getCurrentFloor()).removeRoom(model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()));
                                }
                            }
                            else if (model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex() != view.getCurrentColour()) {
                                if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex()).getNumberOfTiles() == 1) {
                                    int tileColour = model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).getColorIndex();
                                    model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).removeTile(row,col);
                                    model.getFloorPlan(view.getCurrentFloor()).removeRoom(model.getFloorPlan(view.getCurrentFloor()).roomWithColor(tileColour));
                                }
                                else {
                                    model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col).removeTile(row,col);
                                }
                                if (model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()) == null) {
                                    model.getFloorPlan(view.getCurrentFloor()).addRoomAt(row, col).setColorIndex(view.getCurrentColour());
                                }
                                else {
                                    model.getFloorPlan(view.getCurrentFloor()).roomWithColor(view.getCurrentColour()).addTile(row,col);
                                }


                            }
                        }

                    }
                    else if (view.getRadios()[3].isSelected()) {
                        if (model.getFloorPlan(view.getCurrentFloor()).roomAt(row, col) == null) {
                            view.getAlert().showAndWait();
                        }
                        else {
                            Dialog dialog = new RoomInfoDialog(primaryStage,"Room Details", model.getFloorPlan(view.getCurrentFloor()).roomAt(row,col), view.getCurrentFloor(), model);
                            dialog.showAndWait();
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) { launch(args); }

}
