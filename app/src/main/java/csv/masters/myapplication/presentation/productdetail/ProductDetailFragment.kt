package csv.masters.myapplication.presentation.productdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import csv.masters.myapplication.databinding.FragmentProductDetailBinding
import csv.masters.myapplication.presentation.basket.BasketFragment
import csv.masters.myapplication.presentation.signup.SignUpActivity


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root


        setupView()
    }

    private fun setupView() {
        with(binding) {

            ButtonAdd.setOnClickListener {
                activity?.let {
                    val intent = Intent(it, BasketFragment::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }

}

