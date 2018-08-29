package tk.niush.staticrecyclerview;

import android.graphics.Bitmap;

public class News {
    private int id;
    private Bitmap image;
    private String title;
    private String author;
    private String description;

    public News(int id, Bitmap image, String title, String author, String description) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImage() {
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
}
