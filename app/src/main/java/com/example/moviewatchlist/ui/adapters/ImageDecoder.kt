package com.example.moviewatchlist.ui.adapters

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ImageDecoder {
    fun decodeInto(imageView: ImageView, bytes: ByteArray?) {
        if (bytes == null) {
            imageView.setImageResource(android.R.color.darker_gray)
            return
        }

        val scope = (imageView.context as? androidx.lifecycle.LifecycleOwner)?.lifecycleScope
            ?: CoroutineScope(Dispatchers.Main)

        scope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }

            imageView.setImageBitmap(bitmap)
        }
    }
}
