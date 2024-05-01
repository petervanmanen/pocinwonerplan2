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

    @Column(name = "subdoelen")
    private String subdoelen;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_aanbod__subdoel",
        joinColumns = @JoinColumn(name = "aanbod_id"),
        inverseJoinColumns = @JoinColumn(name = "subdoel_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aandachtspunt", "ontwikkelwens", "aanbods" }, allowSetters = true)
    private Set<Subdoel> subdoels = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "aanbods")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aanbods" }, allowSetters = true)
    private Set<Activiteit> activiteits = new HashSet<>();

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

    public String getSubdoelen() {
        return this.subdoelen;
    }

    public Aanbod subdoelen(String subdoelen) {
        this.setSubdoelen(subdoelen);
        return this;
    }

    public void setSubdoelen(String subdoelen) {
        this.subdoelen = subdoelen;
    }

    public Set<Subdoel> getSubdoels() {
        return this.subdoels;
    }

    public void setSubdoels(Set<Subdoel> subdoels) {
        this.subdoels = subdoels;
    }

    public Aanbod subdoels(Set<Subdoel> subdoels) {
        this.setSubdoels(subdoels);
        return this;
    }

    public Aanbod addSubdoel(Subdoel subdoel) {
        this.subdoels.add(subdoel);
        return this;
    }

    public Aanbod removeSubdoel(Subdoel subdoel) {
        this.subdoels.remove(subdoel);
        return this;
    }

    public Set<Activiteit> getActiviteits() {
        return this.activiteits;
    }

    public void setActiviteits(Set<Activiteit> activiteits) {
        if (this.activiteits != null) {
            this.activiteits.forEach(i -> i.removeAanbod(this));
        }
        if (activiteits != null) {
            activiteits.forEach(i -> i.addAanbod(this));
        }
        this.activiteits = activiteits;
    }

    public Aanbod activiteits(Set<Activiteit> activiteits) {
        this.setActiviteits(activiteits);
        return this;
    }

    public Aanbod addActiviteit(Activiteit activiteit) {
        this.activiteits.add(activiteit);
        activiteit.getAanbods().add(this);
        return this;
    }

    public Aanbod removeActiviteit(Activiteit activiteit) {
        this.activiteits.remove(activiteit);
        activiteit.getAanbods().remove(this);
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
            ", subdoelen='" + getSubdoelen() + "'" +
            "}";
    }
}
