package com.codepath.nysearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.nysearch.Model.Article;
import com.codepath.nysearch.R;
import com.codepath.nysearch.View.ArticleArrayAdapter;
import com.codepath.nysearch.View.SettingFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //onclick listener

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //intent to display article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                Article article = articles.get(position);
                i.putExtra("article", article);

                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = getParams(query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articles.clear();
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
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
}
