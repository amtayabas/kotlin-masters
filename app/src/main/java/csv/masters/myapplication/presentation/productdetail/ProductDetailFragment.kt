package csv.masters.myapplication.presentation.productdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.FragmentProductDetailBinding


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val LOG_TAG = ProductDetailFragment::class.java
    }
}