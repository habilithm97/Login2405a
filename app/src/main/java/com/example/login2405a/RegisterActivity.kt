package com.example.login2405a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.login2405a.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var toast: Toast
    private val db by lazy { DBHelper(this@RegisterActivity) }
    private var _checkId: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            // 아이디 중복 확인
            btnIdCheck.setOnClickListener {
                val id = edtId.text.toString()
                /**
                 1. 최소 6자 이상 15자 이하의 길이
                 2. 적어도 하나의 알파벳 문자와 하나의 숫자를 포함
                 */
                val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,15}$"

                if(id.isEmpty()) { // 입력x
                    shortToast(getString(R.string.id_check))
                } else { // 입력o
                    if(Pattern.matches(idPattern, id)) { // 입력한 id가 정규 표현식 패턴에 맞으면
                        val checkId = db.checkId(id)

                        if(!checkId) { // 사용 가능 id
                            _checkId = true
                            shortToast(getString(R.string.id_available))
                        } else { // 이미 있는 id
                            shortToast(getString(R.string.id_exist))
                        }
                    } else { // 사용 불가 id
                        shortToast(getString(R.string.id_unavailable))
                    }
                }
            }
        }
    }
    // 토스트 메시지 중복 방지
    private fun shortToast(msg: String) {
        toast.cancel()
        toast = Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT)
        toast.show()
    }
}