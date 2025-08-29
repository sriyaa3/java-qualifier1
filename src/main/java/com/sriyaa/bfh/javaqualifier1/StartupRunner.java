package com.sriyaa.bfh.javaqualifier1;

import com.sriyaa.bfh.javaqualifier1.config.BfhProps;
import com.sriyaa.bfh.javaqualifier1.model.FinalQueryRequest;
import com.sriyaa.bfh.javaqualifier1.model.GenerateWebhookRequest;
import com.sriyaa.bfh.javaqualifier1.model.GenerateWebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final BfhProps props;

    @Bean
    public ApplicationRunner runOnStartup(WebClient webClient) {
        return args -> {
            // 1. Generate webhook & token
            GenerateWebhookRequest req = new GenerateWebhookRequest(
                    props.getName(),
                    props.getRegNo(),
                    props.getEmail()
            );

            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            GenerateWebhookResponse resp = webClient.post()
                    .uri(url)
                    .bodyValue(req)
                    .retrieve()
                    .bodyToMono(GenerateWebhookResponse.class)
                    .block();

            if (resp == null || resp.getWebhook() == null || resp.getAccessToken() == null) {
                log.error("Failed to get webhook/token!");
                return;
            }

            log.info("Webhook: {}", resp.getWebhook());
            log.info("Token: {}", resp.getAccessToken().substring(0, Math.min(10, resp.getAccessToken().length())) + "...");

            // 2. Determine question based on last two digits of registration number
            String digits = props.getRegNo().replaceAll("\\D", "");
            String lastTwoDigitsStr = digits.length() >= 2 ? digits.substring(digits.length() - 2) : digits;
            int lastTwoDigits = Integer.parseInt(lastTwoDigitsStr);
            boolean isOdd = lastTwoDigits % 2 != 0;

            log.info("Chosen Question: {}", isOdd ? "Question 1" : "Question 2");

            // 3. Load SQL from resources
            String sqlPath = isOdd ? "/sql/solution_q1.sql" : "/sql/solution_q2.sql";
            String finalSql;
            try (var is = getClass().getResourceAsStream(sqlPath)) {
                if (is == null) {
                    log.error("SQL file not found: {}", sqlPath);
                    return;
                }
                finalSql = new String(is.readAllBytes());
            }

            log.info("Final SQL:\n{}", finalSql);

            // 4. Submit SQL
            FinalQueryRequest finalReq = new FinalQueryRequest();
            finalReq.setFinalQuery(finalSql); // assuming setter exists

            String submissionResponse = webClient.post()
                    .uri(resp.getWebhook())
                    .header("Authorization", resp.getAccessToken())
                    .bodyValue(finalReq)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Submission Response: {}", submissionResponse);
        };
    }
}