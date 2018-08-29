package tk.niush.staticrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mCtx;
    private List<News> newsList;

    public NewsAdapter(Context mCtx, List<News> newsList) {
        this.mCtx = mCtx;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        News news = newsList.get(i);
        newsViewHolder.descriptionView.setText(news.getDescription());
        newsViewHolder.titleView.setText(news.getTitle());
        newsViewHolder.authorView.setText(news.getAuthor());
        newsViewHolder.imageView.setImageBitmap(news.getImage());
        //newsViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(news.getImage()));
        //newsViewHolder.imageView.setImageBitmap(getImage());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView titleView;
        TextView authorView;
        TextView descriptionView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            titleView = itemView.findViewById(R.id.title);
            authorView = itemView.findViewById(R.id.author);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }
//
//    public static class BackgroundTask extends AsyncTask<String,Void,Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            URL url;
//            HttpURLConnection httpURLConnection = null;
//
//            try {
//                url = new URL(urls[0]);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.connect();
//
//                InputStream in = httpURLConnection.getInputStream();
//
//                Bitmap myBitmap = BitmapFactory.decodeStream(in);
//
//                return myBitmap;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }
//
//    public Bitmap getImage(){
//        BackgroundTask bt = new BackgroundTask();
//        try {
//            Bitmap downloadedImage = bt.execute("https://niush.neocities.org/logo.png").get();
//            return downloadedImage;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}


