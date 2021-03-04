package com.hkproductions.listme.guest.startview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.hkproductions.listme.Constant
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
    ): View {

        //Hide Keyboard
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

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
        viewModel.phoneOwner.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.no_phone_owner_message),
                    Toast.LENGTH_LONG
                ).show()
                this.findNavController().navigate(StartViewFragmentDirections.actionDataCreate(-1L))
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

        viewModel.navigateToDataDetail.observe(viewLifecycleOwner, Observer { member ->
            if (member > 0L) {
                this.findNavController()
                    .navigate(StartViewFragmentDirections.actionToDetail(member))
                viewModel.onMemberDetailNavigated()

            }
        })

        /* Observe viewModel.navigateToCreateContact (is a boolean)
            if this boolean change value the add contact button was pressed
         */
        configureOnNavigateToCreateContact()

        binding.buttonGuestCodeGroup.setOnClickListener {
            this.findNavController().navigate(StartViewFragmentDirections.actionToCreateGroup())
        }

        binding.buttonGuestShowCode.setOnClickListener {
            this.findNavController()
                .navigate(
                    StartViewFragmentDirections
                        .actionShowPhoneOwnerCode(viewModel.phoneOwner.value!!.guestDataId)
                )
        }

        binding.buttonGuestStartAddContact.setOnClickListener {
            this.findNavController().navigate(StartViewFragmentDirections.actionDataCreate(-1L))
        }

        //DEVELOPER_MODE
        //Clear button to clear database
        if (Constant.DEVELOPER_MODE) {
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
        val adapter = ContactAdapter(
            ContactListener { guestId ->
                viewModel.onMemberClicked(guestId)
            })
        binding.houseMemberList.adapter = adapter

        viewModel.houseMembers.observe(viewLifecycleOwner, {
            it.let {
                adapter.data = it
            }
        })
    }

    /**
     * Observer on navigateToCreateMember
     * true -> createMember button is clicked and navigate to editFragment as create
     * false -> nothing is happend
     */
    private fun configureOnNavigateToCreateContact() {
        viewModel.navigateToCreateContact.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(
                    StartViewFragmentDirections.actionDataCreate(-1L)
                )
                viewModel.onMemberCreateNavigated()
            }
        })
    }
}