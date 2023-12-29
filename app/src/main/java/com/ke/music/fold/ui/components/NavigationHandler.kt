package com.ke.music.fold.ui.components

import androidx.compose.runtime.staticCompositionLocalOf
import com.ke.music.fold.MainRoute


sealed interface NavigationAction {

    fun createPath(): String


    /**
     * 导航到歌手详情
     */
    data class NavigateToArtistDetail(private val artistId: Long) : NavigationAction {
        override fun createPath(): String {
            return MainRoute.ArtistDetail.createRoute(artistId)
        }
    }

    /**
     * 导航到mv详情
     */
    data class NavigateToMvDetail(private val mvId: Long) : NavigationAction {
        override fun createPath(): String {
            return MainRoute.MvDetail.createRoute(mvId)
        }
    }

    /**
     * 导航到专辑详情
     */
    data class NavigateToAlbumDetail(private val id: Long) : NavigationAction {
        override fun createPath(): String {
            return MainRoute.AlbumDetail.createRoute(id)
        }
    }

    /**
     * 导航到评论
     */
    data class NavigateToComment(val id: Long, val type: Int) : NavigationAction {
        override fun createPath(): String {
            return MainRoute.Comment.createRoute(id, type)
        }

    }

//    /**
//     * 导航到评论
//     */
//    data class NavigateToComments(
//        val commentType: CommentType,
//        val id: Long
//    ) : NavigationAction {
//
//        override fun createPath(): String {
//            return Screen.Comments.createMusicComment(commentType, id)
//        }
//    }
//
//    /**
//     * 导航到分享
//     */
//    data class NavigateToShare(
//        val shareType: ShareType,
//        val id: Long,
//        val title: String,
//        val subTitle: String,
//        val cover: String
//    ) : NavigationAction {
//        override fun createPath(): String {
//            return Screen.Share.createPath(shareType, id, title, subTitle, cover)
//        }
//    }
//
//    /**
//     * 导航到转机详情
//     */
//    data class NavigateToAlbumDetail(
//        val id: Long
//    ) : NavigationAction {
//        override fun createPath(): String {
//            return Screen.AlbumDetail.createPath(id)
//        }
//    }
//
//    /**
//     * 导航到用户歌单列表
//     */
//    data class NavigateToPlaylistList(val songId: Long) : NavigationAction {
//        override fun createPath(): String {
//            return Screen.PlaylistList.createPath(songId)
//        }
//    }
//
//    /**
//     * 网友精选碟
//     */
//    data class NavigateToPlaylistTop(val category: String = "全部") : NavigationAction {
//        override fun createPath(): String {
//            return Screen.PlaylistTop.createPath(category)
//        }
//    }
//
//    /**
//     * 精品歌单
//     */
//    object NavigateToHighqualityPlaylist : NavigationAction {
//        override fun createPath(): String {
//            return Screen.HighqualityPlaylist.route
//        }
//    }
//
//    object NavigateToDownloadedMusic : NavigationAction {
//        override fun createPath(): String {
//            return Screen.DownloadedMusic.route
//        }
//    }
//
//
//    object NavigateToDownloadingMusic : NavigationAction {
//        override fun createPath(): String {
//            return Screen.DownloadingMusic.route
//        }
//    }
//
//    object NavigateToRecommendSongs : NavigationAction {
//        override fun createPath(): String {
//            return Screen.RecommendSongs.route
//        }
//    }
//
//    object NavigateToAlbumSquare : NavigationAction {
//        override fun createPath(): String {
//            return Screen.AlbumSquare.route
//        }
//    }
//
//    object NavigateToArtistList : NavigationAction {
//        override fun createPath(): String {
//            return Screen.ArtistList.route
//        }
//    }
//
//    object NavigateToAllMv : NavigationAction {
//        override fun createPath(): String {
//            return Screen.AllMv.route
//        }
//    }
//
//    data class NavigateToArtistDetail(private val artistId: Long) : NavigationAction {
//        override fun createPath(): String {
//            return Screen.ArtistDetail.createPath(artistId)
//        }
//    }
//
//    object NavigateTpPlay : NavigationAction {
//        override fun createPath(): String {
//            return Screen.Play.route
//        }
//    }
}

val LocalNavigationHandler = staticCompositionLocalOf { NavigationHandler { } }

fun interface NavigationHandler {
    fun navigate(navigationAction: NavigationAction)
}