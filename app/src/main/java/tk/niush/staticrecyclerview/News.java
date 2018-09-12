package tk.niush.staticrecyclerview;

import android.graphics.Bitmap;

public class News {
    private int id;
    private String image;
    private String title;
    private String author;
    private String description;
    private String url;

    public News(int id, String image, String title, String author, String description, String url) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
