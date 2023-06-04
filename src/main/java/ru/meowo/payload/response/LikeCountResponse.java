package ru.meowo.payload.response;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCountResponse {
    private String authorId;
    private int totalLikes;

}
