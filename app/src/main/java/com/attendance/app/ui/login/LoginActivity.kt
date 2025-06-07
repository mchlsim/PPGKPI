package com.attendance.app.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.attendance.app.data.SessionManager
import com.attendance.app.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // Auto login as super admin
        sessionManager.setLoggedIn(true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
} 