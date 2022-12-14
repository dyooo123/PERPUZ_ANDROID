package com.example.ugd3_kelompok19

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class StaffFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.staff_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddStaff: Button = view.findViewById(R.id.btnAddStaff)


        btnAddStaff.setOnClickListener(View.OnClickListener {
            val moveStaff = Intent(this@StaffFragment.context, StaffActivity::class.java)
            startActivity(moveStaff)
        })

    }
}