package ru.meowo.payload.request;

import lombok.Data;

@Data
public class LikeRequest {
    String idPost;
    String idLike;
}
