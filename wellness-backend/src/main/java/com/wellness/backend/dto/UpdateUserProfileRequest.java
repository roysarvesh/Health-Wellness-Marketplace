package com.wellness.backend.dto;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
	private Long id;
	private String name;
	private String email;
	private String role;
	private String bio;

}
