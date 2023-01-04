package it.iacovelli.grocerybe.utils;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoogleCredentialsUtils {

    private final RestTemplate restTemplate;

    private final Log LOGGER = LogFactory.getLog(GoogleCredentialsUtils.class);

    @Value("${grocery-be.external.food-details-integrator-be.base-url}")
    private String foodDetailsIntegratorBaseUrl;

    public String getAccessToken() {
        String accessToken = "";
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            if (credentials != null && credentials.hasRequestMetadata() && credentials.hasRequestMetadataOnly()) {
                AccessToken token = credentials.getAccessToken();
                if (token != null) {
                    accessToken = token.getTokenValue();
                } else {
                    LOGGER.error("Token is null trying with metadata server");
                    accessToken = getAccessTokenFromMetadataServer();
                }
            } else {
                LOGGER.error("Credentials not found trying with metadata server");
                accessToken = getAccessTokenFromMetadataServer();
            }
        } catch (IOException e) {
            LOGGER.error("Error while getting access token", e);
            accessToken = getAccessTokenFromMetadataServer();
        }
        return accessToken;
    }

    private String getAccessTokenFromMetadataServer() {
        String accessToken = "";
        String url = "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity?audience=" + foodDetailsIntegratorBaseUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Metadata-Flavor", "Google");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            accessToken = response.getBody();
            LOGGER.info("Access token retrieved from metadata server");
        } else {
            LOGGER.error("Error while getting access token from metadata server");
        }

        return accessToken;
    }

}
