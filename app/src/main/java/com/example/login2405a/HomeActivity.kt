package com.example.login2405a

import android.content.Intent
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

        binding.apply {
            tvUser.text = "$nick($id) 님"

            btnLogout.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout() {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}