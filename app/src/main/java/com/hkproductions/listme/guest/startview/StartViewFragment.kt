package com.hkproductions.listme.guest.startview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentStartviewBinding

class StartViewFragment : Fragment() {

    private lateinit var binding: GuestFragmentStartviewBinding
    private lateinit var viewModel: StartViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.guest_fragment_startview, container, false)

        return binding.root
    }
}