package com.example.ugd3_kelompok19



import android.content.Context
import android.content.SharedPreferences
import com.example.ugd3_kelompok19.databinding.ActivityHomeBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)

        getSupportActionBar()?.hide()
        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val booksFragment = BooksFragment()
        val locationFragment = LocationFragment()
        val profilFragment = ProfilFragment()
        val peminjamFragment = PeminjamFragment()

        setThatFragments(booksFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_books ->{
                    setThatFragments(booksFragment)
                }
                R.id.nav_collect ->{
                    setThatFragments(locationFragment)
                }
                R.id.nav_wishlist->{
                    setThatFragments(peminjamFragment)
                }
                R.id.nav_profil->{
                    setThatFragments(profilFragment)
                }
            }
            true
        }

    }

    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_home,fragment)
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
