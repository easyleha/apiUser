package com.company.dao;

import com.company.model.User;
import com.company.model.UserList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static UserDao instance;
    private static String path = "C:\\Users\\User\\IdeaProjects\\userApiSample\\src\\main\\java\\com\\company\\dao\\text.txt";
    File fileText = new File(path);
    private Gson gson = new Gson();

    private List<User> users = new ArrayList<>();

    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {
    }

    public void addUser(User user) {
        users.add(user);
        fileText();
    }

    public UserList getUsersFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "UTF-8"));
        String line = br.readLine();
        if (line != null) {
            try (final Reader reader = new FileReader(path)) {
                users = gson.fromJson(reader, new TypeToken<List<User>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new UserList(users);
        }
        return getUsers();

    }

    public UserList getUsers() {
        return new UserList(users);
    }

    public boolean remove(String name) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(name) || user.getSecondName().equalsIgnoreCase(name)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    public boolean update(User testUser) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(testUser.getFirstName()) ||
                    user.getSecondName().equalsIgnoreCase(testUser.getSecondName())) {
                user.setAge(testUser.getAge());
                return true;
            }
        }
        return false;
    }

    public void fileText() {
        try (FileWriter writer = new FileWriter(fileText)) {
            writer.append(gson.toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
