package com.example.ourtravelmap

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.ourtravelmap.databinding.ActivityDataBinding

class DataActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_data)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController)  // アクションバーを押したときに、onSupportNavigateup()が呼ばれる

        binding.fab.setOnClickListener { view ->
            navController.navigate(R.id.action_to_dataEditFragment)
        }

        binding.mapButton.setOnClickListener {
            finish()
        }
    }

    // アクションバーを押すと呼ばれる
    override fun onSupportNavigateUp()
        = findNavController(R.id.nav_host_fragment_content_data).navigateUp()  // navigateUp: 前画面に画面遷移

    // fabボタンの表示切り替え
    fun setFabVisible(visibility: Int){
        binding.fab.visibility = visibility
    }
}