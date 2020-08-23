package nl.rockstars.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rockstars.model.Artist;

@Data
@XmlRootElement(name="artists")
@AllArgsConstructor
@NoArgsConstructor
public class ArtistsDTO{
    List<Artist> artist;
}