package tk.niush.staticrecyclerview;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;

    List<News> newsList;
    String result = "";

    RelativeLayout loading_screen;
    RelativeLayout splash_after;
    TextView msg;
    //ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_main);

        setTitle("Morning E-Paper");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.mattblack));
        }

        splash_after = (RelativeLayout) findViewById(R.id.splash_after);
        splash_after.animate().translationYBy(-2000).setDuration(500).setStartDelay(4000);

        newsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

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

        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);

        String[] list = {"Imagine_Dragons","Windows","google","nepal","ISS"};
        String makeQuery = "";

        //More News Added here...Array is above///
        for(int i = 0 ; i < list.length ; i++){
            makeQuery = makeQuery + list[i];
            if(i == list.length-1) {
                break;
            }
            makeQuery = makeQuery + "%20OR%20";
        }
        search(recyclerView, makeQuery);
    }

    @Override
    protected void onPause(){
        //overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
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
            Toast.makeText(MainActivity.this, "Key Loaded From Shared Preferences", Toast.LENGTH_LONG).show();
            loading_screen.animate().translationYBy(2000).setDuration(500).setStartDelay(1000);

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

                for(int i = 0 ; i < 20 ; i++){
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

        final String apiUrl;
        if(query.equalsIgnoreCase("top")){
            apiUrl = "https://newsapi.org/v2/top-headlines?language=en&apiKey=da79ff526d494e338652f7c7ea0f3a1a";
        }else{
            apiUrl = "https://newsapi.org/v2/everything?q=(" + query + ")&language=en&excludeDomains=lifehacker.com&pageSize=30&sortBy=popularity&apiKey=da79ff526d494e338652f7c7ea0f3a1a";
        }

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

//    String[] list = {"top","apple","nepal","ISS"};
//    int curr = 0;

    public void addToList(ArrayList<News> newsArrayList){
        for(int i = 0 ; i < newsArrayList.size() ; i++){
            newsList.add(newsArrayList.get(i));
        }

        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);

//        //More News Added here...Array is above///
//        if(curr < list.length){
//            search(recyclerView, list[curr]);
//            curr++;
//        }
//        //Note:: This this and above comment and code in between
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

    public void topNews(View view){
        Intent newsIntent = new Intent(this, TopNews.class);
        this.startActivity(newsIntent);
    }

}
