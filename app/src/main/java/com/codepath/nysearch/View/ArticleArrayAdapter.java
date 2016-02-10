package com.codepath.nysearch.View;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nysearch.Model.Article;
import com.codepath.nysearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JaneChung on 2/9/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder {
        ImageView image;
        TextView title;
    }
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String thumbnail = article.getThumbnailUrl();
        viewHolder.image.setImageResource(0);

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).fit().placeholder(R.drawable.placeholder_article).into(viewHolder.image);
        }
        viewHolder.title.setText(article.getHeadline());

        return convertView;

    }
}
