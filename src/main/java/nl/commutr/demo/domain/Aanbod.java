package nl.commutr.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aanbod.
 */
@Entity
@Table(name = "aanbod")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aanbod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "naam")
    private String naam;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_aanbod__activiteit",
        joinColumns = @JoinColumn(name = "aanbod_id"),
        inverseJoinColumns = @JoinColumn(name = "activiteit_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aanbods" }, allowSetters = true)
    private Set<Activiteit> activiteits = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "aanbods")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subdoels", "aanbods" }, allowSetters = true)
    private Set<Aandachtspunt> aandachtspunts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "aanbods")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subdoels", "aanbods" }, allowSetters = true)
    private Set<Ontwikkelwens> ontwikkelwens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aanbod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return this.naam;
    }

    public Aanbod naam(String naam) {
        this.setNaam(naam);
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Set<Activiteit> getActiviteits() {
        return this.activiteits;
    }

    public void setActiviteits(Set<Activiteit> activiteits) {
        this.activiteits = activiteits;
    }

    public Aanbod activiteits(Set<Activiteit> activiteits) {
        this.setActiviteits(activiteits);
        return this;
    }

    public Aanbod addActiviteit(Activiteit activiteit) {
        this.activiteits.add(activiteit);
        return this;
    }

    public Aanbod removeActiviteit(Activiteit activiteit) {
        this.activiteits.remove(activiteit);
        return this;
    }

    public Set<Aandachtspunt> getAandachtspunts() {
        return this.aandachtspunts;
    }

    public void setAandachtspunts(Set<Aandachtspunt> aandachtspunts) {
        if (this.aandachtspunts != null) {
            this.aandachtspunts.forEach(i -> i.removeAanbod(this));
        }
        if (aandachtspunts != null) {
            aandachtspunts.forEach(i -> i.addAanbod(this));
        }
        this.aandachtspunts = aandachtspunts;
    }

    public Aanbod aandachtspunts(Set<Aandachtspunt> aandachtspunts) {
        this.setAandachtspunts(aandachtspunts);
        return this;
    }

    public Aanbod addAandachtspunt(Aandachtspunt aandachtspunt) {
        this.aandachtspunts.add(aandachtspunt);
        aandachtspunt.getAanbods().add(this);
        return this;
    }

    public Aanbod removeAandachtspunt(Aandachtspunt aandachtspunt) {
        this.aandachtspunts.remove(aandachtspunt);
        aandachtspunt.getAanbods().remove(this);
        return this;
    }

    public Set<Ontwikkelwens> getOntwikkelwens() {
        return this.ontwikkelwens;
    }

    public void setOntwikkelwens(Set<Ontwikkelwens> ontwikkelwens) {
        if (this.ontwikkelwens != null) {
            this.ontwikkelwens.forEach(i -> i.removeAanbod(this));
        }
        if (ontwikkelwens != null) {
            ontwikkelwens.forEach(i -> i.addAanbod(this));
        }
        this.ontwikkelwens = ontwikkelwens;
    }

    public Aanbod ontwikkelwens(Set<Ontwikkelwens> ontwikkelwens) {
        this.setOntwikkelwens(ontwikkelwens);
        return this;
    }

    public Aanbod addOntwikkelwens(Ontwikkelwens ontwikkelwens) {
        this.ontwikkelwens.add(ontwikkelwens);
        ontwikkelwens.getAanbods().add(this);
        return this;
    }

    public Aanbod removeOntwikkelwens(Ontwikkelwens ontwikkelwens) {
        this.ontwikkelwens.remove(ontwikkelwens);
        ontwikkelwens.getAanbods().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aanbod)) {
            return false;
        }
        return getId() != null && getId().equals(((Aanbod) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aanbod{" +
            "id=" + getId() +
            ", naam='" + getNaam() + "'" +
            "}";
    }
}
