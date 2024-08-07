package org.example.serverjava.Services;

import com.deepl.api.Translator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.serverjava.exceptions.SourceLanguageNotFoundException;
import org.example.serverjava.exceptions.TargetLanguageNotFoundException;
import org.example.serverjava.exceptions.TranslationAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Service
public class TranslateService {
    @Autowired
    private ObjectMapper objectMapper;

    private static final int MAX_THREADS = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private final RestTemplate restTemplate = new RestTemplate();

    private Set<String> supportedLanguages = new HashSet<>();

    @Value("${deepl.api.key}")
    private String apiKey;


    private void loadSupportedLanguages() {
        try {
            String url = "https://api-free.deepl.com/v2/languages";
            String response = restTemplate.getForObject(
                    UriComponentsBuilder.fromHttpUrl(url)
                            .queryParam("auth_key", apiKey)
                            .toUriString(),
                    String.class
            );

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Получен пустой ответ от сервера.");
            }

            JsonNode root = objectMapper.readTree(response);
            if (!root.isArray()) {
                throw new RuntimeException("Неправильный формат ответа: ожидается массив.");
            }


            for (JsonNode languageNode : root) {
                String languageCode = languageNode.path("language").asText();
                if (!languageCode.isEmpty()) {
                    supportedLanguages.add(languageCode.toUpperCase());
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка обработки JSON-ответа: " + e.getMessage(), e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Ошибка HTTP запроса: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Неизвестная ошибка: " + e.getMessage(), e);
        }
    }

    public String translate(String sourceLan, String targetLan, String text) {

        loadSupportedLanguages();


        if (!supportedLanguages.contains(sourceLan.toUpperCase())) {
            throw new SourceLanguageNotFoundException();
        } else if (!supportedLanguages.contains(targetLan.toUpperCase())) {
            throw new TargetLanguageNotFoundException();
        }


        String[] words = text.split("\\s+");
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            futures.add(executor.submit(() -> translateWord(sourceLan, targetLan, word)));
        }

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            try {
                translatedText.append(future.get()).append(" ");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return translatedText.toString().trim();
    }

    private String translateWord(String sourceLanguage, String targetLanguage, String word) {
        Translator translator;
        // Выполняем запрос к API
        try {
            String url = "https://api-free.deepl.com/v2/translate";
            String response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(url)
                            .queryParam("auth_key", apiKey)
                            .queryParam("text", word)

                            .queryParam("target_lang", targetLanguage.toUpperCase())
                            .queryParam("save_formatting", true)
                            .toUriString(),
                    null,
                    String.class
            );
            JsonNode root = objectMapper.readTree(response);
            JsonNode translations = root.path("translations");

            if (translations.isArray() && translations.size() > 0) {
                JsonNode translation = translations.get(0);
                JsonNode textNode = translation.path("text");
                if (!textNode.isMissingNode()) {
                    return URLDecoder.decode(textNode.asText(), StandardCharsets.UTF_8);
                }
            }
            return word;


        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SourceLanguageNotFoundException();
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new TranslationAccessException();
            } else {
                System.err.println("HTTP Status Code: " + e.getStatusCode());
                System.err.println("Response Body: " + e.getResponseBodyAsString());
                return word; // Возвращаем исходное слово в случае ошибки
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

