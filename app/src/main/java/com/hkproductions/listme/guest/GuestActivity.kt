package com.hkproductions.listme.guest

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.hkproductions.listme.MainActivity
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestActivityBinding

class GuestActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<GuestActivityBinding>(this, R.layout.guest_activity)

        /**
         * add nav Controller in the action bar.
         * On the home fragment in this activity the burger menu button is shown,
         * on the other fragments an arrow to navigateUp is shown
         */
        drawerLayout = binding.guestDrawerLayout

        binding.guestNavView.setNavigationItemSelectedListener(this)

        val navController = this.findNavController((R.id.guestNavHostFragment))
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }

    /**
     * called when user want navigateUp
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.guestNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    /**
     * Called if an NavigationItem clicked
     * call navigationItemSelected from MainActivity companion object(equals to static)
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return MainActivity.navigationItemSelected(this, item, drawerLayout)
    }
}