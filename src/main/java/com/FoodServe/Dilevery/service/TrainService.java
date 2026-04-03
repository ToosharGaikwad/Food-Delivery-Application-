package com.FoodServe.Dilevery.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;

	@Service
    public class TrainService {

        @Value("${rapidapi.url}")
        private String apiUrl;

        @Value("${rapidapi.key}")
        private String apiKey;

        @Value("${rapidapi.host}")
        private String apiHost;

        public String getLiveStation(String fromStationCode, int hours) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiKey);
            headers.set("X-RapidAPI-Host", apiHost);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder
                    .fromUriString(apiUrl)

                    .queryParam("fromStationCode", fromStationCode)
                    .queryParam("hours", hours)
                    .toUriString();

            return new RestTemplate()
                    .exchange(url, HttpMethod.GET, entity, String.class)
                    .getBody();
        }
    }
