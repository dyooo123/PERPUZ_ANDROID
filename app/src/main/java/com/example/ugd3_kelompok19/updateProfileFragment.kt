package com.example.ugd3_kelompok19

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd3_kelompok19.databinding.FragmentProfilBinding
import com.example.ugd3_kelompok19.databinding.FragmentUpdateProfileBinding
import kotlinx.android.synthetic.main.fragment_update_profile.*


class updateProfileFragment : Fragment() {
    private var _binding:FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnUpdate.setOnClickListener {
            moveFragment(ProfilFragment())
        }
    }

    private fun moveFragment(fragment: Fragment){
        val move = requireActivity().supportFragmentManager.beginTransaction()
        move.replace(R.id.layoutProfile,fragment)
            .addToBackStack(null).commit()
        move.hide(ProfilFragment())
    }

}