package nl.rockstars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;
import nl.rockstars.model.User;
import nl.rockstars.model.UserRepository;
import nl.rockstars.model.dto.SongDTO;
import nl.rockstars.service.MusicService;

@Log4j2
@SpringBootApplication
@EnableCaching
public class MusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * In the init method the songs are read and stored in the database. The artists are deduced from the songs.
	 * A demo user with encrypted password is created as well. See application.properties for the relevant 
	 * parameters.
	 */
	@Profile("!test")
	@Bean
	public CommandLineRunner init(@Value("${rockstars.baseUrl}") String rockstarsBaseUrl,
			@Value("${rockstars.songFile}") String songFile, PasswordEncoder passwordEncoder, MusicService musicService,
			UserRepository userRepository) {
		return args -> {
			var webClient = WebClient.builder().baseUrl(rockstarsBaseUrl).build();
			log.info("Reading rockstars song file..");
			webClient.get().uri('/' + songFile).retrieve().bodyToFlux(SongDTO.class)
					.filter(song -> song.getGenre().contains("Metal") && song.getYear() < 2016)
					.subscribe(song -> musicService.createSong(song));

			// demo user, encrypted password
			log.info("Adding demo user..");
			userRepository.save(new User("user@rockstars.nl", passwordEncoder.encode("password")));
		};
	}

}
