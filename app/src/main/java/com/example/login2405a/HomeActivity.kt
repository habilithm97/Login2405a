package com.example.login2405a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login2405a.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        val id = intent.getStringExtra(DBHelper.ID)
        val nick = intent.getStringExtra(DBHelper.NICK)

        binding.tvUser.text = "$nick($id) 님"
    }
}