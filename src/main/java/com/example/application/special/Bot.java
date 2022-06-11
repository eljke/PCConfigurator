package com.example.application.special;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Bot {
    private HttpURLConnection con;
    private final String tgToken;

    public Bot(String tgToken){
        this.tgToken = tgToken;
    }

    public void sendMessage(String chatID, String user, String message, String contactEmail) throws IOException {

        String txt = user + " написал:\n" + message + "\nUser ID: " + contactEmail;

        String urlParameters = "chat_id=" + chatID + "&text=" + txt;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {
            System.out.println(tgToken);
            String urlToken = "https://api.telegram.org/bot" + tgToken + "/sendMessage";
            URL url = new URL(urlToken);
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java upread.ru client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

        } finally {
            con.disconnect();
        }
    }
}