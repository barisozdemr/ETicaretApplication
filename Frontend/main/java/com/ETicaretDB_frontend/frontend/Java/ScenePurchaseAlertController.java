
package com.ETicaretDB_frontend.frontend.Java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ScenePurchaseAlertController implements IOkAlert {
    @FXML
    private Button okButton;
    
    public void okButtonPressed(ActionEvent e)
    {
        Stage stage = (Stage)okButton.getScene().getWindow();
        stage.close();
    }
}
