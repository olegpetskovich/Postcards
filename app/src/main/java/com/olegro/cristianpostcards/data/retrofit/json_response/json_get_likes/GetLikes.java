package com.olegro.cristianpostcards.data.retrofit.json_response.json_get_likes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.olegro.cristianpostcards.data.retrofit.json_response.Response;

public class GetLikes extends Response {
    @Expose
    @SerializedName("successData")
    public int successData;

}
