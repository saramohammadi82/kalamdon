package kalamdon.app.android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kalamdon.app.android.databinding.ActivityLevelBinding
import kalamdon.app.android.databinding.ActivityMainBinding


class ActivityLevels : AppCompatActivity() {

    private var levels: ArrayList<ModelLevel> = arrayListOf()
    private lateinit var adapterLevels: AdapterLevels
    private lateinit var binding: ActivityLevelBinding
    var currentLevel = "1"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentLevel = intent.getStringExtra("level").toString()

        adapterLevels = AdapterLevels(this@ActivityLevels,levels, object : AdapterLevels.Interaction {
            override fun onClick(item: ModelLevel, position: Int) {
                startActivity(
                    Intent(this@ActivityLevels, ActivityGame::class.java)
                        .putExtra("word", item.word)
                        .putExtra("level", item.level)
                        .putExtra("timer", item.timer)
                )
            }
        })

        binding.recyclerPlans.adapter = adapterLevels

    }


    override fun onResume() {
        super.onResume()
        loadLevelsAndScore()
    }

    private fun loadLevelsAndScore() {
        levels.clear()
        when (currentLevel) {
            "1" -> {
                levels.add(ModelLevel(1, "فارسی", 90))
                levels.add(ModelLevel(2, "ایران", 100))
                levels.add(ModelLevel(3, "کتاب", 150))
                levels.add(ModelLevel(4, "دانشگاه", 200))
                levels.add(ModelLevel(5, "هواپیما", 250))
                levels.add(ModelLevel(6, "پارلمان", 250))
                levels.add(ModelLevel(7, "تلسکوپ", 300))
                levels.add(ModelLevel(8, "آزمایشگاه", 300))
                levels.add(ModelLevel(9, "فلسفه", 300))
                levels.add(ModelLevel(10, "قسطنطنیه", 300))
            }

            "10" -> {
                levels.add(ModelLevel(11, "پارکینسون", 400))
                levels.add(ModelLevel(12, "آتشفشان", 400))
                levels.add(ModelLevel(13, "میکروسکوپ", 400))
                levels.add(ModelLevel(14, "انرژی", 400))
                levels.add(ModelLevel(15, "تاریخچه", 400))
                levels.add(ModelLevel(16, "فناوری", 400))
                levels.add(ModelLevel(17, "زیست‌شناسی", 500))
                levels.add(ModelLevel(18, "جغرافیا", 500))
                levels.add(ModelLevel(19, "عملیات", 500))
                levels.add(ModelLevel(20, "صنعتی", 500))
            }

            else -> {
                levels.add(ModelLevel(21, "دموکراسی", 550))
                levels.add(ModelLevel(22, "پارادایم", 550))
                levels.add(ModelLevel(23, "ساختارگرایی", 550))
                levels.add(ModelLevel(24, "پسامدرنیسم", 550))
                levels.add(ModelLevel(25, "سوفیان", 550))
                levels.add(ModelLevel(26, "متافیزیک", 550))
                levels.add(ModelLevel(27, "اپیستمولوژی", 550))
                levels.add(ModelLevel(28, "هرمنوتیک", 550))
                levels.add(ModelLevel(29, "پدیدارشناسی", 550))
                levels.add(ModelLevel(30, "کاتلین", 550))
            }
        }



        adapterLevels.notifyDataSetChanged()
    }

}
