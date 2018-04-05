package assignment5;

import javafx.fxml.FXML;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class worldController {
    protected static final int boxSize = 15;
    @FXML protected GridPane worldGrid;

    public void initialize() {
        for (int i = 0; i < Params.world_width; i++) {
            worldGrid.getColumnConstraints().add(new ColumnConstraints(boxSize));
        }
        for (int i = 0; i < Params.world_height; i++) {
            worldGrid.getRowConstraints().add(new RowConstraints(boxSize));
        }
    }

}
