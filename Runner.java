package com.jdoodle;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
public class Runner {

    @PostConstruct
    public void runTask() {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Send POST to generateWebhook API
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        String requestBody = "{"
            + "\"name\":\"D.Joshitha Sri\","
            + "\"regNo\":\"22BCE9345\","
            + "\"email\":\"djoshithasri@gmail.com\""
            + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            System.out.println("Webhook generation response: " + response.getBody());

            // Parse webhookUrl and accessToken from the response (do manually from output for now)
            String parsedWebhookUrl = "<PASTE_WEBHOOK_URL_HERE>";
            String parsedAccessToken = "<PASTE_ACCESS_TOKEN_HERE>";

            String sqlQuery = "SELECT p.AMOUNT AS SALARY, "
                + "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, "
                + "TIMESTAMPDIFF(YEAR, e.DOB, DATE(p.PAYMENT_TIME)) AS AGE, "
                + "d.DEPARTMENT_NAME "
                + "FROM PAYMENTS p "
                + "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID "
                + "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID "
                + "WHERE DAY(DATE(p.PAYMENT_TIME)) != 1 "
                + "ORDER BY p.AMOUNT DESC "
                + "LIMIT 1";

            // Step 2: Send finalQuery to webhook
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            headers2.setBearerAuth(parsedAccessToken);

            String submitBody = "{\"finalQuery\": \"" + sqlQuery.replace("\"","\\\"") + "\"}";
            HttpEntity<String> entity2 = new HttpEntity<>(submitBody, headers2);

            ResponseEntity<String> submitResponse =
                restTemplate.postForEntity(parsedWebhookUrl, entity2, String.class);

            System.out.println("SQL Submission response: " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
