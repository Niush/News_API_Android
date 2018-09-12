package tk.niush.staticrecyclerview;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class NewsActivity extends AppCompatActivity{

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    String url;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_news);

        Intent i = getIntent();
        url = i.getStringExtra("url");
        title = i.getStringExtra("title");

        setTitle(title);

        webView = (WebView) findViewById(R.id.webview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.canGoBack();
        webView.canGoForward();

        TextView textView = (TextView) findViewById(R.id.loading);
        textView.animate().scaleY(0).setDuration(500).setStartDelay(3000);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }

    @Override
    protected void onPause(){
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }

    public void share(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = url;
        String shareHead = title;
        intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
        intent.putExtra(Intent.EXTRA_TEXT, shareHead + " Link: " + shareBody);
        startActivity(Intent.createChooser(intent, "Share This News :"));
    }
}
