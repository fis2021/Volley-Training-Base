package org.example;

import com.sun.source.tree.CatchTree;
import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String path = "jdbc:sqlite:src\\main\\resources\\database.db";
    private static Connection connection;
    private static Statement statement;

    public static void init() {
        try {
            connection = DriverManager.getConnection(path);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection to database has failed");
            e.printStackTrace();
        }
    }
    public static void terminate() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Disconnection to database has failed");
            e.printStackTrace();
        }
    }

    // Tries to upload a user to database
    public static void uploadUser(UserModel user) {
        boolean already_exists = false;
        ArrayList<UserModel> userModels = getAllUsers();
        if(userModels != null)
            for(var i : userModels)
                if(user.equals(i)) {
                    already_exists = true;
                    break;
                }

        if(already_exists)
            new Alert(Alert.AlertType.ERROR, "User already exists").showAndWait();
        else
            try {
                statement.execute(String.format("INSERT INTO user VALUES('%s', '%s', '%s', '%s', '%s', '%s', %d, %d, '%s', '%s', '%s')",
                        user.getFname(), user.getLname(), user.getEmail(), user.getPhone(), user.getCnp(), user.getAccType(),
                        user.getAge(), user.getYoe(), user.getCnp_child(), user.getUsername(), user.getPassword()));
            } catch(SQLException e) {
                e.printStackTrace();
            }
    }

    // Returns all user from database
    public static ArrayList<UserModel> getAllUsers() {
        try {
            ArrayList<UserModel> userModels = new ArrayList<>();
            statement.execute("SELECT * FROM user");
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next())
                userModels.add(0, new UserModel(resultSet.getString("fname"), resultSet.getString("lname"),
                        resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("cnp"),
                        resultSet.getString("accType"), resultSet.getString("cnp_child"), resultSet.getInt("age"),
                        resultSet.getInt("yoe"), resultSet.getString("username"), resultSet.getString("password")));

            return userModels.size() == 0 ? null : userModels;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodePasswordMD5(String password) {
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return generatedPassword;
    }

    public static String getPath() { return path; }
}
