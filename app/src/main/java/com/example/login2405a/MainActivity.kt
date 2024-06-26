package com.example.login2405a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.login2405a.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var toast: Toast
    private val db by lazy { DBHelper(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        toast = Toast.makeText(this@MainActivity, "", Toast.LENGTH_SHORT)

        binding.apply {
            btnLogin.setOnClickListener {
                login()
            }
            btnRegister.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login() {
        val id = binding.edtId.text.toString()
        val pw = binding.edtPw.text.toString()

        if(id.isEmpty() || pw.isEmpty()) { // 입력x
            shortToast(getString(R.string.login_empty))
        } else { // 입력o
            val checkPw = db.checkPw(id, pw)
            if(checkPw) { // 정보 일치
                shortToast(getString(R.string.login_success))

                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                val nick = db.getNickById(id)
                intent.putExtra(DBHelper.ID, id)
                intent.putExtra(DBHelper.NICK, nick)
                startActivity(intent)
            } else { // 정보 불일치
                shortToast(getString(R.string.login_fail))
            }
        }
    }
    // 토스트 메시지 중복 방지
    private fun shortToast(msg: String) {
        toast.cancel()
        toast = Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT)
        toast.show()
    }
}