package com.hkproductions.listme.host.tablemanagement

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostFragmentAreaManagementBinding
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostDatabase

class AreaManagementFragment : Fragment() {

    private lateinit var binding: HostFragmentAreaManagementBinding
    private lateinit var viewModel: AreaManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        //initialize binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.host_fragment_area_management,
            container,
            false
        )

        val database = HostDatabase.getInstance(requireActivity().application).hostDataDao

        //initialize viewModel
        viewModel = ViewModelProvider(
            this,
            AreaManagementViewModelFactory(
                database
            )
        ).get(AreaManagementViewModel::class.java)

        //initialize adapter and load recyclerview
        val adapter = AreaManagementAdapter(database, AreaDeleteClickListener { area ->
            //ask if the user really want to delete
            AlertDialog.Builder(requireActivity())
                .setTitle(
                    resources.getString(
                        R.string.delete_area_message_text,
                        area.name
                    )
                )
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewModel.onAreaDeleteClicked(area.areaId)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create().show()

        }, AreaUpdateListener { area: Area, name: String ->
            viewModel.updateAreaName(area.areaId, name)
            //Feedback of the save operation
            Toast.makeText(
                requireContext(), resources.getString(
                    R.string.update_area_message_text,
                    area.name,
                    name
                ),
                Toast.LENGTH_LONG
            ).show()
        })
        binding.recyclerviewAreaManagement.adapter = adapter

        //update list live
        viewModel.areas.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //Add Area clicked
        binding.buttonAreaManagementAddArea.setOnClickListener {
            viewModel.addArea()
        }

//        //Ok Button Clicked
//        binding.buttonAreaManagementOkDesignation.setOnClickListener {
//            //Hide Keyboard
//            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
//                .hideSoftInputFromWindow(view?.windowToken, 0)
//
//            viewModel.setNewDesignation()
//        }

        return binding.root
    }

}