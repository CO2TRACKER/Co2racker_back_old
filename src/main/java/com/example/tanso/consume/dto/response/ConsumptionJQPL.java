package com.example.tanso.consume.dto.response;

import lombok.*;

public interface ConsumptionJQPL {
    String getCreatedAt();
    String getUserId();
    double getConsumed();
    int getType();
}
