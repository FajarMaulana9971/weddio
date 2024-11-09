package com.weddio.weddio.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class ResponseData<T> {
	private T data;
	private String message;
}
