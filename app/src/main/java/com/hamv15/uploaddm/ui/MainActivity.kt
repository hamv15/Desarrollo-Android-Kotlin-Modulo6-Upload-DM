package com.hamv15.uploaddm.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.hamv15.uploaddm.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)

    }

    fun click(view: View){
        val fileName = "kratos.JPG" //Nombre del archivo a subir

        //Obteniendo la extensión del archivo a subir
        val fileExt = fileName.substringAfterLast(".", "")

        //Creamos una copia del archivo en el directorio caché
        //Esto se hace por si queremos hacer un procesamiento adicional
        val file = File(cacheDir, "HugoMeza.$fileExt")

        file.createNewFile()

        file.outputStream().use {fos ->

            assets.open(fileName).copyTo(fos)

        }
        mainViewModel.uploadImage(file)

        //Suscribimos al contenedor livedata correspondiente
        mainViewModel.message.observe(this, Observer { message ->

            binding.button.isEnabled = false

            //Desaparecen al cargar el archivo
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvProgress.visibility = View.INVISIBLE

            AlertDialog.Builder(this)
                .setTitle("Aviso")
                .setMessage(message)
                .setPositiveButton("Aceptar"){dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        })

        mainViewModel.progress.observe(this, Observer { progress ->

            binding.apply {
                progressBar.visibility = View.VISIBLE
                tvProgress.visibility = View.VISIBLE
                progressBar.progress = progress
                tvProgress.text = "${progress.toString()} %"
            }
        })


    }

}