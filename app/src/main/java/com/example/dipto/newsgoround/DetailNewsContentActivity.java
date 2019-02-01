package com.example.dipto.newsgoround;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DetailNewsContentActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    //
    private String file = "cached_data";
    //
    private ImageView imageView;
    private TextView appBar_tiitle, appBar_subtitle, date, time, title;
    private boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor;

    ProgressBar progressBar;
    private WebView webView;
    private WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news_content);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        webView = findViewById(R.id.webView);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppBar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appBar_tiitle = findViewById(R.id.title_on_appbar);
        appBar_subtitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);

        progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appBar_tiitle.setText(mSource);
        appBar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);

        String author = null;
        if (mAuthor != null || mAuthor != "") {
            mAuthor = " \u2022 " + mAuthor;
        } else {
            author = "";
        }

        time.setText(mSource + author + " \u2022 " + Utils.DateToTimeFormat(mDate));

        initWebView(mUrl);
    }


    public void initWebView(String url) {
        //webView = findViewById(R.id.webView);
        webSettings = webView.getSettings();
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // TODO: cache
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // finish cache
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppBar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && !isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppBar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }


    //_______OnClickOptions Listener


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.view_web) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            startActivity(i);
            return true;
        } else if (id == R.id.share) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plan");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n" + mUrl + "\n" + "Share from the news app" + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with :"));


            } catch (Exception e) {

                Toast.makeText(DetailNewsContentActivity.this, "Sorry...\nCannot be Shared", Toast.LENGTH_SHORT).show();

            }
// tonoy
        } else if (id == R.id.save_for_future) {
            WriteIntoFile(mUrl);
            Toast.makeText(this, "news saved", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.goto_save_news) {


        }

        return super.onOptionsItemSelected(item);
    }

    // tonoy
    public void WriteIntoFile(String mUrl) {
        try {
            FileOutputStream outputStream = openFileOutput(file, Context.MODE_PRIVATE);
            outputStream.write(this.mUrl.getBytes());
            outputStream.close();
            Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String ReadFromFile() {
        String urls = "";
        try {
            FileInputStream inputStream = openFileInput(file);
            int c;

            while ((c = inputStream.read()) != -1) {
                urls += Character.toString((char) c);
            }
            Toast.makeText(getBaseContext(), urls, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

}
