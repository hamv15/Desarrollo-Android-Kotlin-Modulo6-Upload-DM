package com.hamv15.uploaddm.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamv15.uploaddm.data.FileRepository
import com.hamv15.uploaddm.data.remote.ProgressRequestBody
import com.hamv15.uploaddm.data.remote.model.UploadResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(
    private val repository: FileRepository = FileRepository()
): ViewModel() {

    private val _message = MutableLiveData<String>() //version privada y mutable
    val message: LiveData<String> = _message //versión pública y de solo lectura

    private val _error = MutableLiveData<String>() //version privada y mutable
    val error: LiveData<String> = _error //versión pública y de solo lectura

    private val _progress = MutableLiveData<Int>() //version privada y mutable
    val progress: LiveData<Int> = _progress //versión pública y de solo lectura

    fun uploadImage(file: File){
        //Tranformando el file en parámetros multipartBody.part

        viewModelScope.launch(Dispatchers.IO) {
            val requestBody = file.asRequestBody()

            val progresRequestBody = ProgressRequestBody(requestBody){ progreso ->
                //Manejamos el valor del progreso
                _progress.postValue(progreso)

            }

            //Generamos el objeto MultipartBody.Part de la imagen
            val imagePart = MultipartBody.Part.createFormData(
                "image", //nombre del parámetro de la imagen en el script
                file.name,
                progresRequestBody
            )

            //Mandamos llamar la función correspondiente del repositorio
            val call: Call<UploadResponseDto> = repository.uploadImage(imagePart)

            call.enqueue(object: Callback<UploadResponseDto>{
                override fun onResponse(
                    call: Call<UploadResponseDto>,
                    response: Response<UploadResponseDto>
                ) {
                    Log.d("LOGSAPP", "Mensaje del server: ${response.body()?.message}")
                    _message.postValue(response.body()?.message)
                }

                override fun onFailure(call: Call<UploadResponseDto>, t: Throwable) {
                    //Manejo del error
                    _error.postValue(t.message)
                }

            })

        }

    }

}