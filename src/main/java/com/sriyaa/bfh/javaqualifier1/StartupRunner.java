package com.sriyaa.bfh.javaqualifier1;

import com.sriyaa.bfh.javaqualifier1.config.BfhProps;
import com.sriyaa.bfh.javaqualifier1.model.GenerateWebhookRequest;
import com.sriyaa.bfh.javaqualifier1.model.GenerateWebhookResponse;
import com.sriyaa.bfh.javaqualifier1.model.FinalQueryRequest;
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
    ApplicationRunner runOnStartup(WebClient webClient) {
        return args -> {
            log.info("✅ StartupRunner triggered");

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
                log.error("❌ Failed to get webhook/token!");
                return;
            }

            log.info("✅ Webhook: {}", resp.getWebhook());
            log.info("✅ Token: {}", resp.getAccessToken().substring(0, 10) + "...");

            // 2. Choose question
            int lastTwoDigits = Integer.parseInt(
                    props.getRegNo().replaceAll("\\D", "")
                            .substring(props.getRegNo().length() - 2)
            );
            boolean isOdd = lastTwoDigits % 2 != 0;
            log.info("✅ Chosen Question: {}", isOdd ? "Question 1" : "Question 2");

            // 3. Load SQL
            String sqlPath = isOdd ? "/sql/solution_q1.sql" : "/sql/solution_q2.sql";
            String finalSql;
            try (var is = getClass().getResourceAsStream(sqlPath)) {
                if (is == null) {
                    log.error("❌ SQL file not found: {}", sqlPath);
                    return;
                }
                finalSql = new String(is.readAllBytes());
            }
            log.info("✅ Your SQL query:\n{}", finalSql);

            // 4. Submit SQL
            FinalQueryRequest finalReq = new FinalQueryRequest(finalSql);

            String submissionResponse = webClient.post()
                    .uri(resp.getWebhook())
                    .header("Authorization", resp.getAccessToken())
                    .bodyValue(finalReq)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("✅ Submission Response: {}", submissionResponse);
        };
    }
}