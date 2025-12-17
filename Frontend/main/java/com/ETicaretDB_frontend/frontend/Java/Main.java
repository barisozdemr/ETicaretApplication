
package com.ETicaretDB_frontend.frontend.Java;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        System.out.println("FXML = " + getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneSignIn-Primary.fxml"));
        System.out.println("CSS = " + getClass().getResource("/com/ETicaretDB_frontend/frontend/Css/CssSceneLogOperations.css"));
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneSignIn-Primary.fxml")
            );

            Parent root = loader.load();
            
            String SceneSignInCss = this.getClass().getResource("/com/ETicaretDB_frontend/frontend/css/CssSceneLogOperations.css").toExternalForm();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneSignInCss);
            
            stage.setScene(scene);
            stage.setTitle("Taksitle! - Sign In"); //set stage title
            stage.getIcons().add(DataStore.programLogo); //set program logo
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneSignIn-Primary.fxml or\n" +
                    "/com/ETicaretDB_frontend/frontend/Css/CssSceneLogOperations.css\n" +
                    "could not be loaded.");
        }
    }
}
