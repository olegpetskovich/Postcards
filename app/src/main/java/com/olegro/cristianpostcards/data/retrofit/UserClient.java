package com.olegro.cristianpostcards.data.retrofit;

import com.olegro.cristianpostcards.data.retrofit.json_response.json_authorization.Authorization;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_likes.GetLikes;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.GetPostcards;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserClient {
//    @FormUrlEncoded
    @GET("auth")
    Call<Authorization> authorization(@Query("device_key") String deviceKey, @Query("fb_token") String fbToken);

//    @FormUrlEncoded
    @GET("cards")
    Call<GetPostcards> getPostcards(@Query("token") String token,
                                    @Query("idCategory") int idCategory,
                                    @Query("isSortNew") boolean isSortNew,
                                    @Query("page") int page,
                                    @Query("tag") String tag);

//    @FormUrlEncoded
    @GET("like")
    Call<GetLikes> setCardLike(@Query("token") String token, @Query("id_card") int idCard);

    @GET("cardsLiked")
    Call<GetPostcards> getLikedCards(@Query("token") String token);
}
