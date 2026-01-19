package com.wellness.backend.ai.engine;

import com.wellness.backend.ai.dto.RecommendationResponse;
import com.wellness.backend.ai.dto.SymptomRequest;

public interface RecommendationEngine {

    RecommendationResponse recommend(SymptomRequest request);
}
