package ru.geekbrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class ClientChatWindow extends JFrame {
    JTextArea chatArea;

    interface Callback {

        void sendMessage(String text);
    }

    ClientChatWindow(Callback callback) {
        this.chatArea = new JTextArea();

        setTitle("Another new chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 300, 400, 600);
        setResizable(false);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatArea.setEnabled(false);
        chatArea.setDisabledTextColor(Color.BLACK);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatPanel.add(chatScroll);

        JPanel msgPanel = new JPanel();
        msgPanel.setPreferredSize(new Dimension(0, 50));
        JTextField msgField = new JTextField();
        msgField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    callback.sendMessage(msgField.getText());
                    msgField.setText("");
                }
            }
        });
        msgField.setPreferredSize(new Dimension(335, 50));
        JButton sendButton = new JButton();
        sendButton.setPreferredSize(new Dimension(50, 50));
        sendButton.setText(">>");
        sendButton.addActionListener(e -> {
            callback.sendMessage(msgField.getText());
            msgField.setText("");
        });
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(msgField, BorderLayout.WEST);
        msgPanel.add(sendButton, BorderLayout.EAST);
        setLayout(new BorderLayout());
        add(chatPanel, BorderLayout.CENTER);
        add(msgPanel, BorderLayout.SOUTH);

    }

    public void onNewMessage(String message) {
        chatArea.append(message + "\n");
    }

}
