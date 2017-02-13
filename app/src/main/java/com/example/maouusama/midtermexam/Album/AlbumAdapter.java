package com.example.maouusama.midtermexam.Album;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maouusama.midtermexam.Model.Album;
import com.example.maouusama.midtermexam.Model.Image;
import com.example.maouusama.midtermexam.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maouusama on 2/13/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    private static final String TAG = "AlbumAdapter";
    private Context context;
    private List<Album> mAlbumList;
    private ViewHolder holder;

    public AlbumAdapter(List<Album> mAlbumList) {
        this.mAlbumList = mAlbumList;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        context = parent.getContext();
        final View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.card_layout, null);
        holder = new ViewHolder(itemLayoutView);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Album item = mAlbumList.get(position);
        if (item != null) {
            if (holder.mAlbum != null) {
                holder.mAlbum.setText(item.getName());
            }
            if (holder.mArtist != null) {
                holder.mArtist.setText(item.getArtist());
            }

            ArrayList<Image> imglist = item.getImageList();
            Image image = null;
            for (int i = 0; i < imglist.size(); i++) {
                Image temp = imglist.get(i);
                if (temp.getSize().equals("large")) {
                    image = temp;
                }
            }
            try {
                Picasso.with(context).load(image.getImgUrl()).placeholder(R.drawable.progress_animation)
                        .error(R.mipmap.ic_launcher).into(holder.mAlbumImage);
            } catch (Exception e) {
                holder.mAlbumImage.setImageResource(R.mipmap.ic_launcher);
            }

            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri articleUri = Uri.parse(item.getUrl());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                    context.startActivity(webIntent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumImage;
        TextView mAlbum;
        TextView mArtist;
        CardView mCard;
        public ViewHolder(View itemView) {
            super(itemView);
            mAlbumImage = (ImageView) itemView.findViewById(R.id.ivAlbumImage);
            mAlbum = (TextView) itemView.findViewById(R.id.tvAlbum);
            mArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            mCard = (CardView) itemView.findViewById(R.id.cvCardView);
        }

    }

    public void clear() {
        int size = this.mAlbumList.size();
        this.mAlbumList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Album> itemList) {
        for (Album item : itemList) {
            this.mAlbumList.add(item);
            notifyDataSetChanged();
        }
    }
}
