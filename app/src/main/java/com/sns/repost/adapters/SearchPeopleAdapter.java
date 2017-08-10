package com.sns.repost.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sns.repost.R;
import com.sns.repost.activities.UserPageActivity;
import com.sns.repost.helpers.customview.CircleTransform;
import com.sns.repost.loader.TagsLoader;
import com.sns.repost.models.User;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class SearchPeopleAdapter extends RecyclerView.Adapter<SearchPeopleAdapter.Holder> {
    private ArrayList<User> users = new ArrayList<>();
    private Context mContext;
    private TagsLoader tagsLoader;

    public SearchPeopleAdapter(ArrayList<User> list) {
        this.users = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_item, parent, false);
        Holder vh = new Holder(v);
        mContext = v.getContext();
        tagsLoader = TagsLoader.getInstance();
        return vh;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (position >= 0 && position < users.size()) {
            final User user = users.get(position);
            if (user != null) {
                if(StringUtils.isNotEmpty(user.getProfilePicture())){
                    Glide.with(mContext).load(user.getProfilePicture())
                            .transform(new CircleTransform(mContext))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(holder.imvAvatar);
                }
                if(StringUtils.isNotEmpty(user.getUsername())){
                    holder.tvUserName.setText(user.getUsername());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent t = new Intent(mContext, UserPageActivity.class);
                        t.putExtra(Consts.USER_ID,user.getId());
                        mContext.startActivity(t);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.imv_follow)
        ImageView imvAvatar;
        @Bind(R.id.tv_userName)
        TextView tvUserName;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
