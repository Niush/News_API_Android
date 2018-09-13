package tk.niush.staticrecyclerview;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(@NonNull final NewsViewHolder newsViewHolder, final int i) {
        final News news = newsList.get(i);
        newsViewHolder.descriptionView.setText(news.getDescription());
        newsViewHolder.titleView.setText(news.getTitle());
        newsViewHolder.authorView.setText(news.getAuthor());
        if(news.getAuthor() == null || news.getAuthor().equalsIgnoreCase("") || news.getAuthor().equalsIgnoreCase("null")){
            newsViewHolder.authorView.setText("Anonymous");
        }
        Picasso.get().load(news.getImage()).placeholder(R.drawable.ic_launcher_background).into(newsViewHolder.imageView);
//        try {
//            newsViewHolder.imageView.setImageBitmap(new IconDownloader().execute(news.getImage()).get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        newsViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent openNews = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(newsList.get(i).getUrl().toString()));
                //mCtx.startActivity(openNews);

                Toast.makeText(mCtx, news.getTitle(), Toast.LENGTH_SHORT).show();
                Intent newsIntent = new Intent(mCtx, NewsActivity.class);
                //newsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(mCtx, R.anim.pull_in_from_right, R.anim.pull_out_to_left);
                newsIntent.putExtra("url", newsList.get(i).getUrl().toString());
                newsIntent.putExtra("title", newsList.get(i).getTitle().toString());
                mCtx.startActivity(newsIntent, options.toBundle());
            }
        });
        //newsViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(news.getImage()));
        //newsViewHolder.imageView.setImageBitmap(getImage());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView authorView;
        TextView descriptionView;

        View view;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.performClick();
                    /*Intent openNews = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.example.com"));
                    mCtx.startActivity(openNews);
                    Toast.makeText(mCtx, "Opening In Browser", Toast.LENGTH_SHORT).show();*/
                }
            });

            imageView = itemView.findViewById(R.id.image);
            titleView = itemView.findViewById(R.id.title);
            authorView = itemView.findViewById(R.id.author);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }
}

//    public class IconDownloader extends AsyncTask<String,Void,Bitmap> {
//
//        private Bitmap myBitmap = null;
//
//        //Photo Loading is Taking A Lot Of Time...ui overload..and stuck
//
//        @Override
//        protected Bitmap doInBackground(final String... urls) {
//            URL url;
//            HttpURLConnection httpURLConnection = null;
//
//            try {
//                url = new URL(urls[0]);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.connect();
//                InputStream in = httpURLConnection.getInputStream();
//
//                myBitmap = BitmapFactory.decodeStream(in);
//                //Image Size//
//                int nh = (int) (myBitmap.getHeight() * (250.0 / myBitmap.getWidth()));
//                Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 250, nh, true);
//                return scaled;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//    }
