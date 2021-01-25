package ru.geekbrains;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatApiHandler {
    static final ExecutorService chatApiThreadPool = Executors.newCachedThreadPool();

    interface Callback {

        void onAuth(boolean isSuccess, String serverError);

        void onNewMessage(String message);
    }

    Callback callback;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    ChatApiHandler(Callback callback) {
        this.callback = callback;
        try {
            Socket socket = new Socket("localhost", 8081);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            chatApiThreadPool.submit(() -> {
                try {
                    while (true) {
                        String newMessage = dataInputStream.readUTF();
                        if (newMessage.startsWith("/auth")) {
                            System.out.println(newMessage);
                            if (newMessage.equals("/auth ok")) {
                                System.out.println("response success");
                                callback.onAuth(true, null);
                            } else {
                                callback.onAuth(false, newMessage);
                            }
                        } else if (newMessage.startsWith("/nm ") && newMessage.length() > 5) {
                            callback.onAuth(false, newMessage.substring(4));
                            callback.onNewMessage(newMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void auth(String login, String password) {
        chatApiThreadPool.submit(() -> {
            try {
                String command = "/auth " + login + " " + password;
                System.out.println(command);
                dataOutputStream.writeUTF(command);
            } catch (IOException e) {
                System.out.println("response exception");
                callback.onAuth(false, null);
            }
        });
    }

    synchronized void sendMessage(String text) {
        chatApiThreadPool.submit(() -> {
            try {
                dataOutputStream.writeUTF(text);
            } catch (IOException ignored) {
            }
        });
    }
}


