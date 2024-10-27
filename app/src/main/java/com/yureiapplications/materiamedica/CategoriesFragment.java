package com.yureiapplications.materiamedica;
import com.yureiapplications.materiamedica.data.DatabaseHelper;
import com.yureiapplications.materiamedica.model.Categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.onRowClickListener {

    private DatabaseHelper dbHelper;
    private List<Categories> categoriesList = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;

    // Factory method to create a new instance of this fragment
    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        // Initialize UI components
        setupFragmentTitle(view);
        setupRecyclerView(view);

        // Load data from the database asynchronously
        loadCategoriesData();

        return view;
    }

    // Setup the fragment title
    private void setupFragmentTitle(View view) {
        TextView fragmentTitle = view.findViewById(R.id.categories_fragment_title);
        fragmentTitle.setText(R.string.categories_title);
    }

    // Configure the RecyclerView
    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.categories_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //Initialize the adapter with the categories list and listener
        categoriesAdapter = new CategoriesAdapter(requireActivity(), categoriesList, this);
        recyclerView.setAdapter(categoriesAdapter);
    }

    // Fetch categories data from the database asynchronously
    private void loadCategoriesData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Categories> fetchedCategories = fetchCategoriesFromDatabase();

            // Update UI on the main thread
            requireActivity().runOnUiThread(() -> {
                categoriesList.clear();
                categoriesList.addAll(fetchedCategories);
                categoriesAdapter.notifyDataSetChanged();
            });
        });
    }

    // Helper method to fetch categories from the database
    private List<Categories> fetchCategoriesFromDatabase() {
        List<String> names = dbHelper.fetchCategoriesName();
        List<String> descriptions = dbHelper.fetchCategoriesDescription();
        List<String> imagePaths = dbHelper.fetchCategoriesImagePath();

        List<Categories> categories = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            categories.add(new Categories(names.get(i), descriptions.get(i), imagePaths.get(i)));
        }
        return categories;
    }

    // Handle item click events
    @Override
    public void onItemClick(Categories category) {
        // Create a new instance of MedicinesFragment and pass the selected category
        MedicinesFragment medicinesFragment = MedicinesFragment.newInstance(category.getCategories_name());

        // Replace the current fragment with the MedicinesFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, medicinesFragment);
        transaction.addToBackStack(null); // Add to backstack for back navigation
        transaction.commit();
    }
}
