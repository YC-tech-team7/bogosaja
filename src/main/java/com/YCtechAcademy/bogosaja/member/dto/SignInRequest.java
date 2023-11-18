package com.YCtechAcademy.bogosaja.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record SignInRequest(String email, String password) {

}
