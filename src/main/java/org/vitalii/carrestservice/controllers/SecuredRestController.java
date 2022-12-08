package org.vitalii.carrestservice.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
public interface SecuredRestController {
}
