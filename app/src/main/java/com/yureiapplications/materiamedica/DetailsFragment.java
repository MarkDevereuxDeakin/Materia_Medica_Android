package com.yureiapplications.materiamedica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class DetailsFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_REFERENCES = "references";

    public static DetailsFragment newInstance(String name, String image, String description, String references) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_REFERENCES, references);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView imageView = view.findViewById(R.id.details_image);
        TextView nameView = view.findViewById(R.id.details_name);
        TextView descriptionView = view.findViewById(R.id.details_description);
        TextView referencesView = view.findViewById(R.id.details_references);

        if (getArguments() != null) {
            nameView.setText(getArguments().getString(ARG_NAME));
            descriptionView.setText(getArguments().getString(ARG_DESCRIPTION));
            referencesView.setText(getArguments().getString(ARG_REFERENCES));

            String imagePath = "file:///android_asset/medicines/" + getArguments().getString(ARG_IMAGE);
            Glide.with(requireContext())
                    .load(imagePath)
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
        }

        return view;
    }
}
