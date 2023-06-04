package ru.meowo.service.post;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;
import ru.meowo.model.Post;
import ru.meowo.payload.request.UpdateRequest;
import ru.meowo.payload.response.LikeCountResponse;

import java.util.*;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    @Qualifier("dbname")
    String dbname;

    @Autowired
    @Qualifier("mongoClientMap")
    Map<String, MongoClient> mongoClientMap;

    public List<Post> getAllPost() {
        FindIterable<Document> findIterable = mongoClientMap.get("GUEST").getDatabase(dbname).getCollection("posts").find();
        List<Post> posts = new ArrayList<>();
        findIterable.forEach(x -> posts.add(documentToPost(x)));
        return posts;
    }

    public Post selectByID(String id) {
        Document query = new Document();
        query.put("_id", new ObjectId(id));
        return documentToPost(Objects.requireNonNull(mongoClientMap.get("GUEST").getDatabase(dbname).getCollection("posts").find(query).first()));
    }

    public List<Post> selectByName(String name) {
        Document query = new Document();
        query.put("authorName", name);

        FindIterable<Document> findIterable = mongoClientMap.get("GUEST").getDatabase(dbname).getCollection("posts").find(query);
        List<Post> posts = new ArrayList<>();
        findIterable.forEach(x -> posts.add(documentToPost(x)));
        return posts;
    }

    public void insert(Post post) {
        Document document = postToDocument(post);
        mongoClientMap.get("USER").getDatabase(dbname).getCollection("posts").insertOne(document);
    }

    public void updateText(UpdateRequest updateRequest) {
        Post post = selectByID(updateRequest.getId());
        post.setText(updateRequest.getNewText());
        Document update = new Document();
        Document object = postToDocument(post);
        update.put("$set", object);

        BasicDBObject query = new BasicDBObject();
        query.append("_id", new ObjectId(updateRequest.getId()));
        mongoClientMap.get("USER").getDatabase(dbname).getCollection("posts").updateOne(query, update);
    }

    public boolean like(String idPost, String idLike) {
        boolean trigger;
        Post post = selectByID(idPost);
        List<String> likes = post.getLikes();
        if (likes.contains(idLike)) {
            trigger = false;
            likes.remove(idLike);
        } else {
            trigger = true;
            likes.add(idLike);
        }
        post.setLikes(likes);
        Document update = new Document();
        Document object = postToDocument(post);
        update.put("$set", object);

        BasicDBObject query = new BasicDBObject();
        query.append("_id", new ObjectId(idPost));
        mongoClientMap.get("USER").getDatabase(dbname).getCollection("posts").updateOne(query, update);
        return trigger;
    }

    public void delete(String id) {
        Document document = new Document();
        document.put("_id", new ObjectId(id));
        mongoClientMap.get("ADMIN").getDatabase(dbname).getCollection("posts").deleteOne(document);
    }


    // aggregation functions
    public List<LikeCountResponse> mostLikedUsers() {
        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$authorId")
                        .append("totalLikes", new Document("$sum", new Document("$size", "$likes")))),
                new Document("$sort", new Document("totalLikes", -1)),
                new Document("$limit", 10)
        );

        List<Document> result = mongoClientMap.get("ADMIN").getDatabase(dbname).getCollection("posts").aggregate(pipeline).into(new ArrayList<>());

        List<LikeCountResponse> likesCounts = new ArrayList<>();
        for (Document document : result) {
            String authorId = document.getString("_id");
            int totalLikes = document.getInteger("totalLikes");
            LikeCountResponse likesCount = new LikeCountResponse(authorId, totalLikes);
            likesCounts.add(likesCount);
        }
        return likesCounts;
    }

    public List<Post> usersLikes(String id) {
        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("likes", id)),
                new Document("$sort", new Document("postDate", -1))
        );

        List<Document> result = mongoClientMap.get("ADMIN").getDatabase(dbname).getCollection("posts").aggregate(pipeline).into(new ArrayList<>());

        List<Post> posts = new ArrayList<>();
        result.forEach(x -> posts.add(documentToPost(x)));
        return posts;
    }

    public List<Post> topPosts() {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$sort", new Document("likes", -1)));
        pipeline.add(new Document("$limit", 10));

        List<Document> result = mongoClientMap.get("GUEST").getDatabase(dbname).getCollection("posts").aggregate(pipeline).into(new ArrayList<>());

        List<Post> posts = new ArrayList<>();
        result.forEach(x -> posts.add(documentToPost(x)));
        return posts;
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
