package ru.meowo.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    @Indexed
    private String authorId;
    private String authorName;
    @Indexed
    private Date postDate;
    private String text;
    @Indexed
    private List<String> likes = new ArrayList<>();
    private List<String> complains = new ArrayList<>();

    public Post(String authorId, String authorName, Date postDate, String text) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.postDate = postDate;
        this.text = text;
    }

}
