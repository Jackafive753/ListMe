package com.hkproductions.listme.settings;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkproductions.listme.databinding.SettingsFragmentBinding;
import com.hkproductions.listme.R;


public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding binding;
    private SettingsViewModel viewModel;
    private static final String PREFS_LISTME = "com.hkproductions.listme";
    private static final String PREF_DECISION = "decisionRememberDecision";
    private SharedPreferences sp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(SettingsViewModel.class);
        sp = requireContext().getSharedPreferences(PREFS_LISTME, Context.MODE_PRIVATE);
        // initialize Switch
        initSwitchRememberDecision();


        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());
    }

    /**
     * This Method initiates the switch on the Settings Fragment
     * The Switch sets the value of the decisionRememberDecision boolean in the shared preferences file to
     * either true or false
     * A TRUE state represents the user does not want to get asked every time he checks someone out (switch is checked OFF)
     * A FALSE state represents the user wants to get asked every time he checks someone out (switch is checked ON )
     */
    private void initSwitchRememberDecision() {
        binding.switchMaterial.setChecked(!sp.getBoolean(PREF_DECISION, false));
        binding.switchMaterial.setOnClickListener(l -> {
            sp.edit().putBoolean(PREF_DECISION, !binding.switchMaterial.isChecked()).apply();
        });
    }
}