package com.test.medpersonal.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var navController: NavController
    private val fragments = arrayListOf(
        R.id.groupFragment,
        R.id.profileFragment,
        R.id.homeFragment

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavController()
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")
    }

    //проверка на то что есть такой юзер или нет
    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            navController.navigate(R.id.authFragment)
        }

    }

    private fun initNavController() {
        val navHostController = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        navController = navHostController!!.findNavController()
        binding.bottomNav.setupWithNavController(navController)


        //скрыть bottomNavigation
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (fragments.contains(destination.id)) {
                binding.bottomNav.visibility = View.VISIBLE
            } else binding.bottomNav.visibility = View.GONE
        }
    }

}