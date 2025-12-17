
package com.ETicaretDB_frontend.frontend.Java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SceneProfilePageController {

    public static final String ip = DataStore.ip;
    
    @FXML
    private Menu usernameMenu;
    @FXML
    private Label usernameLabel;
    @FXML
    private ScrollPane userScrollPane;

    private HashMap<String, ArrayList<String>> productData = DataStore.productData;
    
    public void initialize()
    {
        usernameMenu.setText(DataStore.loggedUsersName);
        
        usernameMenu.setStyle("-fx-min-height: 70; -fx-min-width: 150;");
        
        usernameLabel.setText(DataStore.loggedUsersName);

        userScrollPane.setFitToWidth(true);

        getUserCart();

        buildUserScrollPane();
    }
    
    public void programLogoClicked(MouseEvent e)
    {
        openSceneMain();
    }
    
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
            
            Stage stage = (Stage)userScrollPane.getScene().getWindow();
            
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneMain.fxml could not be loaded");
        }
    }
    
    public void setCursorToHand(MouseEvent e)
    {
        ((Node)e.getSource()).setCursor(Cursor.HAND);
    }

    public void getUserCart(){
        try {
            OkHttpClient client2 = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://" + ip + "/api/cart")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .build();

            Request request2 = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response2 = client2.newCall(request2).execute();

            if (response2.isSuccessful()) {
                String json = new String(response2.body().bytes(), StandardCharsets.UTF_8).trim();

                JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

                DataStore.loggedUsersCartProducts.clear();
                for (JsonElement elm : arr) {
                    JsonObject obj = elm.getAsJsonObject();

                    DataStore.loggedUsersCartProducts.put(obj.get("productId").getAsString(), obj.get("quantity").getAsString());
                }
            }
        }catch (Exception ex) {}
    }

    public void buildUserScrollPane()
    {
        VBox vboxParent = new VBox(20);
        vboxParent.setMaxWidth(Double.MAX_VALUE);
        vboxParent.getStyleClass().add("vboxParent");
        VBox.setVgrow(vboxParent, Priority.ALWAYS);

        Label labelFav = new Label("My Favourites:");
        labelFav.setStyle("-fx-text-fill: white; -fx-font-size: 30; -fx-font-weight: bold; -fx-padding: 5 0 0 50");

        Label labelCart = new Label("My Cart:");
        labelCart.setStyle("-fx-text-fill: white; -fx-font-size: 30; -fx-font-weight: bold; -fx-padding: 40 0 0 50");

        String cartTotalPrice = null;
        try {
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://" + ip + "/api/cart/total")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            cartTotalPrice = response.body().string();

        }catch (Exception ex) {}

        Label cartTotal = new Label("Cart Total Price: "+cartTotalPrice+" TL");
        cartTotal.setStyle("-fx-text-fill: white; -fx-font-size: 25; -fx-font-weight: bold; -fx-padding: 10 0 0 600");

        int rowFav = 0;
        int colFav = 0;

        int rowCart = 0;
        int colCart = 0;

        GridPane gridPaneFav = new GridPane();
        gridPaneFav.getStyleClass().add("gridpane");
        gridPaneFav.setMaxWidth(Double.MAX_VALUE);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(33.33);
            gridPaneFav.getColumnConstraints().add(c);
        }

        GridPane gridPaneCart = new GridPane();
        gridPaneCart.getStyleClass().add("gridpane");
        gridPaneCart.setMaxWidth(Double.MAX_VALUE);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(33.33);
            gridPaneCart.getColumnConstraints().add(c);
        }

        System.out.println("favourite: "+DataStore.loggedUsersFavouriteProducts);
        System.out.println("cart: "+DataStore.loggedUsersCartProducts);

        for (Map.Entry<String, ArrayList<String>> entry : productData.entrySet()) {  // ===== FAVOURITES
            String productID = entry.getKey();
            System.out.println("entry, id: "+ productID);

            if(!DataStore.loggedUsersFavouriteProducts.contains(productID)){
                continue;
            }
            System.out.println("!continue");

            String productName = entry.getValue().get(0);
            String productCategory = entry.getValue().get(1);
            String productPrice = entry.getValue().get(2);
            String productImagePath = entry.getValue().get(4);
            String productStoreName = entry.getValue().get(6);
            String averageRating = entry.getValue().get(8);
            String ratingCount = entry.getValue().get(9);

            HBox hbox = new HBox();
            hbox.setPrefWidth(Double.MAX_VALUE);
            hbox.setMaxWidth(Double.MAX_VALUE);
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10));
            GridPane.setHgrow(hbox, Priority.ALWAYS);

            Label label1 = new Label(productName);
            label1.setStyle("-fx-text-fill: white; -fx-font-size: 15");
            label1.getStyleClass().add("linkLabel");
            label1.setOnMouseEntered(e -> label1.setCursor(Cursor.HAND));
            label1.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            Label label2 = new Label(productPrice + " TL");
            label2.setStyle("-fx-text-fill: white; -fx-font-size: 13; -fx-font-weight: bold");

            Label label3 = new Label(productCategory);
            label3.setStyle("-fx-text-fill: white; -fx-font-size: 10");

            Label label4 = new Label("Seller: " + productStoreName);
            label4.setStyle("-fx-text-fill: white; -fx-font-size: 10");

            String fileName = productImagePath;
            fileName = fileName.replace("C:/ETicaretDB_Images/","");
            String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
            System.out.println(imageUrl);
            Image image = new Image(imageUrl, true); // true = background loading

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("image-view");
            imageView.setOnMouseEntered(e -> imageView.setCursor(Cursor.HAND));
            imageView.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            hbox.getChildren().add(imageView);

            //======================================================================
            VBox vbox = new VBox();
            vbox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(vbox, Priority.ALWAYS);
            vbox.getStyleClass().add("vbox");
            vbox.getChildren().addAll(label1, label2, label3, label4);

            ImageView starView = new ImageView(DataStore.starImage);

            starView.setFitHeight(18);
            starView.setFitWidth(18);
            starView.setPreserveRatio(true);

            Label label5 = new Label(" " + averageRating + " (" + ratingCount + ") ");
            label5.setStyle("-fx-text-fill: white; -fx-font-size: 13");
            //======================================================================

            HBox ratingHBox = new HBox();
            ratingHBox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ratingHBox, Priority.ALWAYS);

            ratingHBox.getChildren().addAll(starView, label5);

            vbox.getChildren().add(ratingHBox);

            hbox.getChildren().add(vbox);

            if (colFav == 3) {
                colFav = 0;
                rowFav++;
            }
            gridPaneFav.add(hbox, colFav, rowFav);
            colFav++;
        }

        for (Map.Entry<String, ArrayList<String>> entry : productData.entrySet()) {  // ===== CARTS
            String productID = entry.getKey();
            System.out.println("entry, id: "+ productID);

            if(!DataStore.loggedUsersCartProducts.containsKey(productID)){
                continue;
            }
            System.out.println("!continue");

            String productName = entry.getValue().get(0);
            String productCategory = entry.getValue().get(1);
            String productPrice = entry.getValue().get(2);
            String productQuantity = DataStore.loggedUsersCartProducts.get(productID);
            String productImagePath = entry.getValue().get(4);
            String productStoreName = entry.getValue().get(6);
            String averageRating = entry.getValue().get(8);
            String ratingCount = entry.getValue().get(9);

            HBox hbox = new HBox();
            hbox.setPrefWidth(Double.MAX_VALUE);
            hbox.setMaxWidth(Double.MAX_VALUE);
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10));
            GridPane.setHgrow(hbox, Priority.ALWAYS);

            Label label1 = new Label(productName);
            label1.setStyle("-fx-text-fill: white; -fx-font-size: 15");
            label1.getStyleClass().add("linkLabel");
            label1.setOnMouseEntered(e -> label1.setCursor(Cursor.HAND));
            label1.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            Label label2 = new Label(productPrice + " TL x " + productQuantity);
            label2.setStyle("-fx-text-fill: white; -fx-font-size: 13; -fx-font-weight: bold");

            Label label3 = new Label(productCategory);
            label3.setStyle("-fx-text-fill: white; -fx-font-size: 10");

            Label label4 = new Label("Seller: " + productStoreName);
            label4.setStyle("-fx-text-fill: white; -fx-font-size: 10");

            String fileName = productImagePath;
            fileName = fileName.replace("C:/ETicaretDB_Images/","");
            String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
            System.out.println(imageUrl);
            Image image = new Image(imageUrl, true); // true = background loading

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("image-view");
            imageView.setOnMouseEntered(e -> imageView.setCursor(Cursor.HAND));
            imageView.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            hbox.getChildren().add(imageView);

            //======================================================================
            VBox vbox = new VBox();
            vbox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(vbox, Priority.ALWAYS);
            vbox.getStyleClass().add("vbox");
            vbox.getChildren().addAll(label1, label2, label3, label4);

            ImageView starView = new ImageView(DataStore.starImage);

            starView.setFitHeight(18);
            starView.setFitWidth(18);
            starView.setPreserveRatio(true);

            Label label5 = new Label(" " + averageRating + " (" + ratingCount + ") ");
            label5.setStyle("-fx-text-fill: white; -fx-font-size: 13");
            //======================================================================

            HBox ratingHBox = new HBox();
            ratingHBox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ratingHBox, Priority.ALWAYS);

            ratingHBox.getChildren().addAll(starView, label5);

            vbox.getChildren().add(ratingHBox);

            hbox.getChildren().add(vbox);

            ImageView deleteCartView = new ImageView(DataStore.deleteCartImage);
            deleteCartView.setFitHeight(30);
            deleteCartView.setFitWidth(30);
            deleteCartView.setPreserveRatio(true);
            deleteCartView.setOnMouseEntered(e -> hoverCartDeleteEnter(e, deleteCartView));
            deleteCartView.setOnMouseExited(e -> hoverCartDeleteExit(e, deleteCartView));
            deleteCartView.setOnMouseClicked(e -> deleteProductCart(productID));

            hbox.getChildren().add(deleteCartView);

            if (colCart == 3) {
                colCart = 0;
                rowCart++;
            }
            gridPaneCart.add(hbox, colCart, rowCart);
            colCart++;
        }

        vboxParent.getChildren().addAll(labelFav, gridPaneFav, labelCart, gridPaneCart, cartTotal);

        userScrollPane.setContent(vboxParent);
    }

    public void hoverCartDeleteEnter(MouseEvent e, ImageView view){
        view.setImage(DataStore.deleteCartHighlightedImage);
        setCursorToHand(e);
    }

    public void hoverCartDeleteExit(MouseEvent e, ImageView view){
        view.setImage(DataStore.deleteCartImage);
    }

    public void deleteProductCart(String productId){
        try {
            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://" + ip + "/api/cart")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .addPathSegment(productId)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneProfilePage.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/com/ETicaretDB_frontend/frontend/Css/CssSceneProfilePage.css");

            Stage stage = (Stage)userScrollPane.getScene().getWindow();

            stage.setScene(scene);

        }catch (Exception ex) {}
    }

    public void switchToSceneProduct(MouseEvent e, String productID)
    {
        DataStore.chosenProductsID = Integer.parseInt(productID);
        DataStore.chosenProductsName = productData.get(productID).get(0);
        DataStore.chosenProductsCategory = productData.get(productID).get(1);
        DataStore.chosenProductsPrice = Double.parseDouble(productData.get(productID).get(2));
        DataStore.chosenProductsDescription = productData.get(productID).get(3);
        DataStore.chosenProductsPhotoPath = productData.get(productID).get(4);
        DataStore.chosenProductsSellerName = productData.get(productID).get(5);
        DataStore.chosenProductsStoreName = productData.get(productID).get(6);
        DataStore.chosenProductsStoreEmail = productData.get(productID).get(7);
        DataStore.chosenProductsAverageRating = productData.get(productID).get(8);
        DataStore.chosenProductsRatingCount = productData.get(productID).get(9);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneProduct.fxml")
            );

            SceneProductController spc = new SceneProductController(userScrollPane.getScene());
            loader.setController(spc);

            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/com/ETicaretDB_frontend/frontend/Css/CssSceneProduct.css");
            Stage stage = (Stage)userScrollPane.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneProduct.fxml could not be loaded");
            ex.printStackTrace();
        }
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
            
            Stage stage = (Stage)userScrollPane.getScene().getWindow();
            
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
            SceneChangeUsernameController scuc = new SceneChangeUsernameController((Stage)userScrollPane.getScene().getWindow());
            
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
            SceneChangePasswordController scpc = new SceneChangePasswordController((Stage)userScrollPane.getScene().getWindow());
            
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
            SceneLogoutAlertController slac = new SceneLogoutAlertController((Stage)userScrollPane.getScene().getWindow());
            
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
            SceneQuitAlertController sqac = new SceneQuitAlertController((Stage)userScrollPane.getScene().getWindow());
            
            quitAlertSceneLoader.setController(sqac);
            
            Parent root = quitAlertSceneLoader.load();
            
            Stage stage = new Stage();
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Warning");
            stage.getIcons().add(DataStore.programLogo);
            stage.show();
        } catch (IOException ex) {
            System.out.println("com/ETicaretDB_frontend/frontend/Views/SceneQuitAlert.fxml");
        }
    }
}
