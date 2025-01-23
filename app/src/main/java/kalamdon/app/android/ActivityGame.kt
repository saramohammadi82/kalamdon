package kalamdon.app.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kalamdon.app.android.databinding.ActivityGameBinding
import kalamdon.app.android.databinding.ActivityLevelBinding
import kalamdon.app.android.databinding.ActivityMainBinding
import kalamdon.app.android.databinding.ActivityStartBinding


class ActivityGame : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var heartContainer: LinearLayout
    private val hearts = mutableListOf<ImageView>() // لیست قلب‌ها

    private lateinit var textContainer: LinearLayout
    private lateinit var buttonContainer: GridLayout
    private lateinit var scoreTextView: TextView
    private val textViews = mutableListOf<TextView>()
    private val buttons = mutableListOf<Button>()

    private var targetWord = "تست"
    private var score = 5
    private var timer: CountDownTimer? = null
    private var totalTimeInMillis = 120000L // 120 ثانیه (2 دقیقه)
    private val intervalInMillis = 1000L // هر ثانیه به‌روزرسانی شود
    private var level = 1
    private lateinit var vibrator: Vibrator

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetWord = intent.extras!!.getString("word", "").reversed()
        level = intent.extras!!.getInt("level", 1)
        totalTimeInMillis = intent.extras!!.getInt("timer", 120) * 1000L

        textContainer = findViewById(R.id.textContainer)
        buttonContainer = findViewById(R.id.buttonContainer)
        scoreTextView = findViewById(R.id.scoreTextView)
        heartContainer = findViewById(R.id.heartContainer)
        hearts.add(findViewById(R.id.heart1))
        hearts.add(findViewById(R.id.heart2))
        hearts.add(findViewById(R.id.heart3))
        hearts.add(findViewById(R.id.heart4))
        hearts.add(findViewById(R.id.heart5))
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        setupTextViews()
        setupButtons()
        updateScore()
        startTimer()


    }

    private fun setupTextViews() {
        for (i in targetWord.indices) {
            val textView = TextView(this).apply {
                textSize = 24f
                setPadding(16, 16, 16, 16)
                text = "-"
            }
            textViews.add(textView)
            textContainer.addView(textView)
        }
    }


    private fun setupButtons() {
        val letters = targetWord.toCharArray().toMutableList()
        letters.shuffle() // حروف را به صورت تصادفی مرتب کن

        letters.forEach { letter ->
            val button = Button(this).apply {
                text = letter.toString()
                setOnClickListener {
                    onButtonClick(this)
                }
            }
            buttons.add(button)
            buttonContainer.addView(button)
        }
    }

    private fun onButtonClick(button: Button) {
        val letter = button.text.toString()
        // پیدا کردن اولین جای خالی از راست
        val firstEmptyIndex = textViews.indexOfLast { it.text == "-" }

        if (firstEmptyIndex != -1) {
            val expectedLetter = targetWord[firstEmptyIndex].toString()

            if (letter == expectedLetter) {
                textViews[firstEmptyIndex].text = letter
                textViews[firstEmptyIndex].setBackgroundColor(Color.GREEN)
                button.isEnabled = false // دکمه را غیرفعال کن

                // بررسی کن آیا همه‌ی حروف درست پر شده‌اند
                if (textViews.all { it.text != "-" }) {
                    SharedPreferencesHelper(this@ActivityGame).sharedPreferencesSave(SharedPreferencesHelper.KEY_INT_LEVEL, level)
                    endGame("شماره برنده شدید")
                }
            } else {
                // اعمال انیمیشن لرزش و ویبره
                shakeButton(button)
                vibratePhone()

                score--
                updateScore()
                if (score == 0) {
                    endGame("امتیاز های شما تمام شد")
                }
            }
        }
    }
    /*SharedPreferencesHelper(this@ActivityGame).sharedPreferencesSave(SharedPreferencesHelper.KEY_INT_LEVEL, level)
                    endGame("شماره برنده شدید")*/

    /*   endGame("امتیاز های شما تمام شد")*/
    private fun shakeButton(button: Button) {
        // اعمال انیمیشن لرزش
        val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shale)
        button.startAnimation(shakeAnimation)
    }

    private fun vibratePhone() {
        // بررسی آیا دستگاه از ویبره پشتیبانی می‌کند
        if (vibrator.hasVibrator()) {
            // ویبره به مدت 200 میلی‌ثانیه
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    private fun updateScore() {
        scoreTextView.text = "امتیاز: $score"
        if (score >= 0 && score < hearts.size) {
            // اعمال انیمیشن روی قلب مربوطه
            val heart = hearts[score]
            val animation = AnimationUtils.loadAnimation(this, R.anim.heart)
            heart.startAnimation(animation)

            // تغییر تصویر قلب به خالی پس از اتمام انیمیشن
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    heart.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTimeInMillis, intervalInMillis) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.timerTextView.text = "زمان باقی مانده: $secondsRemaining ثانیه"
            }

            override fun onFinish() {
                endGame("زمان تمام شد.")
            }
        }.start()
    }

    private fun endGame(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        buttons.forEach { it.isEnabled = false } // همه‌ی دکمه‌ها را غیرفعال کن
        onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // تایمر را هنگام از بین رفتن اکتیویتی متوقف کن
    }
}