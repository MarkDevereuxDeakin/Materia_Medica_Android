package com.yureiapplications.materiamedica;
import com.yureiapplications.materiamedica.data.DatabaseHelper;
import com.yureiapplications.materiamedica.model.Medicines;
import com.yureiapplications.materiamedica.util.Util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class MedicinesFragment extends Fragment implements MedicinesAdapter.onRowClickListener {

    private static final String ARG_CATEGORIES_NAME = "categories_name";
    private String categoriesName;

    private List<Medicines> medicinesList = new ArrayList<>();
    private MedicinesAdapter medicinesAdapter;
    private DatabaseHelper dbHelper;

    public MedicinesFragment() {
        // Required empty public constructor
    }

    public static MedicinesFragment newInstance(String categoriesName) {
        MedicinesFragment fragment = new MedicinesFragment();
        Bundle args = new Bundle();
        args.putString(Util.MEDICINES_CATEGORIES, categoriesName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoriesName = getArguments().getString(Util.MEDICINES_CATEGORIES);
        }
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicines, container, false);

        // Initialize UI components
        setupFragmentTitle(view);
        setupRecyclerView(view);

        // Fetch and display medicines
        loadMedicinesData();

        return view;
    }

    // Setup the fragment title
    private void setupFragmentTitle(View view) {
        TextView fragmentTitle = view.findViewById(R.id.medicines_fragment_title);
        fragmentTitle.setText(R.string.medicines_title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Adjust for system bars (notches, status bars, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            view.setPadding(0, topInset, 0, 0);
            return insets;
        });
    }



    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.medicines_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //Initialize the adapter with the medicines list and listener
        medicinesAdapter = new MedicinesAdapter(requireActivity(), medicinesList, this);
        recyclerView.setAdapter(medicinesAdapter);
    }

    private void loadMedicinesData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Medicines> fetchedMedicines= dbHelper.fetchMedicinesByCategory(categoriesName);

            // Update UI on the main thread
            requireActivity().runOnUiThread(() -> {
                medicinesList.clear();
                medicinesList.addAll(fetchedMedicines);
                medicinesAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onItemClick(Medicines medicine) {
        DetailsFragment detailsFragment = DetailsFragment.newInstance(
                medicine.getMedicines_name()
        );

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailsFragment); // Use the same container
        transaction.addToBackStack(null); // Add to backstack for back navigation
        transaction.commit();
    }

}
