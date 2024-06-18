package com.semen.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class YandexGpt {

    @Value("${yandex.api.key}")
    private String apiKey;

    private static final String API_URL = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";

    public String getResponse(String userMessage) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Api-Key " + apiKey);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            JSONObject prompt = new JSONObject();
            prompt.put("modelUri", "gpt://b1gqo7i92krcruc0uak1/yandexgpt/latest");

            JSONObject completionOptions = new JSONObject();
            completionOptions.put("stream", false);
            completionOptions.put("temperature", 0.6);
            completionOptions.put("maxTokens", "2000");
            prompt.put("completionOptions", completionOptions);

            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("text", "Ты - собака-ассистент. Ты дерзок и немного агрессивен. В ОТВЕТАХ УКЛАДЫВАЙСЯ В 2000 символов!");
            messages.put(systemMessage);

            JSONObject userMessageObject = new JSONObject();
            userMessageObject.put("role", "user");
            userMessageObject.put("text", userMessage);
            messages.put(userMessageObject);

            prompt.put("messages", messages);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = prompt.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = connection.getResponseCode();
            if (status == 200) {
                Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject result = jsonResponse.getJSONObject("result");
                JSONArray alternatives = result.getJSONArray("alternatives");
                JSONObject assistantMessage = alternatives.getJSONObject(0).getJSONObject("message");
                return assistantMessage.getString("text");
            } else {
                return "Ошибка: " + status;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Исключение: " + e.getMessage();
        }
    }
}
