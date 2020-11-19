package com.hkproductions.listme.guest.editview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentEditViewBinding

class EditViewFragment : Fragment() {

    private lateinit var binding: GuestFragmentEditViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.guest_fragment_edit_view,
            container,
            false
        )

        return binding.root
    }

}