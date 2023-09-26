package com.jhunjhunu.patusari.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.modelClass.complaintsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


public class complaintsAdapter extends FirebaseRecyclerAdapter<complaintsModel, complaintsAdapter.myViewHolder> {


    public complaintsAdapter(@NonNull FirebaseRecyclerOptions<complaintsModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final complaintsModel model) {
        holder.username.setText(model.getName());
        holder.userfeedback.setText(model.getFeedback());
        holder.userphone.setText(model.getPhone());
        holder.time.setText((CharSequence) model.getTime());

        holder.layoutComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.userfeedback.getContext(), "Please Long Press To Share", Toast.LENGTH_SHORT).show();
            }
        });
        holder.layoutComplaint.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.userfeedback.getContext())
                        .setGravity(Gravity.CENTER)
                        .setExpanded(true)
                        .setOverlayBackgroundResource(R.color.black_shadow)
                        .setContentBackgroundResource(R.color.transparent)
                        .setContentHolder(new ViewHolder(R.layout.custom_share_and_copy))
                        .create();

                View view = dialogPlus.getHolderView();
                ImageButton share = view.findViewById(R.id.icon_share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT
                                , model.getFeedback() + "\nBy - " + model.getName());
                        holder.username.getContext().startActivity(Intent.createChooser(intent, "Share via"));
                        dialogPlus.dismiss();
                    }
                });
                ImageButton copy = view.findViewById(R.id.icon_copy);
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) holder.userfeedback.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData data = ClipData.newPlainText("Complaints", model.getFeedback());
                        cm.setPrimaryClip(data);
                        dialogPlus.dismiss();
                        Toast.makeText(holder.layoutComplaint.getContext(), "Copied", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogPlus.show();
                return true;
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_complaints_singlerow, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView username, userfeedback, userphone, time;
        CardView layoutComplaint;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.textUserName);
            userfeedback = (TextView) itemView.findViewById(R.id.textUserComplaint);
            userphone = (TextView) itemView.findViewById(R.id.textUserEmail);
            layoutComplaint = (CardView) itemView.findViewById(R.id.cv_user_complaint);
            time = (TextView) itemView.findViewById(R.id.textComplaintTime);

        }
    }
}
