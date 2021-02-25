package com.hkproductions.listme.host.startview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentStartviewBinding;
import com.hkproductions.listme.host.database.HostDatabase;

public class HostStartViewFragment extends Fragment {

    private HostFragmentStartviewBinding binding;
    private HostStartViewModel viewModel;
    private static final String PREFS_LISTME = "com.hkproductions.listme";
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.host_fragment_startview,
                container,
                false
        );

        return binding.getRoot();
    }



    private boolean decisionRememberDecision = false;
    public void createDialog(long[] hostDataIds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alertDialog_title);
        builder.setMultiChoiceItems(R.array.remember_my_decision, null, (dialogInterface, i, b) -> {
            if(b){
                decisionRememberDecision = true;
            }
        });
        builder.setPositiveButton(R.string.confirm_alertDialog, (dialogInterface, i) -> {
            sp.edit().putBoolean("decisionRememberDecision",decisionRememberDecision).apply();
            viewModel.checkout(hostDataIds);
        });
        builder.setNegativeButton(R.string.cancel_alertDialog, (dialogInterface, i) -> {
            Toast t = Toast.makeText(getContext(), R.string.cancelled_check_out_toast, Toast.LENGTH_SHORT);
            t.show();
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getContext().getSharedPreferences(PREFS_LISTME,Context.MODE_PRIVATE);
        //Hide Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        viewModel = new ViewModelProvider(
                this,
                new HostStartViewModelFactory(
                        HostDatabase.Companion.getInstance(
                                requireActivity().getApplication()).getHostDataDao()
                )).get(HostStartViewModel.class
        );

        HostStartAdapter adapter = new HostStartAdapter(new CheckoutListener(
                (hostDataids) -> {
                    if(sp.getBoolean("decisionRememberDecision",decisionRememberDecision)){
                        viewModel.checkout(hostDataids);
                    }
                    else{
                        createDialog(hostDataids);
                    }


                    return null;
                }));
        binding.recyclerViewHostStartViewAreaList.setAdapter(adapter);

        viewModel.getOpenEntries().observe(getViewLifecycleOwner(),
                hostData -> viewModel.actualizeMap());

        viewModel.getCheckedInAreas().observe(getViewLifecycleOwner(), adapter::submitMap);

        binding.buttonHostStartViewEinauschecken.setOnClickListener(event -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX, IntentIntegrator.QR_CODE);
            integrator.setPrompt(getResources().getString(R.string.host_scan_header));
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });

        binding.buttonHostStartViewGaesteliste.setOnClickListener(event -> {
            Navigation.findNavController(requireView()).navigate(HostStartViewFragmentDirections.actionShowGuestList());
        });

        viewModel.getNavigateToScanResult().observe(getViewLifecycleOwner(), bool -> {
            if (bool) {
                Navigation.findNavController(requireView()).navigate(HostStartViewFragmentDirections.actionShowScanResult(viewModel.getNavigateToScanResultData().getValue()));
                viewModel.navigatedToScanResult();
            }
        });

        //To Delete all HostDatas that older than dataLifeSpan
        viewModel.refreshDatabase();

        setHasOptionsMenu(true);

        //DEVELOPERMODE
        binding.buttonHostStartClearHostData.setOnClickListener(event -> {
            viewModel.clearHostData();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.host_start_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.areaManagementMenuItem) {
            Navigation.findNavController(requireView()).navigate(HostStartViewFragmentDirections.actionOpenAreaManagement());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                viewModel.scannedCode(result.getContents(), requireContext());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
