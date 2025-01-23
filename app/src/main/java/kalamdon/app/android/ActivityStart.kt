package kalamdon.app.android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kalamdon.app.android.databinding.ActivityLevelBinding
import kalamdon.app.android.databinding.ActivityMainBinding
import kalamdon.app.android.databinding.ActivityStartBinding


class ActivityStart : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStart.setOnClickListener {
            var level = 1
            when (binding.rdGroup.checkedRadioButtonId) {
                binding.rdEasy.id -> {
                    level = 1
                }

                binding.rdNormal.id -> {
                    level = 10
                }

                binding.rdHard.id -> {
                    level = 20
                }
            }
            val lastLevel = SharedPreferencesHelper(this@ActivityStart).sharedPreferencesLoad(SharedPreferencesHelper.KEY_INT_LEVEL, 1).toInt()

            if (level <= lastLevel)
                startActivity(Intent(this@ActivityStart, ActivityLevels::class.java).putExtra("level", level.toString()))
            else
                Toast.makeText(this@ActivityStart, "ابتدا مراحل قبل را تمام کنید.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        val lastLevel = SharedPreferencesHelper(this@ActivityStart).sharedPreferencesLoad(SharedPreferencesHelper.KEY_INT_LEVEL, 1).toInt()

        binding.rdEasy.setTextColor(Color.RED)
        binding.rdNormal.setTextColor(Color.RED)
        binding.rdHard.setTextColor(Color.RED)

        when (lastLevel) {
            in 0..9 -> {
                binding.rdEasy.setTextColor(Color.GREEN)
            }

            in 10..19 -> {
                binding.rdEasy.setTextColor(Color.GREEN)
                binding.rdNormal.setTextColor(Color.GREEN)
            }

            in 20..40 -> {
                binding.rdEasy.setTextColor(Color.GREEN)
                binding.rdNormal.setTextColor(Color.GREEN)
                binding.rdHard.setTextColor(Color.GREEN)
            }
        }

    }

}
