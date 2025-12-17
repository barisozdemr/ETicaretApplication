
package com.ETicaretDB_frontend.frontend.Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class DataStore {

    public static final String ip = "localhost:8080";
    
    // Store the id of clicked product
    public static int chosenProductsID;
    public static String chosenProductsSellerName;
    public static String chosenProductsStoreName;
    public static String chosenProductsStoreEmail;
    public static String chosenProductsName;
    public static String chosenProductsCategory;
    public static Double chosenProductsPrice;
    public static String chosenProductsDescription;
    public static String chosenProductsPhotoPath;
    public static String chosenProductsAverageRating;
    public static String chosenProductsRatingCount;
    
    // Store the user
    public static int loggedUsersId;
    public static String loggedUsersName;
    public static String loggedUsersEmail;

    public static ArrayList<String> loggedUsersFavouriteProducts = new ArrayList<>();
    public static HashMap<String, String> loggedUsersCartProducts = new HashMap<>();

    public static final Image emptyHeartImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/emptyHeart.png");
    public static final Image redHeartImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/redHeart.png");

    public static final Image starImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/star.png");

    public static final Image cartImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/basket.png");
    public static final Image cartHighlightedImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/basketHighlighted.png");

    public static final Image deleteCartImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/trash.png");
    public static final Image deleteCartHighlightedImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/trashHighlighted.png");

    public static final Image commentImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/comment.png");
    public static final Image commentHighlightedImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/commentHighlighted.png");

    public static final Image commentUserImage = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/user3.png");
    
    public static final Image programLogo = new Image("/com/ETicaretDB_frontend/frontend/Images/icons-logos/programLogoClear.png");

    public static HashMap<String, ArrayList<String>> productData = new HashMap<>();
    
    //-------------------------------------------------------------------------------------------------------- Product Methods
}
