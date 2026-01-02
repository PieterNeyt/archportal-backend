package be.kdg.ip3.archportal.profiles.domain.profile;


import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
@Getter
public class Section {
    private SectionType type;
    private Visibility  visibility;


    public Section(SectionType type, Visibility visibility) {
        setType(type);
        setVisibility(visibility);
    }

    public static Section createDefault(SectionType type) {
        return new Section(type,Visibility.FRIENDS );
    }
    public void setType(SectionType type) {
        if(type == null)
            throw new IllegalArgumentException("type must not be null");

        this.type = type;
    }

    public void setVisibility(Visibility visibility) {
        if(visibility == null)
            throw new IllegalArgumentException("visibility must not be null");

        this.visibility = visibility;
    }
}
