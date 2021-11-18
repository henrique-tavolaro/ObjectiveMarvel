package com.example.marvelproject.presenter

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marvelproject.R
import com.example.marvelproject.databinding.FragmentHomeBinding
import com.example.marvelproject.domain.repository.Status
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val viewModel: MarvelViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        val editText = binding.etSearch

        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    viewModel.resetSearchParams()
                    viewModel.searchCharacter(editText.text.toString())
                    editText.hideKeyboard()
                    editText.clearFocus()
                    return true
                }
                return false
            }

        })

        val adapter = CharacterAdapter(CharacterListener { id ->
            viewModel.onCharacterClicked(id)
            //todo
        })
        binding.rvHome.adapter = adapter

        viewModel.navigateToDetails.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
                )
                viewModel.resetSearchParams()
                editText.text!!.clear()
                viewModel.onDetailsNavigated()

            }
        })
        val progressBar = binding.progressCircular

        viewModel.response.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        adapter.submitList(it.data!!.data.results)
                        progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.pageNumberList.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.page.observe(viewLifecycleOwner, { page ->
                    val pageAdapter = PageAdapter(it, requireContext(), page)
                    binding.rvPageNumber.adapter = pageAdapter
                })
            }
        })

        val nextPage = binding.ivNextPage
        nextPage.setOnClickListener {

            viewModel.nextPage(editText.text.toString())
        }

        val previousPage = binding.ivPreviusPage
        previousPage.setOnClickListener {

            viewModel.previousPage(editText.text.toString())
        }



        return binding.root


    }

}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}