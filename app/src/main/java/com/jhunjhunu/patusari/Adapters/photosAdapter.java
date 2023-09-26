package com.jhunjhunu.patusari.Adapters;

import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.modelClass.photosModel;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class photosAdapter extends FirebaseRecyclerAdapter<photosModel, photosAdapter.myViewHolder> {


    public photosAdapter(FirebaseRecyclerOptions<photosModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final photosModel model) {
        Glide.with(holder.images.getContext()).load(model.getImageURL()).placeholder(R.drawable.ic_placeholder_app).into(holder.images);
//        holder.name.setText(model.getUserName());

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.images.getContext())
                        .setContentHolder(new ViewHolder(R.layout.custom_photos_fullview))
                        .setGravity(Gravity.CENTER)
                        .setBackgroundColorResId(Color.TRANSPARENT)
                        .setOverlayBackgroundResource(R.color.black_shadow)
//                        .setInAnimation(R.anim.fade_in_center)
//                        .setOutAnimation(R.anim.fade_out_center)
                        .create();

                View view = dialogPlus.getHolderView();

                TextView uploaderName = view.findViewById(R.id.uploaderName);
                uploaderName.setText("Uploaded by :" + " " + model.getUserName());

                ImageView imageView = view.findViewById(R.id.fullPhoto);
                Uri uri = Uri.parse(model.getImageURL());
                Glide.with(holder.images.getContext()).load(uri).placeholder(R.drawable.ic_placeholder).into(imageView);
                dialogPlus.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_photos_singlerow, parent, false);

        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        CardView cvPhotos;
        TextView name;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            images = itemView.findViewById(R.id.photosGallery);
            cvPhotos = (CardView) itemView.findViewById(R.id.cardview_photos);
//            name = itemView.findViewById(R.id.userName);
        }
    }
}
