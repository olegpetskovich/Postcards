package com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostcardsServer {
    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("categoryId")
    public int categoryId;

    @Expose
    @SerializedName("image")
    public String image;

    @Expose
    @SerializedName("numberLike")
    public int numberLike;

    @Expose
    @SerializedName("tags")
    public String tags;

    @Expose
    @SerializedName("isMyLike")
    public boolean isMyLike;

    @Expose
    @SerializedName("dataCreate")
    public String dataCreate;
}
