package com.ddockterview.ddock_terview_backend.dto.note;

import com.ddockterview.ddock_terview_backend.entity.Note;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteResponseDto {

    private Long noteId;

    @JsonProperty("questionId")
    private String questionId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NoteResponseDto(Note note) {
        this.noteId = note.getNoteId();
        this.userId = note.getUser().getUserId();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();
        this.updatedAt = note.getUpdatedAt();

        // questionId 조립 로직
        if (note.getBaseQuestion() != null) {
            // "B:" + BaseQuestion ID
            this.questionId = "B:" + note.getBaseQuestion().getBqId();
        } else if (note.getQuestionAfter() != null) {
            // "A:" + QuestionAfter ID (After의 A)
            this.questionId = "A:" + note.getQuestionAfter().getInqId();
        }
    }

}
