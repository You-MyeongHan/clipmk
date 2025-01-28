package com.infra.meta.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class PostIdDto {
    private long id;
    private LocalDateTime wr_date;

    public PostIdDto(long id, LocalDateTime wr_date) {
        this.id = id;
        this.wr_date = wr_date;
    }
}

