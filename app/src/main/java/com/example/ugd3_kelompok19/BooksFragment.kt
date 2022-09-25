package com.example.ugd3_kelompok19

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok19.RVBukuAdapter
import com.example.ugd3_kelompok19.entity.Buku
import kotlinx.android.synthetic.main.rv_item_buku.*

class BooksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVBukuAdapter = RVBukuAdapter(Buku.listOfBooks)

        val rvBuku : RecyclerView = view.findViewById(R.id.rv_buku)

        rvBuku.layoutManager = layoutManager

        rvBuku.setHasFixedSize(true)

        rvBuku.adapter = adapter



    }
}