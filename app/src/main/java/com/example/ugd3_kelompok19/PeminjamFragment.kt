package com.example.ugd3_kelompok19

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*


class PeminjamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.peminjam_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddPeminjam: Button = view.findViewById(R.id.btnAddPeminjam)


        btnAddPeminjam.setOnClickListener(View.OnClickListener {
            val movePeminjam = Intent(this@PeminjamFragment.context, PeminjamActivity::class.java)
            startActivity(movePeminjam)
        })
    }


}