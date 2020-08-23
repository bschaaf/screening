package nl.rockstars.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rockstars.model.Song;


@Data
@XmlRootElement(name="songs")
@AllArgsConstructor
@NoArgsConstructor
public class SongsDTO{
    List<Song> song;
}