package com.hkproductions.listme.guest.startview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hkproductions.listme.DEVELOPER_MODE
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
        viewModel.phoneOwner.observe(viewLifecycleOwner, {
            if (it == null) {
                //TODO Discuss Styling
                binding.houseMemberList.visibility = View.GONE
                binding.houseMembersLabel.visibility = View.GONE
//                binding.button.visibility = View.GONE
//                binding.button2.visibility = View.GONE
                binding.myDataAreaButton.text = resources.getString(R.string.create_phone_owner)
                binding.myDataAreaButton.setOnClickListener {
                    this.findNavController()
                        .navigate(StartViewFragmentDirections.actionDataCreate(-1L))
                }
            } else {
                val navController = this.findNavController()
                it.apply {
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
                    binding.myDataAreaButton.setOnClickListener {
                        navController.navigate(
                            StartViewFragmentDirections.actionToDetail(
                                guestDataId
                            )
                        )
                    }
                }
            }
        })

        viewModel.navigateToDataDetail.observe(viewLifecycleOwner, { member ->
            member?.let {
                this.findNavController()
                    .navigate(StartViewFragmentDirections.actionToDetail(member))
                viewModel.onMemberDetailNavigated()
            }
        })

        configureOnNavigateToCreateMember()

        //DEVELOPER_MODE
        //Clear button to clear database
        if (DEVELOPER_MODE) {
            binding.developerGuestClear.setOnClickListener { viewModel.onClear() }
        } else {
            binding.developerGuestClear.visibility = View.GONE
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
        val adapter = HouseMemberAdapter(
            HouseMemberListener { guestId ->
                viewModel.onMemberClicked(guestId)
            }, AddMemberListener {
                viewModel.onCreateMemberClicked()
            })
        binding.houseMemberList.adapter = adapter

        viewModel.houseMembers.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitListAndAddButton(it)
            }
        })

        val manager = GridLayoutManager(activity, 2)
        binding.houseMemberList.layoutManager = manager
    }

    /**
     * Observer on navigateToCreateMember
     * true -> createMember button is clicked and navigate to editFragment as create
     * false -> nothing is happend
     */
    private fun configureOnNavigateToCreateMember() {
        viewModel.navigateToCreateMember.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(
                    StartViewFragmentDirections.actionDataCreate(-1L)
                )
                viewModel.onMemberCreateNavigated()
            }
        })
    }
}