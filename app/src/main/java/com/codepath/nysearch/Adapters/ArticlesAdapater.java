package com.codepath.nysearch.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.nysearch.Activities.ArticleActivity;
import com.codepath.nysearch.Model.Article;
import com.codepath.nysearch.R;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JaneChung on 2/12/16.
 */
public class ArticlesAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> mArticles;
    private Context context;

    private final int ARTICLE = 0, TITLE = 1;

    public ArticlesAdapater(List<Article> articles) {
        mArticles = articles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.ivImage) ImageView image;
        @Bind(R.id.tvTitle) TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent i = new Intent(itemView.getContext(), ArticleActivity.class);

            Article article = mArticles.get(position);
            i.putExtra("article", Parcels.wrap(article));

            itemView.getContext().startActivity(i);
        }

    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tvStandAloneTitle) TextView title;

        public ViewHolder2(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent i = new Intent(itemView.getContext(), ArticleActivity.class);

            Article article = mArticles.get(position);
            i.putExtra("article", Parcels.wrap(article));

            itemView.getContext().startActivity(i);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Article article = mArticles.get(position);
        String imageUrl = article.getThumbnailUrl();
        if (imageUrl == null || imageUrl == "") {
            return TITLE;
        } else {
            return ARTICLE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ARTICLE:
                View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolder(articleView);
                break;
            case TITLE:
                View titleView = inflater.inflate(R.layout.item_title, parent, false);
                viewHolder = new ViewHolder2(titleView);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolder2(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Article article = mArticles.get(position);

        switch (holder.getItemViewType()) {
            case ARTICLE:
                ViewHolder v1 = (ViewHolder) holder;
                configureViewHolder(v1, article);
                break;
            case TITLE:
                ViewHolder2 v2 = (ViewHolder2) holder;
                configureViewHolder2(v2, article);
                break;
            default:
                ViewHolder2 v = (ViewHolder2) holder;
                configureViewHolder2(v, article);
                break;
        }
    }

    private void configureViewHolder(ViewHolder v1, Article article) {
        String thumbnail = article.getThumbnailUrl();
        ImageView iv = v1.image;
        iv.setImageResource(0);

        int size = (int) convertDpToPixel(75, context);
        if (!TextUtils.isEmpty(thumbnail)) {
            Glide.with(context).load(thumbnail)
                    .override(size, size)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_article)
                    .into(iv);
        }
        v1.title.setText(article.getHeadline());
    }

    private void configureViewHolder2(ViewHolder2 v2, Article article) {
        v2.title.setText(article.getHeadline());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
