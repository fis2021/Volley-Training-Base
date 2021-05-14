package org.example;

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

    // Uploads player data to db
    public static void uploadPlayerData(PlayerData playerData) {
        if(playerData != null)
            try {
                statement.execute(String.format("INSERT INTO playerData VALUES('%s', %d, '%s', %d)",
                        playerData.getOwner_cnp(), playerData.getProgress(), playerData.getCoach(), playerData.getAbsents()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        else
            new Alert(Alert.AlertType.ERROR, "playerData variable cannot be null");
    }

    // Retrieves player data from db
    public static PlayerData getPlayerData(String cnp) {
        try {
            statement.execute(String.format("SELECT * FROM playerData WHERE owner_cnp='%s'", cnp));
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next())
                return new PlayerData(resultSet.getString("owner_cnp"), resultSet.getString("coach"),
                        resultSet.getInt("progress"), resultSet.getInt("absents"));
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Uploads parent data to db
    public static void uploadParentData(ParentData parentData) {
        if(parentData != null)
            try {
                statement.execute(String.format("INSERT INTO parentData VALUES('%s', %d, %d)",
                        parentData.getOwner_cnp(), parentData.getAbsents(), parentData.getProgress()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        else
            new Alert(Alert.AlertType.ERROR, "playerData variable cannot be null");
    }

    public static void recruit(String cnp, String coachName) {
        PlayerData playerData = Database.getPlayerData(cnp);
        if(playerData == null)
            new Alert(Alert.AlertType.ERROR, "Player doesn't exists").showAndWait();
        else {
            if(playerData.getCoach().equals("NONE")) {
                try {
                    statement.execute(String.format("UPDATE playerData SET coach='%s' WHERE owner_cnp='%s'",
                            coachName, cnp));
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            else
                new Alert(Alert.AlertType.ERROR, "Player already has a coach").showAndWait();
        }
    }

    public static void progress(String cnp_player, int val) {
        PlayerData playerData = Database.getPlayerData(cnp_player);

        if(playerData == null)
            new Alert(Alert.AlertType.ERROR, "Player doesn't exists").showAndWait();
        else
            try {
                statement.execute(String.format("UPDATE playerData SET progress=%d WHERE owner_cnp='%s'",
                        val, cnp_player));
                ArrayList<UserModel> userModels = Database.getAllUsers();
                if(userModels != null) {
                    StringBuilder cnp_parent = new StringBuilder();
                    for(var i : userModels)
                        if(i.getCnp_child().equals(cnp_player)) {
                            cnp_parent.append(i.getCnp());
                            break;
                        }

                    if(!cnp_parent.toString().isEmpty())
                        statement.execute(String.format("UPDATE parentData SET progress=%d WHERE owner_cnp='%s'",
                                val, cnp_parent));
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
    }

    public static void absent(String cnp) {
        PlayerData playerData = Database.getPlayerData(cnp);
        if(playerData == null)
            new Alert(Alert.AlertType.ERROR, "Player doesn't exists").showAndWait();
        else
            try {
                statement.execute(String.format("UPDATE playerData SET absents=%d WHERE owner_cnp='%s'",
                        playerData.getAbsents() + 1, cnp));
                ArrayList<UserModel> userModels = Database.getAllUsers();
                if(userModels != null) {
                    StringBuilder cnp_parent = new StringBuilder();
                    for(var i : userModels)
                        if(i.getCnp_child().equals(cnp)) {
                            cnp_parent.append(i.getCnp());
                            break;
                        }

                    if(!cnp_parent.toString().isEmpty()) {
                        ParentData parentData = Database.getParentData(cnp_parent.toString());
                        assert parentData != null;
                        statement.execute(String.format("UPDATE parentData SET absents=%d WHERE owner_cnp='%s'",
                                parentData.getAbsents() + 1, cnp_parent));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }


    // Retrieves parent data from db
    public static ParentData getParentData(String cnp) {
        try {
            statement.execute(String.format("SELECT * FROM parentData WHERE owner_cnp='%s'", cnp));
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next())
                return new ParentData(resultSet.getString("owner_cnp"), resultSet.getInt("absents"),
                        resultSet.getInt("progress"));
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
