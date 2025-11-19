package com.icc.practica11

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.icc.practica11.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import android.provider.MediaStore
import android.content.ContentValues
import android.graphics.Bitmap
import android.widget.Toast
import android.os.Build
import android.os.Environment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paint = binding.paintView

        binding.spTool.onItemSelectedListener = object : AdapterView.
                OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                paint.currentTool = PaintView.Tool.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

        //paleta de color
        binding.btnCBlack.setOnClickListener { applyColor(Color.BLACK) }
        binding.btnCRed.setOnClickListener { applyColor(0xFFF4436.toInt()) }
        binding.btnCGreen.setOnClickListener { applyColor(0xFF4CAF50.toInt()) }
        binding.btnCBlue.setOnClickListener { applyColor(0xFF2196F3.toInt()) }
        binding.btnCYellow.setOnClickListener { applyColor(0xFFFFEB3B.toInt()) }

        //mezcaldor RGB
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?,
                                           progress: Int,
                                           fromUser: Boolean) {
                val c = Color.rgb(binding.seekR.progress,
                    binding.seekG.progress,
                    binding.seekB.progress)
                applyColor(c)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        binding.seekR.setOnSeekBarChangeListener(listener)
        binding.seekG.setOnSeekBarChangeListener(listener)
        binding.seekB.setOnSeekBarChangeListener(listener)

        //grosor
        binding.seekStroke.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?,
                                           progress: Int,
                                           fromUser: Boolean) {
                paint.strokeWidthPx =
                    progress.coerceAtLeast(1).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //acciones
        binding.btnUndo.setOnClickListener { paint.undo() }
        binding.btnClear.setOnClickListener { paint.clearAll() }
        binding.btnSave.setOnClickListener { saveDrawing() }

    }

    private fun applyColor(color: Int){
        binding.paintView.currentColor = color
        binding.vColorPreview.setBackgroundColor(color)
    }
    private fun saveDrawing() {
        val bitmap = binding.paintView.exportToBitmap()

        CoroutineScope(Dispatchers.IO).launch {
            val success = saveBitmap(bitmap)

            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(this@MainActivity, "Imagen guardada en Galería", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error al guardar imagen", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveBitmap(bitmap: Bitmap): Boolean {
        val filename = "MiniPaint_${System.currentTimeMillis()}.png"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, filename)
                    put(MediaStore.Downloads.MIME_TYPE, "image/png")
                    put(MediaStore.Downloads.RELATIVE_PATH, "Download/MiniPaintPro")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val uri = contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: return false

                contentResolver.openOutputStream(uri)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                } ?: return false

                // Finalizar la escritura
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            } else {
                // Android 9 o menor – requiere permiso WRITE_EXTERNAL_STORAGE
                val dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ).toString() + "/MiniPaintPro"

                val folder = java.io.File(dir)
                if (!folder.exists()) folder.mkdirs()

                val file = java.io.File(folder, filename)
                val fos = java.io.FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()

                // Registrar para que aparezca
                android.media.MediaScannerConnection.scanFile(
                    this,
                    arrayOf(file.absolutePath),
                    null,
                    null
                )
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}
