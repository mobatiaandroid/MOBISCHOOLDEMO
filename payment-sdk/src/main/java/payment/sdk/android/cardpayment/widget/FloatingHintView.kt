package payment.sdk.android.cardpayment.widget

import payment.sdk.android.sdk.R
import android.content.Context
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat

internal class FloatingHintView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttrs) {

    private val hint: TextView = TextView(context).apply {
        setTextColor(ContextCompat.getColor(context, R.color.blue))
    }

    var text: String? = null
        set(value) {
            hint.text = value
        }

    init {
        addView(hint)
        layoutDirection = LayoutDirection.LTR
    }

    fun animateToAlignViewStart(v: View) {
        x = v.x
    }
}