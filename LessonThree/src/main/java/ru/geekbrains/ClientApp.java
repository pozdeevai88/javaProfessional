package ru.geekbrains;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientApp implements
        ClientSignInWindow.Callback,
        ClientChatWindow.Callback,
        ChatApiHandler.Callback {

    final ChatApiHandler api;
    final ClientSignInWindow clientSignInWindow;
    final ClientChatWindow clientChatWindow;
    String login;

    ClientApp() {
        api = new ChatApiHandler(this);
        clientSignInWindow = new ClientSignInWindow(this);
        clientChatWindow = new ClientChatWindow(this);
        showSignInWindow();
    }

    public static void main(String[] args) {
        new ClientApp();
    }

    @Override
    public void onLoginClick(String login, String password) {
        this.login = login;
        api.auth(login, password);
    }

    @Override
    public synchronized void onAuth(boolean isSuccess, String serverError) {
        System.out.println("login: " + isSuccess);
        if (isSuccess) {
            hideSignInWindow();
            try {
                readHistory();
            } catch (IOException e) {
                e.printStackTrace();
                onNewMessage("Система: не удалосьзагрузить историю");
            }
            showChatWindow();
            // Открываем окно чата
        } else {
            clientSignInWindow.showError(serverError);
        }
    }

    private void readHistory() throws IOException {
        // Понятно, чо на рабочем столе такие вещи размещать нельзя, но в учебном проекте пусть будет так
        File historyFile = new File(System.getProperty("user.home") + "/Desktop/" + "history_" + login + ".txt");
        if (!historyFile.exists()) {
            historyFile.createNewFile();
        }
        List<String> fileContents = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(historyFile);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String strLine = null;
        while ((strLine = bufferedReader.readLine()) != null) {
            fileContents.add(strLine);
        }
        fileInputStream.close();
        int startPos = fileContents.size() - 100;
        if ((fileContents.size() - 100) < 0) {
            startPos = 0;
        }
        for (int i = startPos; i < fileContents.size(); i++) {
//            onNewMessage(fileContents.get(i));
            synchronized (clientChatWindow) {
                clientChatWindow.onNewMessage(fileContents.get(i));
            }
        }
    }

    @Override
    public void sendMessage(String text) {
        api.sendMessage(text);
    }

    @Override
    public void onNewMessage(String message) {
        File historyFile = new File(System.getProperty("user.home") + "/Desktop/" + "history_" + login + ".txt");

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(historyFile, true))) {
            out.write(("\n"+ message).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (clientChatWindow) {
            clientChatWindow.onNewMessage(message);
        }
    }


    private void showSignInWindow() {
        clientSignInWindow.setVisible(true);
    }

    private void hideSignInWindow() {
        clientSignInWindow.setVisible(false);
    }

    private void showChatWindow() {
        clientChatWindow.setVisible(true);
    }
}
