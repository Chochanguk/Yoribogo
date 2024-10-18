package com.avengers.yoribogo.answer.dto;

import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerDTO {
    private int answerId;
    private String answerContent;
    private Role writerType;
    private LocalDateTime answerCreatedAt;
    private int inquiryId;
    private UserEntity user;

    public AnswerDTO(String answerContent, Role writerType, int inquiryId, UserEntity user) {
        this.answerContent = answerContent;
        this.writerType = writerType;
        this.inquiryId = inquiryId;
        this.user = user;
    }
}
