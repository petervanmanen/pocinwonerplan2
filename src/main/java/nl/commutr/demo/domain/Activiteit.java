package nl.commutr.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Activiteit.
 */
@Entity
@Table(name = "activiteit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activiteit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "naam")
    private String naam;

    @Column(name = "actiehouder")
    private String actiehouder;

    @Column(name = "afhandeltermijn")
    private Integer afhandeltermijn;

    @Column(name = "actief")
    private Boolean actief;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "activiteits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "activiteits", "aandachtspunts", "ontwikkelwens" }, allowSetters = true)
    private Set<Aanbod> aanbods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activiteit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public Activiteit code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNaam() {
        return this.naam;
    }

    public Activiteit naam(String naam) {
        this.setNaam(naam);
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getActiehouder() {
        return this.actiehouder;
    }

    public Activiteit actiehouder(String actiehouder) {
        this.setActiehouder(actiehouder);
        return this;
    }

    public void setActiehouder(String actiehouder) {
        this.actiehouder = actiehouder;
    }

    public Integer getAfhandeltermijn() {
        return this.afhandeltermijn;
    }

    public Activiteit afhandeltermijn(Integer afhandeltermijn) {
        this.setAfhandeltermijn(afhandeltermijn);
        return this;
    }

    public void setAfhandeltermijn(Integer afhandeltermijn) {
        this.afhandeltermijn = afhandeltermijn;
    }

    public Boolean getActief() {
        return this.actief;
    }

    public Activiteit actief(Boolean actief) {
        this.setActief(actief);
        return this;
    }

    public void setActief(Boolean actief) {
        this.actief = actief;
    }

    public Set<Aanbod> getAanbods() {
        return this.aanbods;
    }

    public void setAanbods(Set<Aanbod> aanbods) {
        if (this.aanbods != null) {
            this.aanbods.forEach(i -> i.removeActiviteit(this));
        }
        if (aanbods != null) {
            aanbods.forEach(i -> i.addActiviteit(this));
        }
        this.aanbods = aanbods;
    }

    public Activiteit aanbods(Set<Aanbod> aanbods) {
        this.setAanbods(aanbods);
        return this;
    }

    public Activiteit addAanbod(Aanbod aanbod) {
        this.aanbods.add(aanbod);
        aanbod.getActiviteits().add(this);
        return this;
    }

    public Activiteit removeAanbod(Aanbod aanbod) {
        this.aanbods.remove(aanbod);
        aanbod.getActiviteits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activiteit)) {
            return false;
        }
        return getId() != null && getId().equals(((Activiteit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activiteit{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", naam='" + getNaam() + "'" +
            ", actiehouder='" + getActiehouder() + "'" +
            ", afhandeltermijn=" + getAfhandeltermijn() +
            ", actief='" + getActief() + "'" +
            "}";
    }
}
