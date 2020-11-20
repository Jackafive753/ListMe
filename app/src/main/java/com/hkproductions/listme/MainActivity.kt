package com.hkproductions.listme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.hkproductions.listme.guest.GuestActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    fun onGuest(view: View) {
        startActivity(Intent(this, GuestActivity::class.java))
    }

    companion object {
        /**
         * Start Activity of the clicked Item and close drawerLayout
         *
         * Standard NavigationMethod
         * if the activity open of the clicked menu item close drawer and do nothing
         */
        fun navigationItemSelected(
            context: Context,
            item: MenuItem,
            drawerLayout: DrawerLayout
        ): Boolean {
            when (item.itemId) {
                R.id.navigate_host -> {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
//                context.startActivity(Intent(context, HostActivity::class.java))
                }
                R.id.navigate_guest -> {
                    context.startActivity(Intent(context, GuestActivity::class.java))
                }
                R.id.navigate_instruction -> {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
//                context.startActivity(Intent(context, InstructionActivity::class.java))
                }
                R.id.navigate_about -> {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
//                context.startActivity(Intent(context, AboutActivity::class.java))
                }
                R.id.navigate_data_protection -> {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
//                context.startActivity(Intent(context, DataProtectionActivity::class.java))
                }
                R.id.navigate_impressum -> {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_LONG).show()
//                context.startActivity(Intent(context, ImpressumActivity::class.java))
                }
            }
            drawerLayout.close()
            return true
        }
    }
}