package com.example.controlpersonalizado


import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.customViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var view : View = LayoutInflater.from(context).inflate(R.layout.custom_view, this, true)
    private var imageView: ImageView
    private var titleTextView: TextView
    private var subtitleTextView: TextView

    private var imageDrawable : Drawable? = null
    private var title: String? = null
    private var subtitle: String? = null

    init {
        imageView = view.findViewById(R.id.imageView)
        titleTextView = view.findViewById(R.id.titleTextView)
        subtitleTextView = view.findViewById(R.id.subtitleTextView)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomView,
            defStyleAttr,
            0
        )

        try {
            imageDrawable = typedArray.getDrawable(R.styleable.CustomView_setImageDrawable)
            title = typedArray.getString(R.styleable.CustomView_setTitle)
            subtitle = typedArray.getString(R.styleable.CustomView_setSubTitle)

            imageView.setImageDrawable(imageDrawable)
            titleTextView.text = title
            subtitleTextView.text = subtitle
        }
        finally {
            typedArray.recycle()
        }


        imageView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        updateContentDescription()
    }

    private fun updateContentDescription() {
        this.contentDescription = "$title. $subtitle"
    }


    fun setTitle(text: String?) {
        title = text
        titleTextView.text = title
        updateContentDescription()
    }

    fun setSubtitle(text: String?) {
        subtitle = text
        subtitleTextView.text = subtitle
        updateContentDescription()
    }


    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.titleState = title
        savedState.subtitleState = subtitle
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setTitle(state.titleState)
            setSubtitle(state.subtitleState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var titleState: String? = null
        var subtitleState: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            titleState = source.readString()
            subtitleState = source.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(titleState)
            out.writeString(subtitleState)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}