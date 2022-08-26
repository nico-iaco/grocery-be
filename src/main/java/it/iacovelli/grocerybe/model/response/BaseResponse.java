package it.iacovelli.grocerybe.model.response;


public record BaseResponse<T>(T body, String errorMessage) {

}
