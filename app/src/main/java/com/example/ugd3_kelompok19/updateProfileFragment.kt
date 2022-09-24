package com.example.ugd3_kelompok19

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd3_kelompok19.databinding.FragmentProfilBinding
import com.example.ugd3_kelompok19.databinding.FragmentUpdateProfileBinding
import com.example.ugd3_kelompok19.room.User
import com.example.ugd3_kelompok19.room.UserDB
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
        var checkUpdate = true

        binding.btnUpdate.setOnClickListener {
            if(binding.tietUsername.text.toString().isEmpty()){
                binding.tietUsername.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietEmail.text.toString().isEmpty()){
                binding.tietEmail.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietTanggalLahir.text.toString().isEmpty()){
                binding.tietTanggalLahir.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietNoTelp.text.toString().isEmpty()){
                binding.tietNoTelp.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(!checkUpdate){
                checkUpdate = true
                return@setOnClickListener
            }else{
                updateData()
                moveFragment(ProfilFragment())
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateData(){
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        val db by lazy { UserDB(activity as  HomeActivity) }
        val userDao = db.userDao()
        val id = sharedPreferences!!.getInt("id",0)
        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.tietUsername.text.toString(),
            binding.tietEmail.text.toString(),
            getUser.password,
            binding.tietTanggalLahir.text.toString(),
            binding.tietNoTelp.text.toString(),
        )
        userDao.updateUser(user)



    }

    private fun moveFragment(fragment: Fragment){
        val move = requireActivity().supportFragmentManager.beginTransaction()
        move.replace(R.id.layoutProfile,fragment)
            .addToBackStack(null).commit()
        move.hide(ProfilFragment())
    }

}