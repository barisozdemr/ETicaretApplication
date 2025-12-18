
package com.ETicaretDB_frontend.frontend.Java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import okhttp3.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class SceneMainController {

    public static final String ip = DataStore.ip;
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Menu usernameMenu;
    @FXML
    private Menu sortMenu;
    @FXML
    private HBox categoryHBox;
    
    private HashMap<String, ArrayList<String>> productData;

    private HashMap<String, Integer> categoryList = new HashMap<String, Integer>();
    
    private ArrayList<String> sortedID_LowToHigh = new ArrayList<>();
    
    @FXML
    GridPane gridPane = new GridPane(); // GridPane of the products

    //=============================================================================================== Initialize methods
    public void initialize()
    {
        System.out.println("SceneMain initialized.");

        usernameMenu.setText(DataStore.loggedUsersName);
        
        usernameMenu.setStyle("-fx-min-height: 70; -fx-min-width: 150;");
        
        sortMenu.setId("sortMenu");
        
        scrollPane.setStyle("-fx-background-color: #292929;");
        
        gridPane.setPrefWidth(1164);
        
        ColumnConstraints colC = new ColumnConstraints(); //set gridpane's column constraints
        colC.setPercentWidth(50);
        gridPane.getColumnConstraints().add(colC);
        gridPane.getColumnConstraints().add(colC);
        
        RowConstraints row1 = new RowConstraints(); //set gridpane's row constraints
        row1.setMinHeight(250);
        gridPane.getRowConstraints().add(row1);

        try{
            productData = getAllProductsMap(); //-------------------------------- get product data
        }catch(Exception ex){
            System.out.println("productData fail");ex.printStackTrace();}

        DataStore.productData = productData;

        setCategoryHBox();

        displayProductsRandom();
        
        sortProductData();
    }

    public HashMap<String, ArrayList<String>> getAllProductsMap() throws IOException {

        OkHttpClient client2 = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("http://"+ip+"/api/favourites/user")
                .newBuilder()
                .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                .build();

        System.out.println(url);

        Request request2 = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response2 = client2.newCall(request2).execute();

        if (response2.isSuccessful()) {
            String json = new String(response2.body().bytes(), StandardCharsets.UTF_8).trim();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            DataStore.loggedUsersFavouriteProducts.clear();
            for(JsonElement elm : arr){
                JsonObject obj = elm.getAsJsonObject();

                DataStore.loggedUsersFavouriteProducts.add(obj.get("productId").getAsString());
            }
        }

        //============================================================

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://"+ip+"/api/products")
                .get()
                .build();

        System.out.println("http://"+ip+"/api/products");

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }

            System.out.println("A");

            String json = response.body().string().trim();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

            System.out.println("A");

            for (int i = 0; i < arr.size(); i++) {

                JsonObject obj = arr.get(i).getAsJsonObject();

                String id = String.valueOf(obj.get("productID").getAsInt());

                url = HttpUrl.parse("http://"+ip+"/api/comments/rating")
                        .newBuilder()
                        .addPathSegment(id)
                        .build();

                request2 = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                System.out.println("B");

                response2 = client2.newCall(request2).execute();

                System.out.println("C");

                json = new String(response2.body().bytes(), StandardCharsets.UTF_8).trim();

                System.out.println("D");

                JsonObject ratingObj = JsonParser.parseString(json).getAsJsonObject();

                System.out.println("E");

                String averageRating;
                String ratingCount;

                if(ratingObj.get("averageRating").isJsonNull()){
                    averageRating = "0";
                    ratingCount = "0";
                }
                else{
                    averageRating = ratingObj.get("averageRating").getAsString();
                    ratingCount = ratingObj.get("ratingCount").getAsString();
                }

                System.out.println("F");

                // map içine eklenecek liste
                ArrayList<String> list = new ArrayList<>();

                // sıralama
                String productName = obj.get("productName").getAsString(); // -- get(0)
                String category = obj.get("category").getAsString(); // -------- get(1)
                String price = obj.get("price").getAsString(); // -------------- get(2)
                String description = obj.get("description").getAsString(); // -- get(3)
                String photoPath = obj.get("photoPath").getAsString(); // ------ get(4)

                // seller içindeki user→userName
                JsonObject seller = obj.getAsJsonObject("seller");
                String sellerName = seller.getAsJsonObject("user").get("userName").getAsString(); //- get(5)
                String storeName = seller.get("storeName").getAsString(); // ------------------------------------ get(6)
                String storeEmail = seller.get("storeEmail").getAsString(); // ---------------------------------- get(7)

                // sırayla listeye ekleniyor
                list.add(productName); // --- get(0)
                list.add(category); // ------ get(1)
                list.add(price); // --------- get(2)
                list.add(description); // --- get(3)
                list.add(photoPath); // ----- get(4)
                list.add(sellerName); // ---- get(5)
                list.add(storeName); // ----- get(6)
                list.add(storeEmail); // ---- get(7)
                list.add(averageRating); // - get(8)
                list.add(ratingCount); // --- get(9)

                resultMap.put(id, list);

                //set category
                if(!categoryList.containsKey(category)){
                    categoryList.put(category, 1);
                }
                else {
                    categoryList.put(category, categoryList.get(category)+1);
                }
            }

            return resultMap;
        }
    }
    
    // Opens SceneMain as new scene
    public void programLogoClicked(MouseEvent e)
    {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneMain.fxml")
            );

            Parent root = loader.load();

            String CssSceneMain = this.getClass().getResource("/com/ETicaretDB_frontend/frontend/Css/CssSceneMain.css").toExternalForm();

            Scene scene = new Scene(root); //create new stage with main scene roots
            scene.getStylesheets().add(CssSceneMain); //add css contents to the new scene
            scene.getStylesheets().add(
                    getClass().getResource(
                            "/com/ETicaretDB_frontend/frontend/Css/CssButton.css"
                    ).toExternalForm()
            );
            
            Stage stage = (Stage)scrollPane.getScene().getWindow();
            
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneMain.fxml could not be loaded");
        }
    }
    
    public void setCursorToHand(MouseEvent e)
    {
        ((Node)e.getSource()).setCursor(Cursor.HAND);
    }

    public void setCategoryHBox()
    {
        System.out.println(categoryList);

        categoryHBox.getChildren().clear();

        categoryHBox.setSpacing(8);

        Button allButton = new Button("Tümü  "+productData.size());

        allButton.getStyleClass().add("button");

        allButton.setStyle(
                "-fx-padding: 6 10 6 10;"
        );

        allButton.setOnAction(e -> {
            try{
                productData = getAllProductsMap();
                if(sortMenu.getText().equals("Random")){
                    displayProductsRandom();
                }
                else if(sortMenu.getText().equals("Lowest Price First")){
                    displayProductsLowToHigh();
                }
                else if(sortMenu.getText().equals("Highest Price First")){
                    displayProductsHighToLow();
                }
            }catch (Exception ex){}
        });

        categoryHBox.getChildren().add(allButton);

        for (Map.Entry<String, Integer> entry : categoryList.entrySet()) //==== category product counts
        {
            String categoryName = entry.getKey();

            System.out.println(categoryName);

            Button btn = new Button(categoryName+"  "+entry.getValue());

            btn.getStyleClass().add("button");

            btn.setStyle(
                    "-fx-padding: 6 10 6 10;"
            );

            btn.setOnAction(e -> {
                try {
                    productData = getProductDataByCategory(categoryName);
                    if(sortMenu.getText().equals("Random")){
                        displayProductsRandom();
                    }
                    else if(sortMenu.getText().equals("Lowest Price First")){
                        displayProductsLowToHigh();
                    }
                    else if(sortMenu.getText().equals("Highest Price First")){
                        displayProductsHighToLow();
                    }
                }catch (Exception ex){}
            });

            categoryHBox.getChildren().add(btn);
        }
    }

    public HashMap<String, ArrayList<String>> getProductDataByCategory(String category) throws IOException{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://"+ip+"/api/products/category/"+category)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }

            String json = response.body().string().trim();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

            for (int i = 0; i < arr.size(); i++) {

                JsonObject obj = arr.get(i).getAsJsonObject();

                String id = String.valueOf(obj.get("productID").getAsInt());

                OkHttpClient client2 = new OkHttpClient();

                HttpUrl url = HttpUrl.parse("http://"+ip+"/api/comments/rating")
                        .newBuilder()
                        .addPathSegment(id)
                        .build();

                Request request2 = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response2 = client2.newCall(request2).execute();

                json = new String(response2.body().bytes(), StandardCharsets.UTF_8).trim();

                JsonObject ratingObj = JsonParser.parseString(json).getAsJsonObject();

                String averageRating;
                String ratingCount;

                if(ratingObj.get("averageRating").isJsonNull()){
                    averageRating = "0";
                    ratingCount = "0";
                }
                else{
                    averageRating = ratingObj.get("averageRating").getAsString();
                    ratingCount = ratingObj.get("ratingCount").getAsString();
                }

                // map içine eklenecek liste
                ArrayList<String> list = new ArrayList<>();

                // sıralama
                String productName = obj.get("productName").getAsString(); // -- get(0)
                String category2 = obj.get("category").getAsString(); // -------- get(1)
                String price = obj.get("price").getAsString(); // -------------- get(2)
                String description = obj.get("description").getAsString(); // -- get(3)
                String photoPath = obj.get("photoPath").getAsString(); // ------ get(4)

                // seller içindeki user→userName
                JsonObject seller = obj.getAsJsonObject("seller");
                String sellerName = seller.getAsJsonObject("user").get("userName").getAsString(); //- get(5)
                String storeName = seller.get("storeName").getAsString(); // ------------------------------------ get(6)
                String storeEmail = seller.get("storeEmail").getAsString(); // ---------------------------------- get(7)

                // sırayla listeye ekleniyor
                list.add(productName); // -- get(0)
                list.add(category2); // ----- get(1)
                list.add(price); // -------- get(2)
                list.add(description); // -- get(3)
                list.add(photoPath); // ---- get(4)
                list.add(sellerName); // --- get(5)
                list.add(storeName); // ---- get(6)
                list.add(storeEmail); // --- get(7)
                list.add(averageRating); // - get(8)
                list.add(ratingCount); // --- get(9)

                resultMap.put(id, list);
            }

            return resultMap;
        }
    }
    
    //------------------------------------------------------------------------------------------------ user menu methods
    
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
            
            Stage stage = (Stage)scrollPane.getScene().getWindow();
            
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
            SceneChangeUsernameController scuc = new SceneChangeUsernameController((Stage)scrollPane.getScene().getWindow());
            
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
            SceneChangePasswordController scpc = new SceneChangePasswordController((Stage)scrollPane.getScene().getWindow());
            
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
            SceneLogoutAlertController slac = new SceneLogoutAlertController((Stage)scrollPane.getScene().getWindow());
            
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
            SceneQuitAlertController sqac = new SceneQuitAlertController((Stage)scrollPane.getScene().getWindow());
            
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
    
    //-------------------------------------------------------------------------------------------------- sorting methods
    
    public void sortRandomSelected(ActionEvent e) //action
    {
        displayProductsRandom();
    }
    
    public void sortLowToHighSelected(ActionEvent e) //action
    {
        displayProductsLowToHigh();
    }
    
    public void sortHighToLowSelected(ActionEvent e) //action
    {
        displayProductsHighToLow();
    }
    
    public void displayProductsRandom()
    {
        sortMenu.setText("Random");
        buildGridPane(productData);
    }
    
    public void displayProductsLowToHigh()
    {
        sortMenu.setText("Lowest Price First");
        buildGridPane(productData, sortedID_LowToHigh);
    }
    
    public void displayProductsHighToLow()
    {
        sortMenu.setText("Highest Price First");
        buildGridPane(productData, sortedID_LowToHigh, -1);
    }

    // =================================================================================================================
    // =================================================================================================================

    public void addUserFavouriteClicked(String productId, ImageView view){
        try{
            view.setImage(DataStore.redHeartImage);

            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://"+ip+"/api/favourites")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .addPathSegment(productId)
                    .build();

            RequestBody body = RequestBody.create(new byte[0], null);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
        }catch (Exception ex){}
    }

    public void deleteUserFavouriteClicked(String productId, ImageView view){
        try{
            view.setImage(DataStore.emptyHeartImage);

            OkHttpClient client = new OkHttpClient();

            HttpUrl url = HttpUrl.parse("http://"+ip+"/api/favourites")
                    .newBuilder()
                    .addPathSegment(String.valueOf(DataStore.loggedUsersId))
                    .addPathSegment(productId)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
        }catch (Exception ex){}
    }

    // =================================================================================================================
    // =================================================================================================================
    // ==================================================================================   BUILD GRIDPANE   ===========
    // =================================================================================================================
    // =================================================================================================================

    public void buildGridPane(Map<String, ArrayList<String>> productData) {
        gridPane.getChildren().clear();               // önceki içerik varsa temizle
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // tek seferde styleClass ekle
        if (!gridPane.getStyleClass().contains("gridpane"))
            gridPane.getStyleClass().add("gridpane");

        // opsiyonel: iki sütun istiyorsan sabit column constraints ekle
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(cc, cc);

        int row = 0;
        int col = 0;

        for (Map.Entry<String, ArrayList<String>> entry : productData.entrySet()) {
            String productID = entry.getKey();
            String productName = entry.getValue().get(0);
            String productCategory = entry.getValue().get(1);
            String productPrice = entry.getValue().get(2);
            String productImagePath = entry.getValue().get(4);
            String productStoreName = entry.getValue().get(6);
            String averageRating = entry.getValue().get(8);
            String ratingCount = entry.getValue().get(9);

            HBox hbox = new HBox();
            hbox.setMaxWidth(Double.MAX_VALUE);
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10));

            Label label1 = new Label(productName);
            label1.setStyle("-fx-text-fill: white; -fx-font-size: 23");
            label1.getStyleClass().add("linkLabel");
            label1.setOnMouseEntered(e -> label1.setCursor(Cursor.HAND));
            label1.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            Label label2 = new Label(productPrice + " TL");
            label2.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

            Label label3 = new Label(productCategory);
            label3.setStyle("-fx-text-fill: white; -fx-font-size: 13");

            Label label4 = new Label("Seller: " + productStoreName);
            label4.setStyle("-fx-text-fill: white; -fx-font-size: 13");

            String fileName = productImagePath;
            fileName = fileName.replace("C:/ETicaretDB_Images/","");
            String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
            System.out.println(imageUrl);
            Image image = new Image(imageUrl, true); // true = background loading

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(180);
            imageView.setFitWidth(180);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("image-view");
            imageView.setOnMouseEntered(e -> imageView.setCursor(Cursor.HAND));
            imageView.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

            hbox.getChildren().add(imageView);

            //======================================================================
            VBox vbox = new VBox(10);
            vbox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(vbox, Priority.ALWAYS);
            vbox.getStyleClass().add("vbox");
            vbox.getChildren().addAll(label1, label2, label3, label4);

            ImageView starView = new ImageView(DataStore.starImage);

            starView.setFitHeight(30);
            starView.setFitWidth(30);
            starView.setPreserveRatio(true);

            Label label5 = new Label("  " + averageRating + "  (" + ratingCount + ")  ");
            label5.setStyle("-fx-text-fill: white; -fx-font-size: 20");
            //======================================================================

            ImageView emptyHeartView = new ImageView(DataStore.emptyHeartImage);
            ImageView redHeartView = new ImageView(DataStore.redHeartImage);

            emptyHeartView.setFitHeight(30);
            emptyHeartView.setFitWidth(30);
            emptyHeartView.setPreserveRatio(true);
            emptyHeartView.setOnMouseEntered(e -> emptyHeartView.setCursor(Cursor.HAND));
            emptyHeartView.setOnMouseClicked(e -> addUserFavouriteClicked(productID, emptyHeartView));

            redHeartView.setFitHeight(30);
            redHeartView.setFitWidth(30);
            redHeartView.setPreserveRatio(true);
            redHeartView.setOnMouseEntered(e -> redHeartView.setCursor(Cursor.HAND));
            redHeartView.setOnMouseClicked(e -> deleteUserFavouriteClicked(productID, redHeartView));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox ratingHBox = new HBox();
            ratingHBox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ratingHBox, Priority.ALWAYS);

            ratingHBox.getChildren().addAll(starView, label5, spacer);

            if(DataStore.loggedUsersFavouriteProducts.contains(productID)){
                System.out.println("redheart");
                HBox.setMargin(redHeartView, new Insets(0, 20, 0, 0));
                ratingHBox.getChildren().add(redHeartView);
            }
            else{
                System.out.println("emptyheart");
                HBox.setMargin(emptyHeartView, new Insets(0, 20, 0, 0));
                ratingHBox.getChildren().add(emptyHeartView);
            }

            vbox.getChildren().add(ratingHBox);

            hbox.getChildren().add(vbox);

            gridPane.add(hbox, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;

                // her satır için YENİ RowConstraints oluştur, aynı nesneyi iki kere ekleme
                RowConstraints rc = new RowConstraints();
                rc.setMinHeight(230);
                gridPane.getRowConstraints().add(rc);
            }
        }

        scrollPane.setContent(gridPane);
    }
    // =================================================================================================================
    // =================================================================================================================
    // =================================================================================================================


    // Build gridpane as low to high price
    public void buildGridPane(Map<String, ArrayList<String>> productData, ArrayList<String> sortedID_LowToHigh)
    {
        System.out.println("buildGridPane sorted");

        gridPane.getChildren().clear();               // önceki içerik varsa temizle
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // tek seferde styleClass ekle
        if (!gridPane.getStyleClass().contains("gridpane"))
            gridPane.getStyleClass().add("gridpane");

        // opsiyonel: iki sütun istiyorsan sabit column constraints ekle
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(cc, cc);

        int row = 0;
        int col = 0;

        for (String s : sortedID_LowToHigh) {
            if(productData.containsKey(s)){
                String productID = s;
                String productName = productData.get(s).get(0);
                String productCategory = productData.get(s).get(1);
                String productPrice = productData.get(s).get(2);
                String productImagePath = productData.get(s).get(4);
                String productStoreName = productData.get(s).get(6);
                String averageRating = productData.get(s).get(8);
                String ratingCount = productData.get(s).get(9);

                HBox hbox = new HBox();
                hbox.setMaxWidth(Double.MAX_VALUE);
                hbox.getStyleClass().add("hbox");
                hbox.setSpacing(20);
                hbox.setPadding(new Insets(10));

                // label stilleri: yanlışlıkla label2'ye uygulama hatası vardı, burada doğru şekilde ayarlandı
                Label label1 = new Label(productName);
                label1.setStyle("-fx-text-fill: white; -fx-font-size: 23");
                label1.getStyleClass().add("linkLabel");
                label1.setOnMouseEntered(e -> label1.setCursor(Cursor.HAND));
                label1.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

                Label label2 = new Label(productPrice + " TL");
                label2.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

                Label label3 = new Label(productCategory);
                label3.setStyle("-fx-text-fill: white; -fx-font-size: 13");

                Label label4 = new Label("Seller: " + productStoreName);
                label4.setStyle("-fx-text-fill: white; -fx-font-size: 13");

                String fileName = productImagePath;
                fileName = fileName.replace("C:/ETicaretDB_Images/","");
                String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
                Image image = new Image(imageUrl, true); // true = background loading

                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(180);
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);
                imageView.getStyleClass().add("image-view");
                imageView.setOnMouseEntered(e -> imageView.setCursor(Cursor.HAND));
                imageView.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

                hbox.getChildren().add(imageView);

                //======================================================================
                VBox vbox = new VBox(10);
                vbox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(vbox, Priority.ALWAYS);
                vbox.getStyleClass().add("vbox");
                vbox.getChildren().addAll(label1, label2, label3, label4);

                ImageView starView = new ImageView(DataStore.starImage);

                starView.setFitHeight(30);
                starView.setFitWidth(30);
                starView.setPreserveRatio(true);

                Label label5 = new Label("  " + averageRating + "  (" + ratingCount + ")  ");
                label5.setStyle("-fx-text-fill: white; -fx-font-size: 20");
                //======================================================================

                ImageView emptyHeartView = new ImageView(DataStore.emptyHeartImage);
                ImageView redHeartView = new ImageView(DataStore.redHeartImage);

                emptyHeartView.setFitHeight(30);
                emptyHeartView.setFitWidth(30);
                emptyHeartView.setPreserveRatio(true);
                emptyHeartView.setOnMouseEntered(e -> emptyHeartView.setCursor(Cursor.HAND));
                emptyHeartView.setOnMouseClicked(e -> addUserFavouriteClicked(productID, emptyHeartView));

                redHeartView.setFitHeight(30);
                redHeartView.setFitWidth(30);
                redHeartView.setPreserveRatio(true);
                redHeartView.setOnMouseEntered(e -> redHeartView.setCursor(Cursor.HAND));
                redHeartView.setOnMouseClicked(e -> deleteUserFavouriteClicked(productID, redHeartView));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox ratingHBox = new HBox();
                ratingHBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(ratingHBox, Priority.ALWAYS);

                ratingHBox.getChildren().addAll(starView, label5, spacer);

                if(DataStore.loggedUsersFavouriteProducts.contains(productID)){
                    System.out.println("redheart");
                    HBox.setMargin(redHeartView, new Insets(0, 20, 0, 0));
                    ratingHBox.getChildren().add(redHeartView);
                }
                else{
                    System.out.println("emptyheart");
                    HBox.setMargin(emptyHeartView, new Insets(0, 20, 0, 0));
                    ratingHBox.getChildren().add(emptyHeartView);
                }

                vbox.getChildren().add(ratingHBox);

                hbox.getChildren().add(vbox);

                gridPane.add(hbox, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;

                    // her satır için YENİ RowConstraints oluştur, aynı nesneyi iki kere ekleme
                    RowConstraints rc = new RowConstraints();
                    rc.setMinHeight(230);
                    gridPane.getRowConstraints().add(rc);
                }
            }
        }

        scrollPane.setContent(gridPane);
    }
    
    // Build gridpane as high to low price
    public void buildGridPane(Map<String, ArrayList<String>> productData, ArrayList<String> sortedID_LowToHigh, int a)
    {
        System.out.println("buildGridPane sorted");

        gridPane.getChildren().clear();               // önceki içerik varsa temizle
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // tek seferde styleClass ekle
        if (!gridPane.getStyleClass().contains("gridpane"))
            gridPane.getStyleClass().add("gridpane");

        // opsiyonel: iki sütun istiyorsan sabit column constraints ekle
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(cc, cc);

        int row = 0;
        int col = 0;

        for (int i = sortedID_LowToHigh.size()-1 ; i>=0 ; i--) {
            String s = sortedID_LowToHigh.get(i);
            if(productData.containsKey(s)){
                String productID = s;
                String productName = productData.get(s).get(0);
                String productCategory = productData.get(s).get(1);
                String productPrice = productData.get(s).get(2);
                String productImagePath = productData.get(s).get(4);
                String productStoreName = productData.get(s).get(6);
                String averageRating = productData.get(s).get(8);
                String ratingCount = productData.get(s).get(9);

                HBox hbox = new HBox();
                hbox.setMaxWidth(Double.MAX_VALUE);
                hbox.getStyleClass().add("hbox");
                hbox.setSpacing(20);
                hbox.setPadding(new Insets(10));

                // label stilleri: yanlışlıkla label2'ye uygulama hatası vardı, burada doğru şekilde ayarlandı
                Label label1 = new Label(productName);
                label1.setStyle("-fx-text-fill: white; -fx-font-size: 23");
                label1.getStyleClass().add("linkLabel");
                label1.setOnMouseEntered(e -> label1.setCursor(Cursor.HAND));
                label1.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

                Label label2 = new Label(productPrice + " TL");
                label2.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

                Label label3 = new Label(productCategory);
                label3.setStyle("-fx-text-fill: white; -fx-font-size: 13");

                Label label4 = new Label("Seller: " + productStoreName);
                label4.setStyle("-fx-text-fill: white; -fx-font-size: 13");

                String fileName = productImagePath;
                fileName = fileName.replace("C:/ETicaretDB_Images/","");
                String imageUrl = "http://"+ip+"/api/products/image/" + fileName;
                Image image = new Image(imageUrl, true); // true = background loading

                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(180);
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);
                imageView.getStyleClass().add("image-view");
                imageView.setOnMouseEntered(e -> imageView.setCursor(Cursor.HAND));
                imageView.setOnMouseClicked(e -> switchToSceneProduct(e, productID));

                hbox.getChildren().add(imageView);

                //======================================================================
                VBox vbox = new VBox(10);
                vbox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(vbox, Priority.ALWAYS);
                vbox.getStyleClass().add("vbox");
                vbox.getChildren().addAll(label1, label2, label3, label4);

                ImageView starView = new ImageView(DataStore.starImage);

                starView.setFitHeight(30);
                starView.setFitWidth(30);
                starView.setPreserveRatio(true);

                Label label5 = new Label("  " + averageRating + "  (" + ratingCount + ")  ");
                label5.setStyle("-fx-text-fill: white; -fx-font-size: 20");
                //======================================================================

                ImageView emptyHeartView = new ImageView(DataStore.emptyHeartImage);
                ImageView redHeartView = new ImageView(DataStore.redHeartImage);

                emptyHeartView.setFitHeight(30);
                emptyHeartView.setFitWidth(30);
                emptyHeartView.setPreserveRatio(true);
                emptyHeartView.setOnMouseEntered(e -> emptyHeartView.setCursor(Cursor.HAND));
                emptyHeartView.setOnMouseClicked(e -> addUserFavouriteClicked(productID, emptyHeartView));

                redHeartView.setFitHeight(30);
                redHeartView.setFitWidth(30);
                redHeartView.setPreserveRatio(true);
                redHeartView.setOnMouseEntered(e -> redHeartView.setCursor(Cursor.HAND));
                redHeartView.setOnMouseClicked(e -> deleteUserFavouriteClicked(productID, redHeartView));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox ratingHBox = new HBox();
                ratingHBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(ratingHBox, Priority.ALWAYS);

                ratingHBox.getChildren().addAll(starView, label5, spacer);

                if(DataStore.loggedUsersFavouriteProducts.contains(productID)){
                    System.out.println("redheart");
                    HBox.setMargin(redHeartView, new Insets(0, 20, 0, 0));
                    ratingHBox.getChildren().add(redHeartView);
                }
                else{
                    System.out.println("emptyheart");
                    HBox.setMargin(emptyHeartView, new Insets(0, 20, 0, 0));
                    ratingHBox.getChildren().add(emptyHeartView);
                }

                vbox.getChildren().add(ratingHBox);

                hbox.getChildren().add(vbox);

                gridPane.add(hbox, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;

                    // her satır için YENİ RowConstraints oluştur, aynı nesneyi iki kere ekleme
                    RowConstraints rc = new RowConstraints();
                    rc.setMinHeight(230);
                    gridPane.getRowConstraints().add(rc);
                }
            }
        }

        scrollPane.setContent(gridPane);
    }
    
    // Sorts products ID's based on their prices as low to high
    public void sortProductData()
    {
        for(int i=0 ; i<productData.size() ; i++)
        {
            Double lowest = Double.MAX_VALUE;

            for(Map.Entry<String, ArrayList<String>> entry : productData.entrySet())
            {
                if(! sortedID_LowToHigh.contains(entry.getKey()))
                {
                    if(Double.parseDouble(entry.getValue().get(2)) < lowest) //== find lowest price
                    {
                        lowest = Double.parseDouble(entry.getValue().get(2));
                    }
                }
            }
  
            for(String key : productData.keySet())
            {
                if(Double.parseDouble(productData.get(key).get(2)) == lowest)
                {
                    sortedID_LowToHigh.add(key);
                }
            }
        }
    }
    
    //-------------------------------------------------------------------------------------------------- switch scene product
    
    public void switchToSceneProduct(MouseEvent e, String productID)
    {
        System.out.println("sp");
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

        System.out.println("p");
        try {
            System.out.println("p");
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ETicaretDB_frontend/frontend/Views/SceneProduct.fxml")
            );

            SceneProductController spc = new SceneProductController(scrollPane.getScene());
            loader.setController(spc);

            System.out.println("p");
            Parent root = loader.load();
            System.out.println("p");
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/com/ETicaretDB_frontend/frontend/Css/CssSceneProduct.css");
            System.out.println("p");
            Stage stage = (Stage)scrollPane.getScene().getWindow();
            System.out.println("p");
            stage.setScene(scene);
            System.out.println("p");
        } catch (IOException ex) {
            System.out.println("/com/ETicaretDB_frontend/frontend/Views/SceneProduct.fxml could not be loaded");
            ex.printStackTrace();
        }
    }
}
