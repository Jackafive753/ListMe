package com.hkproductions.listme.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.MainFragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MainFragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment_home, container, false)

        binding.homeGuestButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionStartGuest())
        }

        return binding.root
    }

}