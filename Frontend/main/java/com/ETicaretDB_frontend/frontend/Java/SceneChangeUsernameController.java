
package com.ETicaretDB_frontend.frontend.Java;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneChangeUsernameController {
    
    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private Label notificationChangeUsername;
    
    private final Stage mainStage; //stores the main stage to close it
    
    public SceneChangeUsernameController(Stage mainStage)
    {
        this.mainStage = mainStage;
    }
    
    public void changeButtonPressed(ActionEvent e) throws IOException //action
    {
         tryChangeUsername();
    }
    
    public void enterKeyPressed(KeyEvent e) throws IOException //action
    {
        if (e.getCode() == KeyCode.ENTER)
        {
            tryChangeUsername();
        }
    }
    
    // Tries to change username
    public void tryChangeUsername() throws IOException
    {
        if(!newUsernameField.getText().equals(DataStore.loggedUsersName))
        {
            int id = DataStore.loggedUsersId;
            String email = DataStore.loggedUsersEmail;

            if (Log.updateUsername(id, newUsernameField.getText(), email, oldPasswordField.getText(), oldPasswordField.getText())) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneChangeUsernameSuccesfullAlert.fxml"));

                    Stage newStage = new Stage();

                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Info");
                    newStage.getIcons().add(DataStore.programLogo);
                    newStage.setResizable(false);
                    newStage.show();

                    mainStage.close();

                    Parent root2 = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneSignIn-Primary.fxml"));

                    Stage stage = (Stage) newUsernameField.getScene().getWindow();

                    stage.setScene(new Scene(root2));
                } catch (IOException ex) {
                    System.out.print("/com/ETicaretDB_frontend/frontend/Views/SceneChangeUsernameSuccesfullAlert.fxml or\n");
                    System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneSignIn-Primary.fxml could not be loaded");
                }
            } else {
                if(Log.updateUserNotification != null){
                    notificationChangeUsername.setText(Log.updateUserNotification);
                }
                else {
                    notificationChangeUsername.setText("Unknown error");
                }
            }
        }
        else{
            notificationChangeUsername.setText("You already have the same username!");
        }
    }
    
    public void notificationSetNull(MouseEvent e) //clears notificationLabel
    {
        if(notificationChangeUsername.getText() != null)
        {
            notificationChangeUsername.setText("");
        }
    }
}
