package com.yureiapplications.materiamedica;
import com.yureiapplications.materiamedica.model.Medicines;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.MedicineViewHolder> {

    private final List<Medicines> medicinesList;
    private final Context context;
    private final onRowClickListener listener;

    public MedicinesAdapter(Context context, List<Medicines> medicinesList, onRowClickListener listener) {
        this.context = context;
        this.medicinesList = medicinesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medicines_row, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicines medicine = medicinesList.get(position);
        holder.bind(medicine, listener);
    }

    @Override
    public int getItemCount() {
        return medicinesList.size();
    }

    // ViewHolder class
    class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;
        ImageView medicineImage;

        MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineImage = itemView.findViewById(R.id.medicine_image);
        }

        public void bind(Medicines medicines, final onRowClickListener listener) {
            // Set the category name and description
            medicineName.setText(medicines.getMedicines_name());

            // Load image from assets
            String assetImagePath = medicines.getMedicines_image(); // Example: "medicines/Milk_Thistle.jpg"
            Log.d("Image Debug", "Image path: " + assetImagePath);

            if (assetImagePath != null && !assetImagePath.isEmpty()) {
                loadImageFromAssets(assetImagePath, medicineImage);
            } else {
                Log.e("Image Debug", "Invalid or empty image path.");
                medicineImage.setImageResource(R.drawable.default_image); // Fallback image
            }

            itemView.setOnClickListener(v -> listener.onItemClick(medicines));
        }

        // Load image from assets
        private void loadImageFromAssets(String imagePath, ImageView imageView) {
            AssetManager assetManager = imageView.getContext().getAssets();
            try (InputStream inputStream = assetManager.open("medicines/" + imagePath)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("Image Load Error", "Error loading image: " + imagePath, e);
                imageView.setImageResource(R.drawable.default_image); // Fallback image
            }
        }
    }

    // Interface for handling row clicks
    public interface onRowClickListener {
        void onItemClick(Medicines medicine);
    }
}
