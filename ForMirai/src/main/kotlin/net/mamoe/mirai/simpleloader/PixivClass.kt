package net.mamoe.mirai.simpleloader

data class PixivClass(
    val `data`: List<PicData>,
    val message: String
)

data class PicData(
    val artistId: Int,
    val artistPreView: ArtistPreView,
    val caption: String,
    val createDate: String,
    val height: Int,
    val id: Int,
    val imageUrls: List<ImageUrl>,
    val pageCount: Int,
    val restrict: Int,
    val sanityLevel: Int,
    val tags: List<Tag>,
    val title: String,
    val tools: List<String>,
    val totalBookmarks: Int,
    val totalView: Int,
    val type: String,
    val width: Int,
    val xrestrict: Int
)

data class ArtistPreView(
    val account: String,
    val avatar: String,
    val id: Int,
    val name: String
)

data class ImageUrl(
    val large: String,
    val medium: String,
    val original: String,
    val squareMedium: String
)

data class Tag(
    val id: Int,
    val name: String,
    val translatedName: String
)