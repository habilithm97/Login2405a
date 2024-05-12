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
                /**
                 1. 최소 6자 이상 15자 이하의 길이
                 2. 적어도 하나의 알파벳 문자와 하나의 숫자를 포함
                 */
                val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,15}$"

                if(id.isEmpty()) { // 입력x
                    shortToast(getString(R.string.id_empty))
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
            // 닉네임 중복 확인
            btnNicCheck.setOnClickListener {
                val nickname = edtNick.text.toString()
                val nickParttern = "^[ㄱ-ㅣ가-힣]*$"

                if(nickname.isEmpty()) { // 입력x
                    shortToast(getString(R.string.nic_empty))
                } else { // 입력o
                    if(Pattern.matches(nickParttern, nickname)) { // 입력한 닉네임이 정규 표현식에 맞으면
                        val checkNick = db.checkNick(nickname)

                        if (!checkNick) { // 사용 가능 닉네임
                            _checkNick = true
                            shortToast(getString(R.string.nic_available))
                        } else { // 이미 있는 닉네임
                            shortToast(getString(R.string.nic_exist))
                        }
                    } else { // 사용 불가 닉네임
                        shortToast(getString(R.string.nic_unavailable))
                    }
                }
            }
            // 완료 버튼 클릭 시
            btnRegister.setOnClickListener {
                val id = edtId.text.toString()
                val pw = edtPw.text.toString()
                val cfPw = edtCfPw.text.toString()
                val nick = edtNick.text.toString()
                val phone = edtPhone.text.toString()
                val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,15}$"
                val phonePattern = "^(010|011|016|017|018|019)\\d{3,4}\\d{4}$"

                // 입력x
                if(id.isEmpty() || pw.isEmpty() || cfPw.isEmpty() || nick.isEmpty() || phone.isEmpty()) {
                    shortToast(getString(R.string.register_empty))
                } else { // 입력o
                    if(_checkId) { // 아이디 중복 확인o
                        if(Pattern.matches(pwPattern, pw)) { // 입력한 비밀번호가 패턴에 맞으면
                            if(pw == cfPw) { // 비밀번호 확인o
                                if(_checkNick) { // 닉네임 중복 확인o
                                    if (Pattern.matches(phonePattern, phone)) { // 입력한 휴대폰 번호가 패턴에 맞으면
                                        val insert = db.insert(id, pw, nick, phone)

                                        if(insert) { // 가입 성공
                                            shortToast(getString(R.string.register_success))
                                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                            startActivity(intent)
                                        } else { // 가입 실패
                                            shortToast(getString(R.string.register_fail))
                                        }
                                    } else { // 사용 불가 휴대폰 번호
                                        shortToast(getString(R.string.phone_unavailable))
                                    }
                                } else { // 닉네임 중복 확인x
                                    shortToast(getString(R.string.nick_check))
                                }
                            } else { // 비밀번호 확인x
                                shortToast(getString(R.string.cfpw_fail))
                            }
                        } else { // 사용 불가 비밀번호
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