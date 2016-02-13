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
public class ArticlesAdapater extends RecyclerView.Adapter<ArticlesAdapater.ViewHolder> {

    private List<Article> mArticles;
    private Context context;

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

    @Override
    public ArticlesAdapater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticlesAdapater.ViewHolder holder, int position) {

        Article article = mArticles.get(position);

        String thumbnail = article.getThumbnailUrl();
        ImageView iv = holder.image;
        iv.setImageResource(0);

        int size = (int) convertDpToPixel(75, context);
        if (!TextUtils.isEmpty(thumbnail)) {
            Glide.with(context).load(thumbnail)
                    .override(size, size)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_article)
                    .into(iv);
        }
        holder.title.setText(article.getHeadline());
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
