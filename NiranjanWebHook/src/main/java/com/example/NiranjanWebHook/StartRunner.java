package com.example.NiranjanWebHook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class StartRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        RestTemplate rt = new RestTemplate();

        Map<String, String> data = new HashMap<>();
        data.put("name", "Niranjan Naik");
        data.put("regNo", "ADT23SOCB0650");
        data.put("email", "naikniranjan300305@gmail.com");

        Map res = rt.postForObject(
                "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA",
                data,
                Map.class
        );

        System.out.println("Response: " + res);
        String webhook = (String) res.get("webhook");
        String token = (String) res.get("accessToken");

        String finalQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC;";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        rt.postForObject(webhook, request, String.class);
        System.out.println("Submitted successfully");
    }
}
