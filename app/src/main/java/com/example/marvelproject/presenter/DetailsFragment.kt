package com.example.marvelproject.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.marvelproject.R
import com.example.marvelproject.databinding.FragmentDetailsBinding
import java.lang.System.load

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_details,
            container,
            false
        )


        val image = binding.ivDetailsScreen
        val name = binding.tvDetailsName
        val description = binding.tvDetailsDescription

        name.text = args.details.name
        description.text = args.details.description

        val url = "${args.details.thumbnail.path}.${args.details.thumbnail.extension}"

        Glide
            .with(image.context)
            .load(url)
            .centerCrop()
            .into(image)

        return binding.root
    }


}