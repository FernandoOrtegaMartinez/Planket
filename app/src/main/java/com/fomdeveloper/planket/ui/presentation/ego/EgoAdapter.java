package com.fomdeveloper.planket.ui.presentation.ego;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.Comment;
import com.fomdeveloper.planket.data.model.Ego;
import com.fomdeveloper.planket.data.model.Fave;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileActivity;
import com.fomdeveloper.planket.ui.view.CircleTransformation;
import com.fomdeveloper.planket.ui.view.EndlessAdapter;
import com.fomdeveloper.planket.ui.view.LoaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fernando on 28/06/16.
 */
public class EgoAdapter extends EndlessAdapter<RecyclerView.ViewHolder>{

    private Context context;
    private Picasso picasso;
    private ArrayList<Ego> egoItems;
    private int profilePicSize;

    public EgoAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        this.egoItems = new ArrayList<>();
        this.profilePicSize = context.getResources().getDimensionPixelSize(R.dimen.profile_pic_size_medium);

    }

    @Override
    protected int getDataCount() {
        return egoItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.viewholder_ego,parent,false);
            return new EgoViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_loader_ego, parent, false);
            return new LoaderViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EgoViewHolder){
            Ego ego = egoItems.get(position);
            ((EgoViewHolder)holder).bindEgo(ego);
        }else{
            ((LoaderViewHolder)holder).bindItem(showLoadingFinished);
        }
    }

    class EgoViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.profile_image)
        ImageView profileImage;
        @BindView(R.id.comment)
        TextView commentTextview;
        @BindView(R.id.username)
        TextView usernameTexview;
        View itemView;

        public EgoViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        public void bindEgo(Ego ego){
            if (ego instanceof Comment) {
                Comment comment = (Comment) ego;
                commentTextview.setText(Html.fromHtml(comment.getComment()));
                usernameTexview.setText(comment.getBestName());
                itemView.setTag(comment.getUserId());
            }else if (ego instanceof Fave){
                Fave fave = (Fave) ego;
                usernameTexview.setText(fave.getBestName());
                itemView.setTag(fave.getUserId());
                commentTextview.setVisibility(View.GONE);

            }

            picasso.load(ego.getUserProfilePicUrl())
                    .resize(profilePicSize, profilePicSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .placeholder(R.drawable.profile)
                    .into(profileImage);
        }

        @OnClick(R.id.item)
        public void onEgoClick(View v){
            String userId = v.getTag().toString();
            ProfileActivity.start(context,userId);
        }

    }

    public void addEgo(@NonNull List<? extends Ego> moreEgo){
        egoItems.addAll(moreEgo);
        notifyItemRangeInserted(getLoadingMoreItemPosition(),moreEgo.size());
    }

}
