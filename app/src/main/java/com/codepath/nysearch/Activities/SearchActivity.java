package com.codepath.nysearch.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.nysearch.Adapters.ArticlesAdapater;
import com.codepath.nysearch.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nysearch.Fragments.SettingFragment;
import com.codepath.nysearch.Model.Article;
import com.codepath.nysearch.R;
import com.codepath.nysearch.View.SpacesItemDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.rvArticles) RecyclerView rvArticles;

    ArrayList<Article> articles;
    ArticlesAdapater adapter;

    RequestParams params;
    String url;
    StaggeredGridLayoutManager layoutManager;

    private SharedPreferences setting;


    public static final String beginDatePrefs = "DATE_PREFS";
    public static final String sortOrder = "SORT_PREFS";
    public static final String artsNewsDesk = "ARTS_NEWS_DESK_PREFS";
    public static final String fashionNewsDesk = "FASHION_NEWs_DESK_PREFS";
    public static final String sportsNewsDesk = "SPORTS_NEWS_DESK_PREFS";
    public static final String prefName = "MY_SHARED_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = getApplicationContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        articles = new ArrayList<>();

        adapter = new ArticlesAdapater(articles);
        rvArticles.setAdapter(adapter);

        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        rvArticles.addItemDecoration(decoration);

        layoutManager = new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(layoutManager);
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreAritcles(page);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                onArticleSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getSupportFragmentManager();
            SettingFragment setting = SettingFragment.newInstance();
            setting.show(fm,"fragment_setting" );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {

        if (isNetworkAvailable() == false) {
            Toast.makeText(this, "Network not available", 2).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        params = getParams(query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    if (articles.size()  > 0 ) {
                        articles.clear();
                        adapter.notifyDataSetChanged();
                    }
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void loadMoreAritcles(int offset) {
        params.put("page", offset);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    int curSize = adapter.getItemCount();
                    adapter.notifyItemRangeChanged(curSize, articles.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public RequestParams getParams(String query) {
        RequestParams params = new RequestParams();
        params.put("api-key", "7244297877ff7ee03e6286c9436d7d58:19:74355132");
        params.put("page", 0);
        params.put("q", query);

        Long beginDate = setting.getLong(beginDatePrefs, 0);

        if (beginDate > 0) {
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(beginDate);
            String year = Integer.toString(cal.get(Calendar.YEAR));
            String month = Integer.toString(cal.get(Calendar.MONTH));
            String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

            params.put("begin_date", year + month + day);
        }

        int order = setting.getInt(sortOrder, 1);

        if (order == 0) {
            params.put("sort", "oldest");
        } else {
            params.put("sort", "newest");
        }

        String newsDesk = "";
        int tracker = 0;

        Boolean arts = setting.getBoolean(artsNewsDesk, false);
        if (arts == true) {
            newsDesk += "\"Arts\"";
            tracker++;
        }

        Boolean fashion = setting.getBoolean(fashionNewsDesk, false);
        if (fashion == true) {
            if (tracker == 1) {
                newsDesk += " ";
            }
            newsDesk += "\"Fashion & Style\"";
            tracker++;
        }

        Boolean sports = setting.getBoolean(sportsNewsDesk, false);
        if (sports == true) {
            if (tracker >= 1) {
                newsDesk += " ";
            }
            newsDesk += "\"Sports\"";
        }

        if (newsDesk.length() > 0) {
            String q = "news_desk:(" + newsDesk + ")";
            params.put("fq", q);
        }

       return params;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
