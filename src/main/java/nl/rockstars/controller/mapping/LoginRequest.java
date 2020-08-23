package nl.rockstars.controller.mapping;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@Schema(example="user@rockstars.nl")
	private String email;
	@Schema(example="password")
	private String password;
}

