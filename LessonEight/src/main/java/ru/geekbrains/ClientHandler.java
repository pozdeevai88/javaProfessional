package ru.geekbrains;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

class ClientHandler {
    AuthService authService;
    Server server;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Client client;
    private static final Logger LOGGER = LogManager.getLogger();

    ClientHandler(AuthService authService, Server server, Socket socket) {
        this.authService = authService;
        this.server = server;
        this.socket = socket;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            if (!auth(dataInputStream, dataOutputStream)) {
                // Удаляемся из сервера
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                server.onClientDisconnected(this);
                return;
            }
            server.onNewClient(this);
            messageListener(dataInputStream);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            // Удаляемся из сервера
            try {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            server.onClientDisconnected(this);
            e.printStackTrace();
        }
    }

    void sendMessage(Client client, String text) {
        try {
            dataOutputStream.writeUTF("/nm " + client.name + ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean auth(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, SQLException, ClassNotFoundException {
        // Цикл ожидания авторизации клиентов
        int tryCount = 0;
        int maxTryCount = 5;
        final int TIMEOUT = 120000;
        Timer timerTimeout = new Timer();
        timerTimeout.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    LOGGER.info("таймаут авторизации");
                    dataInputStream.close();
                    dataOutputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, TIMEOUT);
        while (true) {
            // Читаем комманду от клиента
            String newMessage = dataInputStream.readUTF();
            // Разбиваем сообщение на состовляющие комманды
            String[] messageData = newMessage.split("\\s");
            // Проверяем соответсует ли комманда комманде авторизации
            if (messageData.length == 3 && messageData[0].equals("/auth")) {
                tryCount++;
                String login = messageData[1];
                String password = messageData[2];
                // Зарегистрирован ли данных пользователь
                client = authService.auth(login, password);
                if (client != null) {
                    // Если получилось авторизоваться то выходим из цикла
                    LOGGER.info("Клиент " + client.name + " успешно авторизовался");
                    timerTimeout.cancel();
                    dataOutputStream.writeUTF("/auth ok");
                    break;
                } else {
                    LOGGER.warn("Неправильные логин и пароль!");
                    dataOutputStream.writeUTF("Неправильные логин и пароль!");
                }
            } else {
                LOGGER.warn("Ошибка авторизации!");
                dataOutputStream.writeUTF("Ошибка авторизации!");
            }
            if (tryCount == maxTryCount) {
                LOGGER.warn("Первышен лимит попыток!");
                dataOutputStream.writeUTF("Первышен лимит попыток!");
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                return false;
            }
        }
        return true;
    }

    private void messageListener(DataInputStream dataInputStream) throws IOException, ClassNotFoundException, SQLException {
        while (true) {
            String newMessage = dataInputStream.readUTF();
            if (newMessage.equals("/exit")) {
                dataOutputStream.writeUTF("/exit ok");
                LOGGER.info("Клиент " + client.name + " покинул чат");
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } else if (newMessage.startsWith("/w ")) {
                String messageWithoutCommand = newMessage.substring(3);
                int messageIndex = messageWithoutCommand.indexOf(" ");
                String nick = messageWithoutCommand.substring(0, messageIndex);
                String message = messageWithoutCommand.substring(messageIndex);
                dataOutputStream.writeUTF("/w ok");
                server.sendMessageTo(client, nick, message);
            } else if (newMessage.startsWith("/changenickname ")) {
                LOGGER.info("Клиент " + client.name + " запросил смену nickname");
                DBConnection db = DBConnection.getInstance();
                String[] newNick = newMessage.split(" ");
                db.changeNickName(newNick[1], client.login);
                dataOutputStream.writeUTF("/changenickname ok");
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } else {
                dataOutputStream.writeUTF("/b ok");
                server.sendBroadCastMessage(client, newMessage);
            }
        }
    }
}