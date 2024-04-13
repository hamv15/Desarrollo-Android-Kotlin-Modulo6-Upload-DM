package com.hamv15.uploaddm.data

import com.hamv15.uploaddm.data.remote.FileApi
import com.hamv15.uploaddm.data.remote.RetrofitHelper
import com.hamv15.uploaddm.data.remote.model.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.create

class FileRepository {
    //Instancia al API
    private val fileApi: FileApi = RetrofitHelper.getRetrofit().create(FileApi::class.java)

    fun uploadImage(imagePart: MultipartBody.Part): Call<UploadResponseDto> {
        return fileApi.uploadImage(imagePart)
    }

}