package com.ke.music.api

import com.ke.music.api.response.AlbumDetailResponse
import com.ke.music.api.response.ArtistDetailResponse
import com.ke.music.api.response.BaseListResponse
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.CommentResponse
import com.ke.music.api.response.KeyResponse
import com.ke.music.api.response.LoginResponse
import com.ke.music.api.response.MvDetailResponse
import com.ke.music.api.response.PlaylistDetailResponse
import com.ke.music.api.response.PlaylistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {


    /**
     * 获取登录所需要的二维码和key
     */
    @GET("/key")
    suspend fun key(): BaseResponse<KeyResponse>

    /**
     * 检查登录状态
     */
    @GET("/login/status")
    suspend fun loginStatus(): BaseResponse<Int>

    /**
     * 登录
     */
    @GET("/login")
    suspend fun login(@Query("key") key: String): BaseResponse<LoginResponse>

    /**
     * 获取当前用户的歌单
     */
    @GET("/user/current/playlists")
    suspend fun getCurrentUserPlaylists(): BaseResponse<List<PlaylistResponse>>

    /**
     * 获取歌单详情
     */
    @GET("/playlist/{id}")
    suspend fun getPlaylistDetail(@Path(value = "id") id: Long): BaseResponse<PlaylistDetailResponse>

    /**
     * 获取歌手详情
     */
    @GET("/artist/{id}")
    suspend fun getArtistDetail(@Path(value = "id") id: Long): BaseResponse<ArtistDetailResponse>

    /**
     * 获取mv信息
     */
    @GET("/mv/{id}")
    suspend fun getMvDetail(@Path(value = "id") id: Long): BaseResponse<MvDetailResponse>

    /**
     * 获取mv信息
     */
    @GET("/album/{id}")
    suspend fun getAlbumDetail(@Path(value = "id") id: Long): BaseResponse<AlbumDetailResponse>

    /**
     * 获取评论列表
     */
    @GET("/comment")
    suspend fun getComments(
        @Query("type")
        type: Int,
        @Query("id")
        id: Long,
        @Query("index")
        index: Int,
        @Query("pageSize")
        pageSize: Int = 50,
        @Query("cursor")
        cursor: Long = 0L,
    ): BaseListResponse<CommentResponse>

    /**
     * 获取子评论列表
     */
    @GET("/comment/{commentId}/child")
    suspend fun getChildComments(
        @Path("commentId") commentId: Long,
        @Query("type")
        type: Int,
        @Query("id")
        id: Long,
        @Query("pageSize")
        pageSize: Int = 50,
        @Query("time")
        time: Long = 0L,

        ): BaseListResponse<CommentResponse>
}