package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        //отправка запроса
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            filter(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void filter(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = EntityUtils.toString(entity);
            List<Cats> cats = objectMapper.readValue(jsonStr, new TypeReference<>() {
            });
            List<Cats> filteredCats = cats.stream()
                    .filter(f -> f.getUpvotes() > 0)
                    .toList();
            System.out.println("Отфильтрованные факты о котах: " + Arrays.toString(filteredCats.toArray()));
        }
    }
}