package com.narveri.narveri.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Chunk {
    private String text;
    private int start;
    private int end;
}
