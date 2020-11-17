package com.hkproductions.listme.guest.startview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentStartviewBinding
import com.hkproductions.listme.guest.database.GuestDataDao
import com.hkproductions.listme.guest.database.GuestDatabase

class StartViewFragment : Fragment() {

    private lateinit var binding: GuestFragmentStartviewBinding
    private lateinit var viewModel: StartViewViewModel
    private lateinit var datasource: GuestDataDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.guest_fragment_startview,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        //initialize datasource
        datasource = GuestDatabase.getInstance(application).guestDataDao

        //initialize viewModel
        viewModel = initViewModel()

        //fill Recycler View
        fillRecyclerView()

        //Fill my Data Area with content
        //TODO if phoneOwner is null then make the my_data_area_button to a create Button
        viewModel.phoneOwner.value?.apply {
            binding.myDataAreaButton.text = resources.getString(
                R.string.my_data,
                firstName,
                lastName,
                street,
                houseNumber,
                postalCode,
                city,
                phoneNumber
            )
        }

        return binding.root
    }

    /**
     * Initialize the viewModel variable with an instance of StartViewViewModel
     */
    private fun initViewModel(): StartViewViewModel {
        val viewModelFactory = StartViewViewModelFactory(datasource)
        return ViewModelProvider(this, viewModelFactory).get(StartViewViewModel::class.java)
    }

    /**
     * deliver the RecyclerView an adapter with all house_members plus add Button
     * and give RecyclerView an GridLayoutManager
     */
    private fun fillRecyclerView() {
        val adapter = HouseMemberAdapter(HouseMemberListener { guestId ->
            viewModel.onDataClicked(guestId)
        })
        binding.houseMemberList.adapter = adapter

        viewModel.houseMembers.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitListAndAddButton(it)
            }
        })

        val manager = GridLayoutManager(activity, 2)
        binding.houseMemberList.layoutManager = manager
    }
}