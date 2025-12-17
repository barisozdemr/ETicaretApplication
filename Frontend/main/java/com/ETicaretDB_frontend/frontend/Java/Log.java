
package com.ETicaretDB_frontend.frontend.Java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.*;

public class Log {
    public static String signUpNotification;
    
    public static String updateUserNotification;

    public static final String ip = DataStore.ip;
    
    // Checks if username and password is correct
    public static boolean trySignIn(String username, String password) throws IOException {
        System.out.println("trySignIn...");
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("http://"+ip+"/api/users/authorize")
                .newBuilder()
                .addPathSegment(username)
                .addPathSegment(password)
                .build();

        System.out.println("URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return false;
        }

        if (response.body() == null) {
            return false;
        }

        String json = new String(response.body().bytes(), StandardCharsets.UTF_8).trim();

        if (json.equals("null")) {
            return false;
        }

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        String userIDFromJson = obj.get("userID").getAsString();
        String usernameFromJson = obj.get("userName").getAsString();
        String emailFromJson = obj.get("email").getAsString();

        DataStore.loggedUsersId = Integer.parseInt(userIDFromJson);
        DataStore.loggedUsersName = usernameFromJson;
        DataStore.loggedUsersEmail = emailFromJson;

        return true;
    }
    
    // Checks if username, password and password again are valid and username is not already in use
    public static boolean trySignUp(String username, String password, String password2, String email) throws IOException {
        if(!validUsernamePasswordControl(username, password, password2)) //--- valid control
        {
            return false;
        }

        OkHttpClient client = new OkHttpClient();

        // JSON body oluştur
        String json = "{"
                + "\"username\":\"" + username + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("http://"+ip+"/api/users")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        // backend true veya false döndürüyor
        String result = response.body().string().trim();

        if (!result.equals("true")) {
            signUpNotification = "This username or email are already in use!";

            return false;
        }
        else{ // sign up successfull
            return true;
        }
    }
    
    // Checks if username, password and password again are valid
    public static boolean validUsernamePasswordControl(String username, String password, String password2)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String specCharacters = ",._-*+/=&%$#";
        
        if(username.isBlank() || password.isBlank() || password2.isBlank())
        {
            signUpNotification = "You can not leave blank lines!";
            return false;
        }
        
        if(!password.equals(password2))
        {
            signUpNotification = "Your passwords don't match!";
            return false;
        }
        
        if(username.length() < 5)
        {
            signUpNotification = "Username can not be shorter than 5 characters!";
            return false;
        }
        
        if(password.length() < 8)
        {
            signUpNotification = "Password can not be shorter than 8 characters!";
            return false;
        }
        
        for(int i=0 ; i<username.length() ; i++)
        {
            if(alphabet.indexOf(username.charAt(i)) == -1 && numbers.indexOf(username.charAt(i)) == -1)
            {
                signUpNotification = "Username can only contain characters of alphabet and numbers!";
                return false;
            }
        }
        
        for(int i=0 ; i<password.length() ; i++)
        {
            if(alphabet.indexOf(password.charAt(i)) == -1 && numbers.indexOf(password.charAt(i)) == -1 && specCharacters.indexOf(password.charAt(i)) == -1)
            {
                signUpNotification = "Your password contains invalid characters!";
                return false;
            }
        }
        
        return true;
    }
    
    //------------------------------------------------------------------------------------ User Information Change Methods
    
    // Checks if username is valid and password is correct
    public static boolean updateUsername(Integer userId, String username, String email, String newPassword, String oldPassword) throws IOException
    {
        System.out.println("updateUser");
        System.out.println("A");
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("http://"+ip+"/api/users/checkPassword")
                .newBuilder()
                .addPathSegment(DataStore.loggedUsersName)
                .addPathSegment(oldPassword)
                .build();

        System.out.println("URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("A");

        if(!response.isSuccessful() || response.body() == null){
            return false;
        }

        System.out.println("A");

        String body = response.body().string(); // "true" veya "false"
        boolean result = Boolean.parseBoolean(body);

        System.out.println("A");
        
        if(result) //---------------------------------------------------------- correct password control
        {
            if(validUsernameControl(username)) //------------------------------ valid username control
            {
                System.out.println("B");
                url = HttpUrl.parse("http://"+ip+"/api/users/checkUsernameInUse")
                        .newBuilder()
                        .addPathSegment(username)
                        .build();

                System.out.println("URL: " + url);

                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                response = client.newCall(request).execute();

                System.out.println("B");

                if(!response.isSuccessful() || response.body() == null){
                    return false;
                }

                body = response.body().string(); // "true" veya "false"
                result = Boolean.parseBoolean(body); // "true"->alreadyInUse, "false"->notInUse(Available)

                System.out.println("B");

                if(!result) //---------------------------------------------- username not already in use control
                {
                    System.out.println("C");
                    url = HttpUrl.parse("http://"+ip+"/api/users/update")
                            .newBuilder()
                            .build();

                    System.out.println("URL: " + url);

                    System.out.println(newPassword);
                    String json = "{"
                            + "\"userID\":\"" + userId + "\","
                            + "\"userName\":\"" + username + "\","
                            + "\"email\":\"" + email + "\","
                            + "\"password\":\"" + newPassword + "\""
                            + "}";

                    System.out.println(json);

                    RequestBody requestBody = RequestBody.create(
                            json,
                            MediaType.parse("application/json")
                    );

                    System.out.println("C");

                    request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute();

                    System.out.println("C");

                    if(!response.isSuccessful() || response.body() == null){
                        return false;
                    }

                    body = response.body().string(); // "true" veya "false"
                    result = Boolean.parseBoolean(body);

                    System.out.println("C");

                    if(!result){
                        System.out.println("false");
                        return false;
                    }
                    System.out.println("true");
                    return true;
                }
                else{
                    updateUserNotification = "This username is already in use!";
                }
            }
        }
        else{
            updateUserNotification = "Your password is incorrect!";
        }

        return false;
    }

    public static boolean updatePassword(Integer userId, String username, String email, String newPassword, String oldPassword) throws IOException
    {
        System.out.println("updateUser");
        System.out.println("A");
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("http://"+ip+"/api/users/checkPassword")
                .newBuilder()
                .addPathSegment(DataStore.loggedUsersName)
                .addPathSegment(oldPassword)
                .build();

        System.out.println("URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("A");

        if(!response.isSuccessful() || response.body() == null){
            return false;
        }

        System.out.println("A");

        String body = response.body().string(); // "true" veya "false"
        boolean result = Boolean.parseBoolean(body);

        System.out.println("A");

        if(result) //---------------------------------------------------------- correct password control
        {
            System.out.println("C");
            url = HttpUrl.parse("http://"+ip+"/api/users/update")
                    .newBuilder()
                    .build();

            System.out.println("URL: " + url);

            System.out.println(newPassword);
            String json = "{"
                    + "\"userID\":\"" + userId + "\","
                    + "\"userName\":\"" + username + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + newPassword + "\""
                    + "}";

            System.out.println(json);

            RequestBody requestBody = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            System.out.println("C");

            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            response = client.newCall(request).execute();

            System.out.println("C");

            if(!response.isSuccessful() || response.body() == null){
                return false;
            }

            body = response.body().string(); // "true" veya "false"
            result = Boolean.parseBoolean(body);

            System.out.println("C");

            if(!result){
                System.out.println("false");
                return false;
            }
            System.out.println("true");
            return true;
        }
        else{
            updateUserNotification = "Your password is incorrect!";
        }

        return false;
    }
    
    public static boolean validUsernameControl(String username)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        
        if(username.isBlank())
        {
            updateUserNotification = "You can not leave blank lines!";
            return false;
        }
        
        if(username.length() < 5)
        {
            updateUserNotification = "Username can not be shorter than 5 characters!";
            return false;
        }
        
        for(int i=0 ; i<username.length() ; i++)
        {
            if(alphabet.indexOf(username.charAt(i)) == -1 && numbers.indexOf(username.charAt(i)) == -1)
            {
                updateUserNotification = "Username can only contain characters of alphabet and numbers!";
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean validPasswordControl(String newPassword, String newPasswordAgain)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String specCharacters = ",._-*+/=&%$#";
        
        if(newPassword.isBlank() || newPasswordAgain.isBlank())
        {
            updateUserNotification = "You can not leave blank lines!";
            return false;
        }
        
        if(!newPassword.equals(newPasswordAgain))
        {
            updateUserNotification = "Your passwords don't match!";
            return false;
        }
        
        if(newPassword.length() < 8)
        {
            updateUserNotification = "Password can not be shorter than 8 characters!";
            return false;
        }
        
        for(int i=0 ; i<newPassword.length() ; i++)
        {
            if(alphabet.indexOf(newPassword.charAt(i)) == -1 && numbers.indexOf(newPassword.charAt(i)) == -1 && specCharacters.indexOf(newPassword.charAt(i)) == -1)
            {
                updateUserNotification = "Your password contains invalid characters!";
                return false;
            }
        }
        
        return true;
    }
}
