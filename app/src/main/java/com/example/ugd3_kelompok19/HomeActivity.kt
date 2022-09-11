package com.example.ugd3_kelompok19



import android.content.DialogInterface
import com.example.ugd3_kelompok19.databinding.ActivityHomeBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        getSupportActionBar()?.hide()
        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val booksFragment = BooksFragment()
        val collectionsFragment = CollectionsFragment()
        val profilFragment = ProfilFragment()
        val wishlistFragment = WishlistFragment()

        setThatFragments(booksFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_books ->{
                    setThatFragments(booksFragment)
                }
                R.id.nav_collect ->{
                    setThatFragments(collectionsFragment)
                }
                R.id.nav_wishlist->{
                    setThatFragments(wishlistFragment)
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



}
