package com.hamv15.uploaddm.data.remote

import com.hamv15.uploaddm.data.remote.model.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

interface FileApi {

    @Multipart //Para que se pueda enviar el contenido por partes
    @POST("cm/games/upload.php")
    @Streaming //Para que no use toda la ram y se vaya cargando por partes
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<UploadResponseDto>
}