package nl.rockstars.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@XmlRootElement
@Entity
@Table(name = "artists")
@Builder(toBuilder = true)
public class Artist {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer artistId;
    @Getter
    private String name;
    @Getter
    private String genre;
    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER )
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonBackReference
    @Getter
    private Set<Song> songs = new HashSet<>();
}
