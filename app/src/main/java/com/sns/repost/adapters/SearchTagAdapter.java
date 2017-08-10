package com.sns.repost.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sns.repost.R;
import com.sns.repost.activities.TagFeedActivity;
import com.sns.repost.helpers.EventBusUtils;
import com.sns.repost.loader.TagsLoader;
import com.sns.repost.models.Tag;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.Holder> {
    private ArrayList<Tag> tags = new ArrayList<>();
    private Context mContext;
    private TagsLoader tagsLoader;

    public SearchTagAdapter(ArrayList<Tag> list) {
        this.tags = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag_search, parent, false);
        Holder vh = new Holder(v);
        mContext = v.getContext();
        tagsLoader = TagsLoader.getInstance();
        return vh;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (position >= 0 && position < tags.size()) {
            final Tag tag = tags.get(position);
            if (tag != null) {
                if(StringUtils.isNotEmpty(tag.getName())){
                    holder.tvTagName.setText("#"+tag.getName());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent t = new Intent(mContext, TagFeedActivity.class);
                        t.putExtra(Consts.PARAM_TAG_NAME, tag.getName());
                        mContext.startActivity(t);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvTagName)
        TextView tvTagName;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
