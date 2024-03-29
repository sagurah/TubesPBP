package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.skyfishjy.library.RippleBackground


class FTOActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = getSharedPreferences("prefs", 0)
        val firstRun = settings.getBoolean("firstRun", false)

        if (firstRun == false)//if running for the first time
        //Splash will load for the first time
        {
            val editor = settings.edit()
            editor.putBoolean("firstRun", true)
            editor.commit()

            setContentView(R.layout.activity_ftoactivity)

            Handler().postDelayed(Runnable{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}




