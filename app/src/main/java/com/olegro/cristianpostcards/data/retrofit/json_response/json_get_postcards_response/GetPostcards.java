package com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.olegro.cristianpostcards.data.retrofit.json_response.Response;

public class GetPostcards extends Response {
    @Expose
    @SerializedName("successData")
    public SuccessData successData;
}