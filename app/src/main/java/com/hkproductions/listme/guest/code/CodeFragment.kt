package com.hkproductions.listme.guest.code

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentCodeViewBinding
import com.hkproductions.listme.guest.database.GuestDatabase

class CodeFragment : Fragment() {

    private lateinit var binding: GuestFragmentCodeViewBinding
    private lateinit var viewModel: CodeViewModel

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
            layoutInflater,
            R.layout.guest_fragment_code_view,
            container,
            false
        )

        //init ViewModel with database and initDataId from ArgsBundle
        viewModel = ViewModelProvider(
            this,
            CodeViewModelFactory(
                GuestDatabase.getInstance(requireNotNull(this.activity).application).guestDataDao,
                CodeFragmentArgs.fromBundle(requireArguments()).dataId
            )
        ).get(CodeViewModel::class.java)

        //set adapter and fill for recyclerview
        val adapter = ShowContactCodeAdapter(
            ShowContactCodeListener { guestId ->
                viewModel.onShowContactCodeClicked(guestId)
            }
        )
        binding.recyclerViewCodeContacts.adapter = adapter

        viewModel.allGuestData.observe(viewLifecycleOwner, {
            adapter.data = it
        })

        //observer on values to update front end
        viewModel.selected.observe(viewLifecycleOwner, {
            binding.textViewCodeNameLabel.text =
                resources.getString(
                    R.string.code_with_full_name,
                    it.firstName,
                    it.lastName
                )
        })

        viewModel.code.observe(viewLifecycleOwner, {
            binding.imageViewCodeCode.setImageBitmap(it)
        })

        return binding.root
    }

}