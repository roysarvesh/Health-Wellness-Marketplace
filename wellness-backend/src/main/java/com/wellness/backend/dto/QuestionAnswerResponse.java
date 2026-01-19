package com.wellness.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerResponse {

    private QuestionDTO question;
    private List<AnswerDTO> answers;
}
