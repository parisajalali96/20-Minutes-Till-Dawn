package io.github.parisajalali96.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    private static final String FILE_PATH = "users.json";
    private static final Gson gson = new Gson();

    public static List<User> loadUsers() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }

    public static void saveUsers(List<User> users) throws IOException {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        }
    }

    public static void registerUser(User newUser) throws IOException {
        List<User> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }

    public static User findUserByUsername(String username) throws IOException {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean deleteUserByUsername(String username) throws IOException {
        List<User> users = loadUsers();
        boolean removed = users.removeIf(user -> user.getUsername().equals(username));
        if (removed) {
            saveUsers(users);
        }
        return removed;
    }
    public static void updateUser(User updatedUser) throws IOException {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveUsers(users);
    }



}

