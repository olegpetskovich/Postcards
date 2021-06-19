package com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessData {
    @Expose
    @SerializedName("tags")
    public String[] tags;

    @Expose
    @SerializedName("cards")
    public PostcardsServer[] cards;
}
