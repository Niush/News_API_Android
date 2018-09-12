package tk.niush.staticrecyclerview;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;

    List<News> newsList;
    String result = "";

    RelativeLayout loading_screen;
    TextView msg;
    //ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_main);

        newsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        newsList.add(new News(1,R.drawable.ic_launcher_background,"Title One","Aue 1","Lorem ipsum le lorem bla bla lorem ipsum kjasd kajsnd kasd asd ajs dasd asdoa sdaosidniasodna askjda sd as dasjdasjdnasdandandansdkjdanskdj"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"My Title 2","Dev","Lorem Ipsum"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Is This Long Title Lorem De rom","Poka Poka","Lorem ipsum le lorem bla bla lorem ipsum kjasd kajsnd kasd asd ajs dasd asdoa sdaosidniasodna askjda sd as dasjdasjdnasdandandansdkjdanskdj"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"My Title 2","Dev","Lorem Ipsum"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Is This Long Title Lorem De rom","Poka Poka","Lorem ipsum le lorem bla bla lorem ipsum kjasd kajsnd kasd asd ajs dasd asdoa sdaosidniasodna askjda sd as dasjdasjdnasdandandansdkjdanskdj"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"My Title 2","Dev","Lorem Ipsum"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Is This Long Title Lorem De rom","Poka Poka","Lorem ipsum le lorem bla bla lorem ipsum kjasd kajsnd kasd asd ajs dasd asdoa sdaosidniasodna askjda sd as dasjdasjdnasdandandansdkjdanskdj"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"My Title 2","Dev","Lorem Ipsum"));
//        newsList.add(new News(1,R.drawable.ic_launcher_background,"Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Title For Ipsum Is This Long Title Lorem De rom","Poka Poka","Lorem ipsum le lorem bla bla lorem ipsum kjasd kajsnd kasd asd ajs dasd asdoa sdaosidniasodna askjdaa sdaosidniasodna askjdaa sdaosidniasodna askjdaa sdaosidniasodna askjda a sdaosidniasodna askjdaa sdaosidniasodna askjda a sdaosidniasodna askjda  sd as dasjdasjdnasdandandansdkjdanskdj"));

//        adapter = new NewsAdapter(this, newsList);
//        recyclerView.setAdapter(adapter);

        search(recyclerView, "google");
    }

    @Override
    protected void onPause(){
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }

    public class BackgroundTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int currPos = reader.read();

                while(currPos != -1){
                    char currChar = (char)currPos;
                    result+=currChar;
                    currPos = reader.read();
                }

                reader.close();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Unexpected - Opps... My Bad. May be the Input had Errors.";
            } catch (Exception e){
                e.printStackTrace();
                return "Looks Like There is No Internet Connection, I cannot reach the server.";
            }
        }

        /*public class IconDownloader extends AsyncTask<String,Void,Bitmap> {

            private Bitmap myBitmap = null;

            //Photo Loading is Taking A Lot Of Time...ui overload..and stuck

            @Override
            protected Bitmap doInBackground(final String... urls) {
                URL url;
                HttpURLConnection httpURLConnection = null;

                try {
                    url = new URL(urls[0]);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream in = httpURLConnection.getInputStream();

                    myBitmap = BitmapFactory.decodeStream(in);
                    //Image Size//
                    int nh = (int) (myBitmap.getHeight() * (100.0 / myBitmap.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 100, nh, true);
                    return scaled;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_screen = (RelativeLayout) findViewById(R.id.loading_screen);
            msg = (TextView) findViewById(R.id.msg);

            //Toast.makeText(MainActivity.this, "Fetching News, If you see this it is fetching", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dialog.dismiss();
            loading_screen.animate().translationYBy(500).setDuration(500).setStartDelay(1000);

            String status = "";
            String title = "";
            String author = "";
            String description = "";
            String articles = "";
            String image = "";
            String url = "";

            try {
                JSONObject jsonObject = new JSONObject(result);

                status = jsonObject.getString("status");

                articles = jsonObject.getString("articles");

                //Toast.makeText(MainActivity.this, articles, Toast.LENGTH_SHORT).show();
                //Note: Loop and keep in array
                //Show that Array while adding.

                JSONArray arr = new JSONArray(articles);

                ArrayList<News> newsArrayList = new ArrayList<>();
                String iconUrl = "";
                //IconDownloader id = new IconDownloader();
                //Bitmap myBitmap = null;

                for(int i = 0 ; i < 3 ; i++){
                    try {
                        JSONObject jsonObjectArticles = arr.getJSONObject(i);
                        title = jsonObjectArticles.getString("title");
                        author = jsonObjectArticles.getString("author");
                        description = jsonObjectArticles.getString("description");
                        image = jsonObjectArticles.getString("urlToImage");
                        url = jsonObjectArticles.getString("url");

                        if (status.equals("error")) {
                            Toast.makeText(MainActivity.this, "API ERROR", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(MainActivity.this, "Grabbed API JSON, At least", Toast.LENGTH_SHORT).show();

                            iconUrl = image;
                            //NOTE: BUG: ERROR ASYNC FOR IMAGE DOWNLOAD NEEDED
                            //try {
                                //Bitmap myBitmap = id.execute(iconUrl).get();
                                //new IconDownloader().execute(iconUrl).get()
                                newsArrayList.add(new News(i, iconUrl , title, author, description, url));
                            /*} catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }*/
                        }

                        //newsArrayList.add(new News(i, myBitmap, title, author, description));
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "World's Weirdest Error", Toast.LENGTH_SHORT).show();
                    }
                }

                //Add To List pass ArrayList of News
                addToList(newsArrayList);

//                JSONObject jsonObjectArticles = arr.getJSONObject(0);
//                title = jsonObjectArticles.getString("title");
//                author = jsonObjectArticles.getString("author");
//                description = jsonObjectArticles.getString("description");
//                image = jsonObjectArticles.getString("urlToImage");

                //Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, image, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Could Not Fetch", Toast.LENGTH_SHORT).show();
                msg.setText("No Stable Internet");
                loading_screen.setBackgroundColor(getResources().getColor(R.color.danger));
                loading_screen.animate().translationYBy(0).setDuration(500);
                e.printStackTrace();
            }
        }
    }

    public void search(View view, String query){
        View myInput = this.getCurrentFocus();
        if (myInput != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(myInput.getWindowToken(), 0);
        }

        final String apiUrl = "https://newsapi.org/v2/everything?language=en&q="+query+"&sortBy=popularity&apiKey=da79ff526d494e338652f7c7ea0f3a1a";

        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                BackgroundTask bt = new BackgroundTask();
                try {
                    result = bt.execute(apiUrl).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(run,200);
    }

    String[] list = {"apple","nepal","ISS"};
    int curr = 0;

    public void addToList(ArrayList<News> newsArrayList){
        for(int i = 0 ; i < newsArrayList.size() ; i++){
            newsList.add(newsArrayList.get(i));
        }
        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);

        //More News Added here...Array is above///
        if(curr < list.length){
            search(recyclerView, list[curr]);
            curr++;
        }
        //Note:: This this and above comment and code in between
    }

    public void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

//    public void intenter(String url){
//        Intent newsIntent = new Intent(this, NewsActivity.class);
//        newsIntent.putExtra("url",url);
//        this.startActivity(newsIntent);
//    }

    public void intenter(){
        Intent newsIntent = new Intent(this, NewsActivity.class);
        this.startActivity(newsIntent);
    }

//    public void addToList(int id, Bitmap image, String title, String author, String description){
//        newsList.add(new News(1,image,title,author,description));
//        adapter = new NewsAdapter(this, newsList);
//        recyclerView.setAdapter(adapter);
//    }

}
