package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    private TwitterClient mClient;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TwitterClient client) {
        mTweets = tweets;
        mClient = client;
    }
    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDate.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));

        if(tweet.isRetweeted) {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
        } else {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }

        if(tweet.isFavorited) {
            holder.ibFavorite.setImageResource(R.drawable.ic_vector_heart);
        } else {
            holder.ibFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        holder.tvFavorite.setText(Integer.toString(tweet.favoriteCount));
        holder.tvRetweet.setText(Integer.toString(tweet.retweetCount));

        holder.ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tweet.isFavorited) {
                    mClient.unFavoriteTweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);

                    tweet.isFavorited = false;
                    tweet.favoriteCount--;
                    holder.tvFavorite.setText(Integer.toString(tweet.favoriteCount));
                } else {
                    mClient.favoriteTweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibFavorite.setImageResource(R.drawable.ic_vector_heart);

                    tweet.isFavorited = true;
                    tweet.favoriteCount++;
                    holder.tvFavorite.setText(Integer.toString(tweet.favoriteCount));
                }
            }
        });

        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tweet.isRetweeted) {
                    mClient.unretweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);

                    tweet.isRetweeted = false;
                    tweet.retweetCount--;
                    holder.tvRetweet.setText(Integer.toString(tweet.retweetCount));
                } else {
                    mClient.retweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet);

                    tweet.isRetweeted = true;
                    tweet.retweetCount++;
                    holder.tvRetweet.setText(Integer.toString(tweet.retweetCount));
                }
            }
        });

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvDate;

        public ImageButton ibFavorite;
        public ImageButton ibRetweet;
        public ImageButton ibReply;

        public TextView tvFavorite;
        public TextView tvRetweet;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            ibRetweet = itemView.findViewById(R.id.ibRetweet);
            ibReply = itemView.findViewById(R.id.ibReply);

            tvFavorite = itemView.findViewById(R.id.tvFavoriteCount);
            tvRetweet = itemView.findViewById(R.id.tvRetweetCount);
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

}
