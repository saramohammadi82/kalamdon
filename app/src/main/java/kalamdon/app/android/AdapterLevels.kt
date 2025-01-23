package kalamdon.app.android


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kalamdon.app.android.databinding.AdapterLevelBinding
import java.util.*

/**
 * Created by JavaDroid on 7/18/2015.
 */
class AdapterLevels(
    private val context: Context,
    private val items: ArrayList<ModelLevel>,
    private val delegate: Interaction,
) : RecyclerView.Adapter<AdapterLevels.MainHolder>() {

    interface Interaction {
        fun onClick(item: ModelLevel, position: Int)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(mainHolder: MainHolder, position: Int) {
        val item = items[position]


        mainHolder.binding.tvTitle.text = "مرحله " + item.level
        mainHolder.binding.tvBalance.text = "زمان:" + item.timer.toString() + " ثانیه "


        val level = SharedPreferencesHelper(App.context).sharedPreferencesLoad(SharedPreferencesHelper.KEY_INT_LEVEL, 1)

// تنظیم تصویر وضعیت مرحله
        if (item.level <= level.toInt()) {
            mainHolder.binding.imgState.setImageResource(R.drawable.baseline_done_outline_24)
        } else {
            mainHolder.binding.imgState.setImageResource(R.drawable.baseline_close_24)
        }

        mainHolder.binding.root.setOnClickListener {
            if (item.level <= level.toInt()) {
                // اگر مرحله قبلاً باز شده است
                showConfirmationDialog(item.level) {
                    // اگر کاربر تأیید کرد، به مرحله بروید
                    delegate.onClick(item, position)
                }
            } else if (item.level == level.toInt() + 1) {
                // اگر مرحله بعدی مجاز است
                delegate.onClick(item, position)
            } else {
                // اگر مرحله بعدی مجاز نیست
                showMessage("لطفاً مرحله ${level.toInt() + 1} را ابتدا تمام کنید.")
            }
        }


    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showConfirmationDialog(level: Int, onConfirm: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("تأیید")
            .setMessage("شما قبلاً به مرحله $level رفته‌اید. آیا می‌خواهید دوباره بروید؟")
            .setPositiveButton("بله") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("خیر") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MainHolder {
        val adapterView = R.layout.adapter_level
        val itemView = LayoutInflater.from(viewGroup.context).inflate(adapterView, viewGroup, false)
        return MainHolder(itemView)
    }

    class MainHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = AdapterLevelBinding.bind(v)

    }
}
