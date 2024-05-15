package com.example.login2405a

import android.content.Intent
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
    private var _checkNick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        toast = Toast.makeText(this@RegisterActivity, "", Toast.LENGTH_SHORT)

        binding.apply {
            // 아이디 중복 확인
            btnIdCheck.setOnClickListener {
                val id = edtId.text.toString()

                // 6~15자의 알파벳/숫자 조합으로 이루어진 패턴
                val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,15}$"

                if(id.isEmpty()) { // 입력x
                    shortToast(getString(R.string.id_empty))
                } else { // 입력o
                    if(Pattern.matches(idPattern, id)) { // 입력한 id가 패턴에 맞음
                        val checkId = db.checkId(id)

                        if(!checkId) { // 존재x
                            shortToast(getString(R.string.id_available))
                            _checkId = true
                        } else { // 존재o
                            shortToast(getString(R.string.id_exist))
                        }
                    } else { // 입력한 id가 패턴에 안 맞음
                        shortToast(getString(R.string.id_unavailable))
                    }
                }
            }
            // 닉네임 중복 확인
            btnNicCheck.setOnClickListener {
                val nick = edtNick.text.toString()
                val nickParttern = "^[ㄱ-ㅣ가-힣]*$"

                if(nick.isEmpty()) { // 입력x
                    shortToast(getString(R.string.nick_empty))
                } else { // 입력o
                    if(Pattern.matches(nickParttern, nick)) { // 입력한 닉네임이 패턴에 맞음
                        val checkNick = db.checkNick(nick)

                        if (!checkNick) { // 존재x
                            shortToast(getString(R.string.nick_available))
                            _checkNick = true
                        } else { // 존재o
                            shortToast(getString(R.string.nick_exist))
                        }
                    } else { // 입력한 닉네임이 패턴에 안 맞음
                        shortToast(getString(R.string.nick_unavailable))
                    }
                }
            }
            // 완료 버튼 클릭 시
            btnRegister.setOnClickListener {
                val id = edtId.text.toString()
                val pw = edtPw.text.toString()
                val cfPw = edtCfPw.text.toString()
                val nick = edtNick.text.toString()
                val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,15}$"

                // 입력x
                if(id.isEmpty() || pw.isEmpty() || cfPw.isEmpty() || nick.isEmpty()) {
                    shortToast(getString(R.string.register_empty))
                } else { // 입력o
                    if(_checkId) { // 아이디 중복 확인o
                        if(Pattern.matches(pwPattern, pw)) { // 입력한 비밀번호가 패턴에 맞으면
                            if(cfPw == pw) { // 비밀번호 확인o
                                if(_checkNick) { // 닉네임 중복 확인o
                                    val insert = db.insert(id, pw, nick)

                                    if(insert) { // 가입 성공
                                        shortToast(getString(R.string.register_success))
                                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    } else { // 가입 실패
                                        shortToast(getString(R.string.register_fail))
                                    }
                                } else { // 닉네임 중복 확인x
                                    shortToast(getString(R.string.nick_check))
                                }
                            } else { // 비밀번호 확인x
                                shortToast(getString(R.string.cfpw_fail))
                            }
                        } else { // 입력한 비밀번호가 패턴에 안 맞음
                            shortToast(getString(R.string.pw_unavailable))
                        }
                    } else { // 아이디 중복 확인x
                        shortToast(getString(R.string.id_check))
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