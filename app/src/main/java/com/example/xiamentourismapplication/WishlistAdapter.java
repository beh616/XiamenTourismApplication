package com.example.xiamentourismapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> implements BottomSheetDialog.FragmentListener{

    Context context;
    Activity activity;
    ArrayList<Destination> destinations;
    ArrayList<Wish> wishlist;
    WishlistDatabaseHelper wishlistDatabaseHelper;
    BottomSheetDialog bottomSheetDialogFragment;
    String name, address, comment;
    int wish_id;
    Bitmap destinationImage;
    Boolean isView = false;

    public WishlistAdapter(Context context, Activity activity, ArrayList<Wish> wishlist, ArrayList<Destination> destinations){
        this.context = context;
        this.activity = activity;
        this.wishlist = wishlist;
        this.destinations = destinations;
    }
    @NonNull
    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.wishlist_card_layout, parent, false);
        wishlistDatabaseHelper = new WishlistDatabaseHelper(context);


        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.WishlistViewHolder holder, int position) {
        Wish wish = wishlist.get(holder.getAdapterPosition());

        int destination_id = wish.getDestination_id();
        Destination destination = destinations.get(position);
//        convert blob to bitmap
        byte[] image = destination.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.image.setImageBitmap(bitmap);
        holder.name.setText(destination.getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, DestinationDescription.newInstance(destinations.get(holder.getAdapterPosition()))).addToBackStack(null).commit();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                get comment from database
                wish_id = wishlist.get(holder.getAdapterPosition()).getWish_id();
                comment = wishlistDatabaseHelper.viewComment(wish_id);
                bottomSheetDialogFragment = BottomSheetDialog.newInstance(WishlistAdapter.this::getView);
                isView = true;
                //              get destination image, name and address
                name = destinations.get(holder.getAdapterPosition()).getName();
                address = destinations.get(holder.getAdapterPosition()).getAddress();
                byte[] image = destinations.get(holder.getAdapterPosition()).getImage();
                destinationImage = BitmapFactory.decodeByteArray(image, 0, image.length);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                bottomSheetDialogFragment.show(activity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                get comment from database
                wish_id = wishlist.get(holder.getAdapterPosition()).getWish_id();
                comment = wishlistDatabaseHelper.viewComment(wish_id);
                bottomSheetDialogFragment = BottomSheetDialog.newInstance(WishlistAdapter.this::getView);
                isView = false;
//              get destination image, name and address
                name = destinations.get(holder.getAdapterPosition()).getName();
                address = destinations.get(holder.getAdapterPosition()).getAddress();
                byte[] image = destinations.get(holder.getAdapterPosition()).getImage();
                destinationImage = BitmapFactory.decodeByteArray(image, 0, image.length);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                bottomSheetDialogFragment.show(activity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wish_id = wishlist.get(holder.getAdapterPosition()).getWish_id();
                boolean result = wishlistDatabaseHelper.removeListById(wish_id);
                if (result){
                    name = destinations.get(holder.getAdapterPosition()).getName();
                    wishlist.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemChanged(holder.getAdapterPosition(), getItemCount());
                    Toast.makeText(context, name + " has been removed", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    @Override
    public void getView(View view) {
        ((RoundedImageView)view.findViewById(R.id.destinationImage)).setImageBitmap(destinationImage);
        ((TextView)view.findViewById(R.id.tv_destinationName)).setText(name);
        ((TextView)view.findViewById(R.id.tv_destinationAddress)).setText(address);
        ((EditText)view.findViewById(R.id.et_comment)).setText(comment);
        if(isView)
        {
            ((TextView)view.findViewById(R.id.tv_bottomTitle)).setText("View Comment");
            ((EditText)view.findViewById(R.id.et_comment)).setFocusable(false);
            ((EditText)view.findViewById(R.id.et_comment)).setVerticalScrollBarEnabled(true);
            ((Button)view.findViewById(R.id.btn_add)).setVisibility(View.INVISIBLE);
        }
        else
        {
            ((TextView)view.findViewById(R.id.tv_bottomTitle)).setText("Edit Comment");
            ((Button)view.findViewById(R.id.btn_add)).setText("Edit");
            ((Button)view.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    String comment1 = ((EditText)view.findViewById(R.id.et_comment)).getText().toString();
                    if(comment1.equals(""))
                    {
                        Toast.makeText(context, "The input cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int result = wishlistDatabaseHelper.editComment(wish_id, comment1);
                        if(result > 0)
                        {
                            bottomSheetDialogFragment.dismiss();
                            Toast.makeText(context, "Comment Updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }


    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder{
        LinearLayout card;
        TextView name;
        RoundedImageView image;
        ImageView view, edit, remove;
        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_wishlistName);
            image = itemView.findViewById(R.id.wishlist_image);
            card = itemView.findViewById(R.id.wishlist_card);
            view = itemView.findViewById(R.id.btn_view_comment);
            edit = itemView.findViewById(R.id.btn_edit_comment);
            remove = itemView.findViewById(R.id.btn_remove_comment);

        }
    }
}
