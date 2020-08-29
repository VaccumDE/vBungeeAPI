/*
 * Developed by Paul Artjomow
 * Last update 10.06.20, 17:50
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.rest;

import bungee.vaccum.api.vBungeeAPI;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RestAPI {

    public JSONObject getObjectFromWebsite(String url) throws IOException {

        try (InputStream inputStream = new URL(url).openStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String rawJsonText = read(bufferedReader);

            return new JSONObject(rawJsonText);
        }
    }

    public JSONObject postObjectToWebsite(String webURL, String post) throws Exception {
        URL url = new URL(webURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Authorization", vBungeeAPI.getInstance().getConfiguration().getString("api-key"));
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setDoOutput(true);

        try(OutputStream outputStream = httpURLConnection.getOutputStream()) {
            byte[] input = post.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            String rawJsonText = read(bufferedReader);

            return new JSONObject(rawJsonText);
        }
    }

    public String read(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int counter;

        while ((counter = reader.read()) != -1) {
            stringBuilder.append((char) counter);
        }
        return stringBuilder.toString();
    }

}
