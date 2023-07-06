package ziyari.mahan.ashena.ui

import android.Manifest
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    //Other
    private lateinit var navController: NavController
    private var isOnDetailsScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.apply {
            setSupportActionBar(contactsToolbar)
            //Nav
            navController = findNavController(R.id.nav_host)
            bottomNav.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.detailsFragment) {
                    bottomNav.visibility = View.GONE
                    contactsToolbar.visibility = View.GONE
                    isOnDetailsScreen = true
                } else {
                    isOnDetailsScreen = false
                    bottomNav.visibility = View.VISIBLE
                    contactsToolbar.visibility = View.VISIBLE
                    contactsToolbar.title = getString(R.string.ashena)
                }
            }

        }
    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contacts_toolbar_menu, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return true
    }
}