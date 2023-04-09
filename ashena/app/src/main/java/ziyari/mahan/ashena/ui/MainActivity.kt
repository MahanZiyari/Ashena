package ziyari.mahan.ashena.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding

    //Other
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.apply {
            //Nav
            navController = findNavController(R.id.nav_host)
            bottomNav.setupWithNavController(navController)
        }
    }
}