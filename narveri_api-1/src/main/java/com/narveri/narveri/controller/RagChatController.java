package com.narveri.narveri.controller;


import com.narveri.narveri.dto.BaseResponseDto;
import com.narveri.narveri.service.RagChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RagChatController {

    private final RagChatService ragChatService;

    @PostMapping("/rag/ask")
    public ResponseEntity<?> askQuestion(
            @RequestParam String question,
            @RequestParam(defaultValue = "3") int topK
    ) {
        try {
            String answer = ragChatService.answerQuestion(question, topK);

            return ResponseEntity.ok(answer);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping ("/rag/embed-and-store")
    public ResponseEntity<BaseResponseDto>  embedAndStoreDocument(@RequestParam Long docID) {
        ragChatService.embedAndStoreDocument(docID);
        return ResponseEntity.ok(BaseResponseDto.builder().message("Döküman embedding ve saklama işlemi başarılı olarak tamamlanmıştır.").build());
    }
}

