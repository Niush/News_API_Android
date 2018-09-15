package tk.niush.staticrecyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class TopNews extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;

    List<News> newsList;
    String result = "";

    RelativeLayout loading_screen;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_top_news);

        setTitle("Top Headlines");

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

        search(recyclerView, "top");
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_screen = (RelativeLayout) findViewById(R.id.loading_screen);
            msg = (TextView) findViewById(R.id.msg);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dialog.dismiss();
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

                for(int i = 0 ; i < 10 ; i++){
                    try {
                        JSONObject jsonObjectArticles = arr.getJSONObject(i);
                        title = jsonObjectArticles.getString("title");
                        author = jsonObjectArticles.getString("author");
                        description = jsonObjectArticles.getString("description");
                        image = jsonObjectArticles.getString("urlToImage");
                        url = jsonObjectArticles.getString("url");

                        if (status.equals("error")) {
                            Toast.makeText(TopNews.this, "API ERROR", Toast.LENGTH_SHORT).show();
                        } else {
                            iconUrl = image;
                            //NOTE: BUG: ERROR ASYNC FOR IMAGE DOWNLOAD NEEDED
                            newsArrayList.add(new News(i, iconUrl , title, author, description, url));
                        }

                        //newsArrayList.add(new News(i, myBitmap, title, author, description));
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(TopNews.this, "World's Weirdest Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TopNews.this, "Could Not Fetch", Toast.LENGTH_SHORT).show();
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

    public void addToList(ArrayList<News> newsArrayList){
        for(int i = 0 ; i < newsArrayList.size() ; i++){
            newsList.add(newsArrayList.get(i));
        }

        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);
    }

    public void back(View view){
        this.finish();
    }

}
