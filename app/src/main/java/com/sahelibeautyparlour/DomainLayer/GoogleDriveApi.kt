package com.sahelibeautyparlour.DomainLayer

import com.sahelibeautyparlour.UlLayer.Home.Dataclass.DriveFilesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDriveApi {
    @GET("drive/v3/files")
    fun listFiles(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("fields") fields: String = "files(id,name,mimeType,webContentLink)"
    ): Call<DriveFilesResponse>
}
