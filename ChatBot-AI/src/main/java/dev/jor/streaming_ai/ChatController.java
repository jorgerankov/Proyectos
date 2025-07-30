package dev.jor.streaming_ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        logger.info("ChatController initialized with Ollama ChatClient");
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam String message){
        logger.info("Received message: {}", message);

        try {
            String userMessage = "Usuario: " + message + "\n";

            logger.info("Sending request to Ollama...");
            String aiResponse = chatClient.prompt()
                            .user(message)
                            .call()
                            .content();

            logger.info("Received response from Ollama: {}", aiResponse);
            String fullResponse = userMessage + "IA: " + aiResponse + "\n\n";
            return fullResponse;

        } catch (Exception e) {
            logger.error("Error processing chat request: ", e);
            return "Usuario: " + message + "\nError: " + e.getMessage() + "\n\n";
        }
    }

    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message) {
        return chatClient.prompt()
                        .user(message)
                        .stream()
                        .content();
    }

}
