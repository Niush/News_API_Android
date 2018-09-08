package tk.niush.staticrecyclerview;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.canGoBack();
        webView.canGoForward();

        TextView textView = (TextView) findViewById(R.id.loading);
        textView.animate().scaleX(0).scaleY(0).setDuration(500).setStartDelay(3000);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this, "Share Btn Created", Toast.LENGTH_SHORT).show();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_activity_menu, menu);

        MenuItem share = menu.findItem(R.id.action_share);
        ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), "Share Clicked Once", Toast.LENGTH_SHORT).show();
                openShare();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                openShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openShare(){
        Toast.makeText(getApplicationContext(), "Share Clicked Method Called", Toast.LENGTH_SHORT).show();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, url);
        share.putExtra(Intent.EXTRA_TITLE, title);
        startActivity(share);
    }

    @Override
    protected void onPause(){
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }
}
