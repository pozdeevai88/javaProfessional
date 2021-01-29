package ru.geekbrains;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Server {

    List<ClientHandler> clients = new ArrayList<>();
    Map<String, List<Message>> chats = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    Server() {
        ExecutorService clientThreadPool = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            LOGGER.info("Сервер запущен на порту 8081");
            AuthService authService = new AuthService();
            // Обработчик клиентов
            while (true) {
                Socket socket = serverSocket.accept();
                clientThreadPool.submit(() -> new ClientHandler(authService, this, socket));
            }
        } catch (IOException e) {
            clientThreadPool.shutdownNow();
            LOGGER.error("Сервер прекратил работу с ошибкой");
            e.printStackTrace();
        }
    }

    synchronized void sendBroadCastMessage(Client sender, String text) {
        for (int i = 0; i < clients.size(); i++) {
            String recipientLogin = clients.get(i).client.login;
            sendMessageTo(sender, recipientLogin, text);
        }
    }

    synchronized void sendMessageTo(Client sender, String recipientLogin, String text) {
        // Получаем логин получателя для поиска
        String senderLogin = sender.login;
        // Генерируем ключ чата
        String key;
        if (senderLogin.compareTo(recipientLogin) > 0) {
            key = senderLogin + recipientLogin;
        } else {
            key = recipientLogin + senderLogin;
        }
        // Проверяем создан ли чат и если нет то создаём
        if (!chats.containsKey(key)) {
            // Создаём список сообщений для чата
            chats.put(key, new ArrayList());
        }
        // Сохраняем сообщение в чат
        chats.get(key).add(new Message(sender, text));
        // Ищем получателя среди клиентов
        ClientHandler recipient = null;
        for (int i = 0; i < clients.size(); i++) {
            ClientHandler client = clients.get(i);
            if (client.client.login.equals(recipientLogin)) {
                recipient = client;
            }
        }
        // Если получатель онлайн то отправляем ему сообщение
        if (recipient != null) {
            recipient.sendMessage(sender, text);
            LOGGER.info("Отправлено сообщение для " + recipientLogin);
        } else {
            LOGGER.warn("Получатель не найден " + recipientLogin);
        }
    }

    synchronized void onNewClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        LOGGER.info(clientHandler.client.name + "Вошел в чат");
        sendBroadCastMessage(clientHandler.client, "Вошел в чат");
    }

    synchronized void onClientDisconnected(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        LOGGER.info(clientHandler.client.name + "Покинул чат");
        sendBroadCastMessage(clientHandler.client, "Покинул чат");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        new Server();

    }
}