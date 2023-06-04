package ru.meowo.service.post;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.meowo.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImp implements PostService{
    @Autowired
    @Qualifier("mongoClientGuest")
    MongoClient guestClient;

    @Autowired
    @Qualifier("mongoClientUser")
    MongoClient userClient;

    @Autowired
    @Qualifier("mongoClientAdmin")
    MongoClient adminClient;


    public List<Post> getAllPost(){
        FindIterable<Document> findIterable = guestClient.getDatabase("meowo").getCollection("posts").find();
        List<Post> posts = new ArrayList<>();
        findIterable.forEach(x -> posts.add(documentToPost(x)));
        return posts;
    }

    public Post selectByID(String id) {
        Document query = new Document();
        query.put("_id", new ObjectId(id));
        return documentToPost(Objects.requireNonNull(guestClient.getDatabase("meowo").getCollection("posts").find(query).first()));
    }

    public List<Post> selectByName(String name) {
        Document query = new Document();
        query.put("authorName", name);

        FindIterable<Document> findIterable = guestClient.getDatabase("meowo").getCollection("posts").find(query);
        List<Post> posts = new ArrayList<>();
        findIterable.forEach(x -> posts.add(documentToPost(x)));
        return posts;
    }

    public void insert(Post post) {
        Document document = postToDocument(post);
        userClient.getDatabase("meowo").getCollection("posts").insertOne(document);
    }


    // util methods
    private Post documentToPost(Document document) {
        Object getDone = document.get("done");
        boolean done = getDone != null && (boolean) getDone;
        return Post.builder()
                .id(document.get("_id").toString())
                .authorId(document.get("authorId").toString())
                .authorName(document.get("authorName").toString())
                .postDate(document.getDate("postDate"))
                .text(document.get("text").toString())
                .likes(document.getList("likes", String.class))
                .complains(document.getList("complains", String.class))
                .build();
    }

    private Document postToDocument(Post post) {
        Document document = new Document();
        if (post.getId() != null) {
            document.put("_id", new ObjectId(post.getId()));
        }

        document.put("authorId", post.getAuthorId());
        document.put("authorName", post.getAuthorName());
        document.put("postDate", post.getPostDate());
        document.put("text", post.getText());
        document.put("likes", post.getLikes());
        document.put("complains", post.getComplains());
         return document;
    }
}
