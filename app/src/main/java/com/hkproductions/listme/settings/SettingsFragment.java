package com.hkproductions.listme.settings;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hkproductions.listme.databinding.SettingsFragmentBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hkproductions.listme.R;

public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding binding;
    private SettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater,R.layout.settings_fragment,container,false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(SettingsViewModel.class);



        return binding.getRoot();


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    binding.setViewModel(viewModel);
    binding.setLifecycleOwner(this);
    }

}