package com.example.myloginapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloginapp.LessorPaymentResponse;
import com.example.myloginapp.PaymentResponse;
import com.example.myloginapp.LessorPaymentViewHolder;
import com.example.myloginapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LessorAdaptor extends RecyclerView.Adapter<LessorPaymentViewHolder> {

    private List<PaymentResponse> payments; // Change the generic type here
    private Context context;
    private OnButtonClickListener onButtonClickListener;
    private List<Integer> hiddenButtonPositions = new ArrayList<>();


    public interface OnButtonClickListener {
        void onButton1Click(int position, String paymentId);

        void onButton2Click(int position, String paymentId);
    }

    public LessorAdaptor(Context context, List<PaymentResponse> payments) {
        this.payments = payments;
        this.context = context;
        this.onButtonClickListener = (OnButtonClickListener) context;
        this.hiddenButtonPositions = new ArrayList<>();
    }

    @NonNull
    @Override
    public LessorPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclercardpayment, parent, false);
        return new LessorPaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LessorPaymentViewHolder holder, int position) {
        PaymentResponse payment = payments.get(position);

        String imageData = payment.getImage();

        if (payment.getImage() != null && !payment.getImage().isEmpty()) {
            Bitmap imageBitmap = decodeBase64(payment.getImage());
            holder.screenshot.setImageBitmap(imageBitmap);
        } else {
            // If screenshot is empty, set a placeholder or handle it accordingly
            holder.screenshot.setImageResource(R.drawable.background_imagesquare);
        }


            holder.status.setText(payment.getStatus());
        holder.lessee.setText("Lessee: " + payment.getLessee());
        holder.date.setText(payment.getDate());
        holder.id.setText("Payment ID :" + payment.getPaymentId());
        holder.decline.setOnClickListener(v -> {
            if (onButtonClickListener != null) {
                onButtonClickListener.onButton1Click(position, payment.getPaymentId());

                // Save the position in hiddenButtonPositions list
                hiddenButtonPositions.add(position);

                // Hide the buttons
                if ("Failed".equalsIgnoreCase(payment.getStatus())) {
                    holder.decline.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                } else if ("Success".equalsIgnoreCase(payment.getStatus())) {
                    holder.decline.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                }
            }
        });

        holder.accept.setOnClickListener(v -> {
            if (onButtonClickListener != null) {
                onButtonClickListener.onButton2Click(position, payment.getPaymentId());

                // Save the position in hiddenButtonPositions list
                hiddenButtonPositions.add(position);

                // Hide the buttons
                if ("Failed".equalsIgnoreCase(payment.getStatus())) {
                    holder.decline.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                } else if ("Success".equalsIgnoreCase(payment.getStatus())) {
                    holder.decline.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                }
            }
        });

        // Check if the current position is in hiddenButtonPositions
        if (hiddenButtonPositions.contains(position)) {
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
            // Adjust image size or any other changes if needed
            int newWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
            int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            holder.screenshot.setLayoutParams(params);
        } else {
            holder.decline.setVisibility(View.VISIBLE);
            holder.accept.setVisibility(View.VISIBLE);
            // Reset image size or any other changes if needed
            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.screenshot.setLayoutParams(params);
        }

        // Set text color based on payment status
        if ("Failed".equalsIgnoreCase(payment.getStatus())) {
            holder.status.setTextColor(context.getResources().getColor(R.color.leasedTextColor));
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
            holder.title.setText("Payment Failed");
            holder.question.setVisibility(View.GONE);

            // Adjust image size or any other changes if needed
            int newWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
            int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            holder.screenshot.setLayoutParams(params);
        } else if ("Success".equalsIgnoreCase(payment.getStatus())) {
            holder.status.setTextColor(context.getResources().getColor(R.color.availableTextColor));
            holder.title.setText("Payment Success");
            holder.question.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
            // Adjust image size or any other changes if needed
            int newWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
            int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            holder.screenshot.setLayoutParams(params);
        }

        if (holder.accept.getVisibility() == View.GONE) {
            int newWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
            int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            holder.screenshot.setLayoutParams(params);
        } else {
            int newWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
            int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = holder.screenshot.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            holder.screenshot.setLayoutParams(params);
        }
    }
    @Override
    public int getItemCount() {
        return payments != null ? payments.size() : 0;
    }
    private Bitmap decodeBase64(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
