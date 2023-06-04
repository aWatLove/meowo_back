package ru.meowo.payload.request;

import lombok.Data;

@Data
public class UpdateRequest {
    String id;
    String newText;
}
