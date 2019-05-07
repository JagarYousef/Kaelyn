package me.jagar.kaelyn.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.jagar.kaelyn.R;
import me.jagar.kaelyn.activities.RecordsActivity;
import me.jagar.kaelyn.models.Record;
import me.jagar.kaelyn.storage.SQLDatabaseHelper;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    private Context context;
    private List<Record> recordsList;
    private ImageView placeHolder;

    public RecordsAdapter(Context context, List<Record> recordsList, ImageView placeHolder) {
        this.context = context;
        this.recordsList = recordsList;
        this.placeHolder = placeHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgEmo;
        public TextView txtAmount, txtRate, txtTime, txtDate;
        public CardView cvBG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEmo = itemView.findViewById(R.id.imgEmotion);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtRate = itemView.findViewById(R.id.txtRate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDate = itemView.findViewById(R.id.txtDate);
            cvBG = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.records_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Record record = recordsList.get(position);

        holder.txtAmount.setText(String.valueOf(record.getAmount()));
        holder.txtRate.setText(String.valueOf(record.getRate()));
        holder.txtTime.setText(String.valueOf(record.getTime()));
        String dateString = DateFormat.format("dd, MMM\nyyyy", new Date(record.getDate())).toString();
        holder.txtDate.setText(dateString);

        if (record.getEmo() == 1){
            Glide.with(context).load(R.drawable.ic_sentiment_neutral_white_24dp).into(holder.imgEmo);
            holder.cvBG.setCardBackgroundColor(context.getResources().getColor(R.color.netural));
        }else if (record.getEmo() == 0){
            Glide.with(context).load(R.drawable.ic_sentiment_satisfied_white_24dp).into(holder.imgEmo);
            holder.cvBG.setCardBackgroundColor(context.getResources().getColor(R.color.smile));
        }else if (record.getEmo() == 2){
            Glide.with(context).load(R.drawable.ic_sentiment_dissatisfied_white_24dp).into(holder.imgEmo);
            holder.cvBG.setCardBackgroundColor(context.getResources().getColor(R.color.sad));
        }

        holder.cvBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View content = LayoutInflater.from(context).inflate(R.layout.info_layout, null);
                builder.setView(content);

                ImageView imgEmo = content.findViewById(R.id.imgEmo);
                TextView txtAmount = content.findViewById(R.id.txtAmount);
                TextView txtRate = content.findViewById(R.id.txtRate);
                TextView txtTime = content.findViewById(R.id.txtTime);
                TextView txtDateAndTime = content.findViewById(R.id.txtDateAndTime);
                Button btnEdit = content.findViewById(R.id.btnEdit);
                ImageButton imgBtnDelete = content.findViewById(R.id.imgBtnDelete);
                TextView txtNote = content.findViewById(R.id.txtNote);
                LinearLayout ll_info = content.findViewById(R.id.ll_info);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                if (record.getEmo() == 1){
                    Glide.with(context).load(R.drawable.ic_sentiment_neutral_blue_24dp).into(imgEmo);
                    ll_info.setBackgroundColor(context.getResources().getColor(R.color.netural));
                    txtNote.setTextColor(context.getResources().getColor(R.color.netural));
                    txtDateAndTime.setTextColor(context.getResources().getColor(R.color.netural));
                }else if (record.getEmo() == 0){
                    Glide.with(context).load(R.drawable.ic_sentiment_satisfied_green_24dp).into(imgEmo);
                    ll_info.setBackgroundColor(context.getResources().getColor(R.color.smile));
                    txtNote.setTextColor(context.getResources().getColor(R.color.smile));
                    txtDateAndTime.setTextColor(context.getResources().getColor(R.color.smile));
                }else if (record.getEmo() == 2){
                    Glide.with(context).load(R.drawable.ic_sentiment_dissatisfied_black_24dp).into(imgEmo);
                    ll_info.setBackgroundColor(context.getResources().getColor(R.color.sad));
                    txtNote.setTextColor(context.getResources().getColor(R.color.sad));
                    txtDateAndTime.setTextColor(context.getResources().getColor(R.color.sad));
                }

                txtNote.setText(record.getNote());
                txtAmount.setText(String.valueOf(record.getAmount()));
                txtRate.setText(String.valueOf(record.getRate()));
                txtTime.setText(String.valueOf(record.getTime()));
                String dateString = DateFormat.format("dd, MMMM, yyyy", new Date(record.getDate())).toString();
                String timeString = DateFormat.format("HH:mm:ss", new Date(record.getDate())).toString();
                txtDateAndTime.setText(dateString + " at " + timeString);

                imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder alertDialog1 =new AlertDialog.Builder(context);
                        alertDialog1.setMessage("Are you sure that you want to delete this record?");

                        alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog alertDialog2 = alertDialog1.create();
                                alertDialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                                SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(context);
                                sqlDatabaseHelper.deleteRecord(record);
                                alertDialog2.dismiss();
                                alertDialog.dismiss();
                                Toast.makeText(context, "The record has been deleted", Toast.LENGTH_LONG)
                                        .show();
                                final MediaPlayer mp = MediaPlayer.create(context, R.raw.delete);
                                mp.setVolume(0.05f, 0.05f);
                                mp.start();
                                recordsList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, recordsList.size());
                                if (recordsList.size() <= 0)
                                    placeHolder.setVisibility(View.VISIBLE);
                            }
                        });
                        alertDialog1.setNegativeButton("No", null);
                        AlertDialog alertDialog2 = alertDialog1.create();
                        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog2.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_bg));
                        alertDialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                        final MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                        mp.setVolume(0.05f, 0.05f);
                        mp.start();
                        alertDialog2.show();
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Record record_to_update = new Record();
                        alertDialog.dismiss();
                        final SQLDatabaseHelper sqlDatabaseHelper = new SQLDatabaseHelper(context);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View content = LayoutInflater.from(context).inflate(R.layout.record_popup, null);
                        builder.setView(content);

                        final EditText edtAmount = content.findViewById(R.id.edtAmount);
                        final EditText edtTime = content.findViewById(R.id.edtTime);
                        final EditText edtRate = content.findViewById(R.id.edtRate);
                        final EditText edtNotes = content.findViewById(R.id.edtNotes);
                        Button btnCalcualte = content.findViewById(R.id.btnCalculate);
                        Button btnSave = content.findViewById(R.id.btnSave);
                        final ImageView imgSmile = content.findViewById(R.id.imgSmile);
                        final ImageView imgNormal = content.findViewById(R.id.imgNormal);
                        final ImageView imgSad = content.findViewById(R.id.imgSad);
                        final ImageButton imgBtnCancel = content.findViewById(R.id.imgBtnCancel);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                        edtAmount.setText(String.valueOf(record.getAmount()));
                        edtRate.setText(String.valueOf(record.getRate()));
                        edtTime.setText(String.valueOf(record.getTime()));
                        edtNotes.setText(record.getNote());

                        final int[] selected_emo = {-1};
                        if (record.getEmo() == 1) {
                            GradientDrawable selected_shape = new GradientDrawable();
                            selected_shape.setColor(context.getResources().getColor(R.color.gray));
                            selected_shape.setCornerRadius(8);
                            GradientDrawable unselected_shape = new GradientDrawable();
                            unselected_shape.setColor(context.getResources().getColor(R.color.white));
                            unselected_shape.setCornerRadius(0);

                            imgNormal.setBackground(selected_shape);
                            imgSad.setBackground(unselected_shape);
                            imgSmile.setBackground(unselected_shape);
                            selected_emo[0] = 1;


                        }else if (record.getEmo() == 0){
                            GradientDrawable selected_shape = new GradientDrawable();
                            selected_shape.setColor(context.getResources().getColor(R.color.gray));
                            selected_shape.setCornerRadius(8);
                            GradientDrawable unselected_shape = new GradientDrawable();
                            unselected_shape.setColor(context.getResources().getColor(R.color.white));
                            unselected_shape.setCornerRadius(0);

                            imgSmile.setBackground(selected_shape);
                            imgSad.setBackground(unselected_shape);
                            imgNormal.setBackground(unselected_shape);

                            selected_emo[0] = 0;
                        }else if (record.getEmo() == 2){
                            GradientDrawable selected_shape = new GradientDrawable();
                            selected_shape.setColor(context.getResources().getColor(R.color.gray));
                            selected_shape.setCornerRadius(8);
                            GradientDrawable unselected_shape = new GradientDrawable();
                            unselected_shape.setColor(context.getResources().getColor(R.color.white));
                            unselected_shape.setCornerRadius(0);

                            imgSad.setBackground(selected_shape);
                            imgSmile.setBackground(unselected_shape);
                            imgNormal.setBackground(unselected_shape);

                            selected_emo[0] = 2;
                        }



                        btnCalcualte.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                double amount = 0.0;
                                double time = 0.0;
                                double rate = 0.0;
                                if (edtAmount.getText().toString().trim().length() > 0)
                                    amount = Double.valueOf(edtAmount.getText().toString().trim());
                                if (edtTime.getText().toString().trim().length() > 0)
                                    time = Double.valueOf(edtTime.getText().toString().trim());
                                if (edtRate.getText().toString().trim().length() > 0)
                                    rate = Double.valueOf(edtRate.getText().toString().trim());
                                if (amount == 0.0 && (time != 0.0 && rate != 0.0))
                                    edtAmount.setText(String.valueOf(time * rate));
                                else if (rate == 0.0 && (amount != 0.0 && time != 0.0))
                                    edtRate.setText(String.valueOf(amount / time));
                                else if (time == 0.0 && (amount != 0.0 && rate != 0.0))
                                    edtTime.setText(String.valueOf(amount / rate));
                                else if (amount != 0.0 && rate != 0.0 && time != 0.0)
                                    Toast.makeText(context, "Nothing to calculate!", Toast.LENGTH_SHORT)
                                            .show();
                                else
                                    Toast.makeText(context, "At least 2 valuse need to be defined!", Toast.LENGTH_LONG)
                                            .show();
                            }
                        });

                        imgBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                Toast.makeText(context, "The editing has been cancelled!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        imgSmile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GradientDrawable selected_shape = new GradientDrawable();
                                selected_shape.setColor(context.getResources().getColor(R.color.gray));
                                selected_shape.setCornerRadius(8);
                                GradientDrawable unselected_shape = new GradientDrawable();
                                unselected_shape.setColor(context.getResources().getColor(R.color.white));
                                unselected_shape.setCornerRadius(0);

                                imgSmile.setBackground(selected_shape);
                                imgSad.setBackground(unselected_shape);
                                imgNormal.setBackground(unselected_shape);

                                selected_emo[0] = 0;

                            }
                        });
                        imgSad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GradientDrawable selected_shape = new GradientDrawable();
                                selected_shape.setColor(context.getResources().getColor(R.color.gray));
                                selected_shape.setCornerRadius(8);
                                GradientDrawable unselected_shape = new GradientDrawable();
                                unselected_shape.setColor(context.getResources().getColor(R.color.white));
                                unselected_shape.setCornerRadius(0);

                                imgSad.setBackground(selected_shape);
                                imgSmile.setBackground(unselected_shape);
                                imgNormal.setBackground(unselected_shape);

                                selected_emo[0] = 2;

                            }
                        });
                        imgNormal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GradientDrawable selected_shape = new GradientDrawable();
                                selected_shape.setColor(context.getResources().getColor(R.color.gray));
                                selected_shape.setCornerRadius(8);
                                GradientDrawable unselected_shape = new GradientDrawable();
                                unselected_shape.setColor(context.getResources().getColor(R.color.white));
                                unselected_shape.setCornerRadius(0);

                                imgNormal.setBackground(selected_shape);
                                imgSad.setBackground(unselected_shape);
                                imgSmile.setBackground(unselected_shape);
                                selected_emo[0] = 1;

                            }
                        });

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (edtAmount.getText().toString().trim().length() <= 0){
                                    edtAmount.setError("This value is requested!");
                                    edtAmount.requestFocus();
                                }else if (edtRate.getText().toString().trim().length() <= 0){
                                    edtRate.setError("This value is requested!");
                                    edtRate.requestFocus();
                                }else if (edtTime.getText().toString().trim().length() <= 0){
                                    edtTime.setError("This value is requested!");
                                    edtTime.requestFocus();
                                }else{
                                    record_to_update.setAmount(Double.valueOf(edtAmount.getText().toString().trim()));
                                    record_to_update.setRate(Double.valueOf(edtRate.getText().toString().trim()));
                                    record_to_update.setTime(Double.valueOf(edtTime.getText().toString().trim()));
                                    if (selected_emo[0] != -1)
                                        record_to_update.setEmo(selected_emo[0]);
                                    else
                                        record_to_update.setEmo(1);
                                    if (edtNotes.getText().toString().trim().length() > 0)
                                        record_to_update.setNote(edtNotes.getText().toString());
                                    else
                                        record_to_update.setNote("N/A");
                                    record_to_update.setDate(record.getDate());
                                    record_to_update.setId(record.getId());
                                    sqlDatabaseHelper.updateRecord(record_to_update);
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "The record has been updated successfully"
                                            ,Toast.LENGTH_SHORT).show();

                                    //Update record UI
                                    final MediaPlayer mp = MediaPlayer.create(context, R.raw.success);
                                    mp.setVolume(0.05f, 0.05f);
                                    mp.start();
                                    recordsList.remove(position);
                                    notifyItemRemoved(position);
                                    recordsList.add(position, record_to_update);
                                    notifyItemRangeChanged(position, recordsList.size());
                                    notifyDataSetChanged();
                                    if (recordsList.size() <= 0)
                                        placeHolder.setVisibility(View.VISIBLE);

                                }
                            }
                        });

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_bg));
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                        final MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                        mp.setVolume(0.05f, 0.05f);
                        mp.start();
                        alertDialog.show();
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_bg));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                mp.setVolume(0.05f, 0.05f);
                mp.start();
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

}
