package com.sahelibeautyparlour.UlLayer.Home.Dataclass

data class DriveFilesResponse(
    val files: List<DriveFile>
)

data class DriveFile(
    val id: String,
    val name: String,
    val mimeType: String,
    val webContentLink: String
)
