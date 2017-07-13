package com.kuyue.wechatpublishimagesdrag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * 图片显示Adapter
 * Created by kuyue on 2017/6/19 下午3:59.
 * 邮箱:595327086@qq.com
 */

public class PostArticleImgAdapter extends RecyclerView.Adapter<PostArticleImgAdapter.MyViewHolder> {

    private List<String> mDatas;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public PostArticleImgAdapter(Context context, List<String> datas) {
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.item_post_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position >= PostImagesActivity.IMAGE_SIZE) {//图片已选完时，隐藏添加按钮
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(mDatas.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sdv);
        }
    }


}
