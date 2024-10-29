package com.yureiapplications.materiamedica;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.yureiapplications.materiamedica.data.DatabaseHelper;
import com.yureiapplications.materiamedica.model.Medicines;
import java.io.IOException;
import java.io.InputStream;

public class DetailsFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private DatabaseHelper dbHelper;

    public static DetailsFragment newInstance(String name) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Initialize UI elements
        ImageView imageView = view.findViewById(R.id.details_image);
        TextView nameView = view.findViewById(R.id.details_name);
        TextView descriptionView = view.findViewById(R.id.details_description);
        TextView referencesView = view.findViewById(R.id.details_references);

        dbHelper = new DatabaseHelper(requireContext());

        if (getArguments() != null) {
            String medicineName = getArguments().getString(ARG_NAME);
            loadMedicineDetails(medicineName, imageView, nameView, descriptionView, referencesView);
        }

        return view;
    }

    private void loadMedicineDetails(String name, ImageView imageView, TextView nameView, TextView descriptionView, TextView referencesView) {
        try {
            // Fetch the medicine details from the database
            Medicines medicine = dbHelper.getMedicineByName(name);

            if (medicine != null) {
                // Set the UI elements with data from the database
                nameView.setText(medicine.getMedicines_name());
                descriptionView.setText(medicine.getMedicines_description().replace("\\n", "\n"));
                referencesView.setText(medicine.getMedicines_references());

                // Load the image from the assets folder
                loadImageFromAssets(medicine.getMedicines_image(), imageView);
            } else {
                Log.e("DetailsFragment", "No medicine found with the name: " + name);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching medicine details", e);
        }
    }

    private void loadImageFromAssets(String imageName, ImageView imageView) {
        try (InputStream inputStream = requireContext().getAssets().open("medicines/" + imageName)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e("DetailsFragment", "Error loading image from assets", e);
            imageView.setImageResource(R.drawable.default_image); // Fallback image
        }
    }
}
