
package com.ETicaretDB_frontend.frontend.Java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import okhttp3.*;

import javax.xml.crypto.Data;

public class SceneProductController { // Scene of the chosen Product

    public static final String ip = DataStore.ip;
    
    @FXML
    private BorderPane borderPane;
    @FXML
    private ScrollPane paymentsScrollPane;
    @FXML
    private VBox commentsVbox;
    @FXML
    private VBox commentsParentVbox;
    @FXML
    private Menu usernameMenu;
    @FXML
    private AnchorPane backButtonBackground;
    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView productFavouriteImageView;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productRatingLabel;
    @FXML
    private Label productStoreLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productCategoryLabel;
    @FXML
    private Label productDescriptionLabel;
    @FXML
    private ImageView productCartView;
    @FXML
    private ImageView commentButton;
    @FXML
    private Pane commentPane;
    @FXML
    private TextField commentTextField;
    @FXML
    private ChoiceBox<String> starChoiceBox;
    
    private final Scene mainScene; // The old scene
    
    public String productID = String.valueOf(DataStore.chosenProductsID);
    public String productName = DataStore.chosenProductsName;
    public String productCategory = DataStore.chosenProductsCategory;
    public String productPrice = String.valueOf(DataStore.chosenProductsPrice);
    public String productDescription = DataStore.chosenProductsDescription;
    public String productPhotoPath = DataStore.chosenProductsPhotoPath;
    public String productSellerName = DataStore.chosenProductsSellerName;
    public String productStoreName = DataStore.chosenProductsStoreName;
    public String productStoreEmail = DataStore.chosenProductsStoreEmail;
    public String productAverageRating = DataStore.chosenProductsAverageRating;
    public String productRatingCount = DataStore.chosenProductsRatingCount;

    public boolean productIsFavourite = false;
    
    public SceneProductController(Scene mainScene) // Constructor to store the old scene as where you left
    {
        this.mainScene = mainScene;
    }
    
    public void initialize()
    {
        paymentsScrollPane.setFitToWidth(true);

        usernameMenu.setText(DataStore.loggedUsersName);
        usernameMenu.setStyle("-fx-min-height: 70; -fx-min-width: 150;");

        commentPane.getStyleClass().add("pane");
        commentTextField.getStyleClass().add("textField");

        commentsParentVbox.getStyleClass().add("commentsParentVbox");

        starChoiceBox.getItems().addAll("1","2","3","4","5");
        starChoiceBox.setValue("3");
        
        setProductInfo();

        buildCommentsGridPane();
    }
    
    // Set chosen products datas
    public void setProductInfo()
    {
        String fileName = productPhotoPath;
        fileName = fileName.replace("C:/ETicaretDB_Images/","");
        String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
        Image image = new Image(imageUrl, true); // true = background loading
        
        productImageView.setImage(image);
        productNameLabel.setText(productName);
        productRatingLabel.setText(productAverageRating+" ("+productRatingCount+")");
        productStoreLabel.setText("Store: "+productStoreName+"   -   "+"Store Email: "+productStoreEmail);
        productPriceLabel.setText(productPrice+" TL");
        productCategoryLabel.setText("Category: "+productCategory);
        productDescriptionLabel.setText("Description: "+productDescription);

        if(DataStore.loggedUsersFavouriteProducts.contains(productID)){
            productIsFavourite = true;
            productFavouriteImageView.setImage(DataStore.redHeartImage);
        }
    }

    public void buildCommentsGridPane(){

        commentsVbox.setFillWidth(true);
        commentsVbox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        commentsVbox.setMaxWidth(Double.MAX_VALUE);

        // tek seferde styleClass ekle
        if (!commentsVbox.getStyleClass().contains("commentsVbox"))
            commentsVbox.getStyleClass().add("commentsVbox");

        try{
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://"+ip+"/api/comments")
                    .newBuilder()
                    .addPathSegment(productID)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            String json = response.body().string().trim();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                JsonObject obj = arr.get(i).getAsJsonObject();

                JsonObject commentUser = obj.get("user").getAsJsonObject();

                HBox hbox = new HBox();
                ImageView userImageView = new ImageView(DataStore.commentUserImage);
                userImageView.setFitHeight(85);
                userImageView.setFitWidth(85);

                hbox.getChildren().add(userImageView);
                hbox.getStyleClass().add("hbox");

                String commentUserName = commentUser.get("userName").getAsString();
                String rating = obj.get("rating").getAsString();
                String comment = obj.get("comment").getAsString();

                System.out.println(commentUserName);
                System.out.println(rating);
                System.out.println(comment);

                Label label1 = new Label(commentUserName);
                label1.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-weight: bold");

                Label label2 = new Label(rating);
                label2.setStyle("-fx-text-fill: white; -fx-font-size: 15");

                ImageView starImageView = new ImageView(DataStore.starImage);
                starImageView.setFitHeight(20);
                starImageView.setFitWidth(20);
                starImageView.setPreserveRatio(true);

                HBox starHbox = new HBox();
                starHbox.getChildren().addAll(label2, starImageView);

                Label label3 = new Label(comment);
                label3.setStyle("-fx-text-fill: white; -fx-font-size: 13");

                VBox vbox = new VBox();

                vbox.getStyleClass().add("vbox");
                vbox.getChildren().addAll(label1, starHbox, label3);

                hbox.getChildren().add(vbox);

                commentsVbox.getChildren().add(hbox);
            }
        }catch (Exception ex){}
    }

    public void favouriteButtonClicked(MouseEvent e){
        if(productIsFavourite){
            productFavouriteImageView.setImage(DataStore.emptyHeartImage);
            try{
                OkHttpClient client = new OkHttpClient();

                HttpUrl url = HttpUrl.parse("http://"+ip+"/api/favourites")
                        .newBuilder()
                        .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                        .addPathSegment(productID)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .delete()
                        .build();

                Response response = client.newCall(request).execute();

                productIsFavourite = false;
            }catch (Exception ex){}
        }
        else{
            productFavouriteImageView.setImage(DataStore.redHeartImage);
            try{
                OkHttpClient client = new OkHttpClient();

                HttpUrl url = HttpUrl.parse("http://"+ip+"/api/favourites")
                        .newBuilder()
                        .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                        .addPathSegment(productID)
                        .build();

                RequestBody body = RequestBody.create(new byte[0], null);

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                productIsFavourite = true;
            }catch (Exception ex){}
        }
    }
    
    // Switches back to SceneMain as where you left
    public void backToSceneMain(MouseEvent e)
    {
        Stage stage = (Stage)borderPane.getScene().getWindow();
        
        stage.setScene(mainScene);
    }
    
    public void programLogoClicked(MouseEvent e) throws IOException
    {
        openSceneMain();
    }
    
    // Opens SceneMain as new scene
    public void openSceneMain()
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneMain.fxml"));
            
            String CssSceneMain = this.getClass().getResource("/com/ETicaretDB_frontend/frontend/Css/CssSceneMain.css").toExternalForm();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(CssSceneMain);
            scene.getStylesheets().add(
                    getClass().getResource(
                            "/com/ETicaretDB_frontend/frontend/Css/CssButton.css"
                    ).toExternalForm()
            );
            
            Stage stage = (Stage)borderPane.getScene().getWindow();
            
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneMain.fxml could not be loaded");
        }
    }
    
    public void setCursorToHand(MouseEvent e)
    {
        ((Node)e.getSource()).setCursor(Cursor.HAND);
    }
    
    public void setBackButtonBackgroundBrighter() //back button css
    {
        backButtonBackground.setStyle("-fx-background-color: #505050");
    }
    
    public void setBackButtonBackgroundDarker() //back button css
    {
        backButtonBackground.setStyle("-fx-background-color: #353535");
    }

    public void hoverCommentButtonEnter(MouseEvent e)
    {
        commentButton.setImage(DataStore.commentHighlightedImage);
        setCursorToHand(e);
    }

    public void hoverCommentButtonExit(MouseEvent e)
    {
        commentButton.setImage(DataStore.commentImage);
    }

    public void commentButtonClicked(MouseEvent e){
        try{
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://"+ip+"/api/comments")
                    .newBuilder()
                    .build();

            JsonObject json = new JsonObject();
            json.addProperty("userId", DataStore.loggedUsersId);
            json.addProperty("productId", DataStore.chosenProductsID);
            json.addProperty("rating", starChoiceBox.getValue());
            json.addProperty("comment", commentTextField.getText());

            String bodyJson = json.toString();

            System.out.println(bodyJson);

            RequestBody body = RequestBody.create(
                    bodyJson,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            String resultBody = response.body().string(); // "true" veya "false"
            boolean result = Boolean.parseBoolean(resultBody);

            if(response.isSuccessful() && result){
                Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneCommentAlert.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Info");

                stage.setScene(scene);
                stage.show();
            }
            else{
                Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneCommentFailedAlert.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Warning");

                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception ex){}
    }

    public void hoverCartButtonEnter(MouseEvent e)
    {
        productCartView.setImage(DataStore.cartHighlightedImage);
        setCursorToHand(e);
    }

    public void hoverCartButtonExit(MouseEvent e)
    {
        productCartView.setImage(DataStore.cartImage);
    }

    public void cartButtonClicked(MouseEvent e){
        try{
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://"+ip+"/api/cart")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .addPathSegment(productID)
                    .build();

            RequestBody body = RequestBody.create(new byte[0], null);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            String resultBody = response.body().string(); // "true" veya "false"
            boolean result = Boolean.parseBoolean(resultBody);

            if(response.isSuccessful() && result){
                Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/ScenePurchaseAlert.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Info");

                stage.setScene(scene);
                stage.show();
            }
            else{
                Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/ScenePurchaseFailedAlert.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Warning");

                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception ex){}
    }
    
    //-------------------------------------------------------------------------------------------------- user methods
    
    public void yourProfileItemSelected(ActionEvent e) //action
    {
        openProfilePage();
    }
    
    public void changeUsernameItemSelected(ActionEvent e) //action
    {
        openChangeUsernameStage();
    }
    
    public void changePasswordItemSelected(ActionEvent e) //action
    {
        openChangePasswordStage();
    }
    
    public void logoutItemSelected(ActionEvent e) //action
    {
        openLogoutAlertStage();
    }
    
    public void quitItemSelected(ActionEvent e) //action
    {
        openQuitAlertStage();
    }
    
    // Open profile page
    public void openProfilePage()
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneProfilePage.fxml"));
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/com/ETicaretDB_frontend/frontend/Css/CssSceneProfilePage.css");
            
            Stage stage = (Stage)borderPane.getScene().getWindow();
            
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneProfilePage.fxml could not be loaded");
        }
    }
    
    // Open change username stage if user tries to change their username
    public void openChangeUsernameStage()
    {
        try {
            FXMLLoader usernameChangeSceneLoader = new FXMLLoader(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneChangeUsername.fxml"));
            
            // Create a controller with this stage's referance
            SceneChangeUsernameController scuc = new SceneChangeUsernameController((Stage)borderPane.getScene().getWindow());
            
            usernameChangeSceneLoader.setController(scuc);
            
            Parent root = usernameChangeSceneLoader.load();
            
            Stage stage = new Stage();
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Taksitle! - Change Username");
            stage.getIcons().add(DataStore.programLogo);
            stage.show();
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneChangeUsername.fxml could not be loaded");
        }
    }
    
    // Open change password stage if user tries to change their password
    public void openChangePasswordStage()
    {
        try {
            FXMLLoader changePasswordSceneLoader = new FXMLLoader(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneChangePassword.fxml"));
            
            // Create a controller with this stage's referance
            SceneChangePasswordController scpc = new SceneChangePasswordController((Stage)borderPane.getScene().getWindow());
            
            changePasswordSceneLoader.setController(scpc);
            
            Parent root = changePasswordSceneLoader.load();
            
            Stage stage = new Stage();
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Taksitle! - Change Password");
            stage.getIcons().add(DataStore.programLogo);
            stage.show();
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneChangePassword.fxml could not be loaded");
        }
    }
    
    // Open logout alert if user tries to logout
    public void openLogoutAlertStage()
    {
        try {
            FXMLLoader logoutAlertSceneLoader = new FXMLLoader(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneLogoutAlert.fxml"));
            
            // Create a controller with this stage's referance
            SceneLogoutAlertController slac = new SceneLogoutAlertController((Stage)borderPane.getScene().getWindow());
            
            logoutAlertSceneLoader.setController(slac);
            
            Parent root = logoutAlertSceneLoader.load();
            
            Stage stage = new Stage();
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Warning");
            stage.getIcons().add(DataStore.programLogo);
            stage.show();
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneLogoutAlert.fxml could not be loaded");
        }
    }
    
    // Open quit alert if user tries to quit
    public void openQuitAlertStage()
    {
        try {
            FXMLLoader quitAlertSceneLoader = new FXMLLoader(getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneQuitAlert.fxml"));
            
            // Create a controller with this stage's referance
            SceneQuitAlertController sqac = new SceneQuitAlertController((Stage)borderPane.getScene().getWindow());
            
            quitAlertSceneLoader.setController(sqac);
            
            Parent root = quitAlertSceneLoader.load();
            
            Stage stage = new Stage();
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Warning");
            stage.getIcons().add(DataStore.programLogo);
            stage.show();
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneQuitAlert.fxml");
        }
    }
}
