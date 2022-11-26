package com.example.ugd3_kelompok19

import android.content.Context
import android.content.SharedPreferences
import com.example.ugd3_kelompok19.databinding.ActivityHomeBinding
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)

        getSupportActionBar()?.hide()

        val booksFragment = BooksFragment()
        val locationFragment = LocationFragment()
        val profilFragment = ProfilFragment()
        val peminjamFragment = PeminjamFragment()

        setThatFragments(booksFragment)
        bottom_navigation.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {

                //redirecting fragments
                when(newIndex){
                    0 -> setThatFragments(booksFragment);
                    1 -> setThatFragments(locationFragment);
                    2 -> setThatFragments(peminjamFragment);
                    3 -> setThatFragments(profilFragment);
                }
                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        });

    }


    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.bottom_nav_menu,menu)
        return true
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }


}
