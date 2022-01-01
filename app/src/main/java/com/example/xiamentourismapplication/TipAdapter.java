package com.example.xiamentourismapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder>{
    Context context;
    Activity activity;
    ArrayList<Tip> tips;

    public TipAdapter(Context context, Activity activity, ArrayList<Tip> tips){
        this.context = context;
        this.activity = activity;
        this.tips = tips;
    }

    @NonNull
    @Override
    public TipAdapter.TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tip_card_layout, parent, false);

        return new TipAdapter.TipViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TipAdapter.TipViewHolder holder, int position) {

        Tip tip = tips.get(position);
        byte[] image = tip.getImage();
        Bitmap tipImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.image.setImageBitmap(tipImage);
        holder.title.setText(tip.getTitle());
        holder.author.setText("by "+tip.getAuthor());
        holder.tag.setText(tip.getType());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public class TipViewHolder extends RecyclerView.ViewHolder{
        LinearLayout card;
        TextView title, author, tag;
        RoundedImageView image;
        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tip_title);
            author = itemView.findViewById(R.id.tip_author);
            image = itemView.findViewById(R.id.tip_image);
            card = itemView.findViewById(R.id.tip_card);
            tag = itemView.findViewById(R.id.tip_tag);

        }
    }
}
