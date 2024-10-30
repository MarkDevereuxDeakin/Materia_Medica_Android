package com.yureiapplications.materiamedica;
import com.yureiapplications.materiamedica.util.Disclaimer;
import com.yureiapplications.materiamedica.util.Terms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private TextView fragmentTitle;

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkAndShowDialogs();
        loadFragment(CategoriesFragment.newInstance(), "Materia Medica");
    }
    private void checkAndShowDialogs() {
        SharedPreferences sharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        boolean isTermsAccepted = sharedPreferences.getBoolean("TERMS_ACCEPTED", false);
        boolean isDisclaimerAccepted = sharedPreferences.getBoolean("DISCLAIMER_ACCEPTED", false);

        if (!isTermsAccepted) {
            showTermsDialog();
        } else if (!isDisclaimerAccepted) {
            showDisclaimerDialog();
        }
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Terms and Conditions")
                .setMessage("By using this app, you agree to the following terms: " + Terms.TERMS_AND_CONDITIONS)
                .setCancelable(false)
                .setPositiveButton("I AGREE", (dialog, which) -> {
                    SharedPreferences.Editor editor = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE).edit();
                    editor.putBoolean("TERMS_ACCEPTED", true);
                    editor.apply();
                    dialog.dismiss();
                    showDisclaimerDialog();
                })
                .setNegativeButton("I DISAGREE", (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
        adjustDialogButtons(dialog);
    }

    private void showDisclaimerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Disclaimer")
                .setMessage("By using this app, you agree to the following disclaimer: " + Disclaimer.DISCLAIMER)
                .setCancelable(false)
                .setPositiveButton("I AGREE", (dialog, which) -> {
                    SharedPreferences.Editor editor = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE).edit();
                    editor.putBoolean("DISCLAIMER_ACCEPTED", true);
                    editor.apply();
                    dialog.dismiss();
                })
                .setNegativeButton("I DISAGREE", (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
        adjustDialogButtons(dialog);
    }

    private void adjustDialogButtons(AlertDialog dialog) {
        int padding = getResources().getDimensionPixelSize(R.dimen.dialog_button_padding);
        int[] buttons = {AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE};

        for (int buttonType : buttons) {
            Button button = dialog.getButton(buttonType);
            if (button != null) {
                button.setPadding(padding, 0, padding, 0);
            }
        }
    }

    private void loadFragment(Fragment fragment, String title) {
        toolbarTitle.setText(title); // Set the toolbar title
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Add to backstack for proper back navigation
        transaction.commit();
    }
/*
    private final FragmentManager.OnBackStackChangedListener backStackListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_framelayout);
                    if (currentFragment instanceof CategoriesFragment) {
                        updateFragmentTitle("Categories");
                    } else if (currentFragment instanceof MedicinesFragment) {
                        updateFragmentTitle("Medicines");
                    } else if (currentFragment instanceof DetailsFragment) {
                        updateFragmentTitle("Details");
                    }
                }
            };

    private void updateFragmentTitle(String title) {
        fragmentTitle.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(backStackListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(backStackListener);
    }*/


}
