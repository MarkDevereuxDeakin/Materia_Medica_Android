package com.yureiapplications.materiamedica;
import com.yureiapplications.materiamedica.model.Categories;

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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<Categories> categoriesList;
    private final onRowClickListener listener;

    // Constructor
    public CategoriesAdapter(Context context, List<Categories> categoriesList, onRowClickListener listener) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_row, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        holder.bind(category, listener);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    // ViewHolder class
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName, categoryDescription;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categories_name_text);
            categoryDescription = itemView.findViewById(R.id.categories_description_text);
            categoryImage = itemView.findViewById(R.id.categories_image);
        }

        public void bind(Categories category, final onRowClickListener listener) {
            // Set the category name and description
            categoryName.setText(category.getCategories_name());
            categoryDescription.setText(category.getCategories_description());

            // Load image from assets
            String assetImagePath = category.getCategories_image_path(); // Example: "categories/anaesthesia.jpg"
            Log.d("Image Debug", "Image path: " + assetImagePath);

            if (assetImagePath != null && !assetImagePath.isEmpty()) {
                loadImageFromAssets(assetImagePath, categoryImage);
            } else {
                Log.e("Image Debug", "Invalid or empty image path.");
                categoryImage.setImageResource(R.drawable.default_image); // Fallback image
            }

            itemView.setOnClickListener(v -> listener.onItemClick(category));
        }

        // Helper method to load images from assets
        private void loadImageFromAssets(String imagePath, ImageView imageView) {
            AssetManager assetManager = imageView.getContext().getAssets();
            try (InputStream inputStream = assetManager.open("categories/" + imagePath)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("Image Debug", "Error loading image from assets: " + imagePath, e);
                imageView.setImageResource(R.drawable.default_image); // Set default image on error
            }
        }
    }

    // Interface for handling click events
    public interface onRowClickListener {
        void onItemClick(Categories category);
    }
}
