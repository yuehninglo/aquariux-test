package com.aquariux.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuobiResponse {
    private String status;
    private Long ts;
    private List<HuobiTicker> data;
}
