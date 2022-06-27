package com.example.worgess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.worgess.databinding.FragmentGameHomeBinding

class GameHome : Fragment() {
    private var _binding: FragmentGameHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameHomeBinding.inflate(inflater, container, false)
        val view = _binding!!.root

        _binding!!.startButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_gameHome_to_gameFragment)
        }

        // Inflate the layout for this fragment
        return view
    }

}