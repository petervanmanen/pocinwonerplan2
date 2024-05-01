package nl.commutr.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ontwikkelwens.
 */
@Entity
@Table(name = "ontwikkelwens")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ontwikkelwens implements Serializable {

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

    @Column(name = "omschrijving")
    private String omschrijving;

    @Column(name = "actief")
    private Boolean actief;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ontwikkelwens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aandachtspunt", "ontwikkelwens" }, allowSetters = true)
    private Set<Subdoel> subdoels = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_ontwikkelwens__aanbod",
        joinColumns = @JoinColumn(name = "ontwikkelwens_id"),
        inverseJoinColumns = @JoinColumn(name = "aanbod_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "activiteits", "aandachtspunts", "ontwikkelwens" }, allowSetters = true)
    private Set<Aanbod> aanbods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ontwikkelwens id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public Ontwikkelwens code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNaam() {
        return this.naam;
    }

    public Ontwikkelwens naam(String naam) {
        this.setNaam(naam);
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return this.omschrijving;
    }

    public Ontwikkelwens omschrijving(String omschrijving) {
        this.setOmschrijving(omschrijving);
        return this;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Boolean getActief() {
        return this.actief;
    }

    public Ontwikkelwens actief(Boolean actief) {
        this.setActief(actief);
        return this;
    }

    public void setActief(Boolean actief) {
        this.actief = actief;
    }

    public Set<Subdoel> getSubdoels() {
        return this.subdoels;
    }

    public void setSubdoels(Set<Subdoel> subdoels) {
        if (this.subdoels != null) {
            this.subdoels.forEach(i -> i.setOntwikkelwens(null));
        }
        if (subdoels != null) {
            subdoels.forEach(i -> i.setOntwikkelwens(this));
        }
        this.subdoels = subdoels;
    }

    public Ontwikkelwens subdoels(Set<Subdoel> subdoels) {
        this.setSubdoels(subdoels);
        return this;
    }

    public Ontwikkelwens addSubdoel(Subdoel subdoel) {
        this.subdoels.add(subdoel);
        subdoel.setOntwikkelwens(this);
        return this;
    }

    public Ontwikkelwens removeSubdoel(Subdoel subdoel) {
        this.subdoels.remove(subdoel);
        subdoel.setOntwikkelwens(null);
        return this;
    }

    public Set<Aanbod> getAanbods() {
        return this.aanbods;
    }

    public void setAanbods(Set<Aanbod> aanbods) {
        this.aanbods = aanbods;
    }

    public Ontwikkelwens aanbods(Set<Aanbod> aanbods) {
        this.setAanbods(aanbods);
        return this;
    }

    public Ontwikkelwens addAanbod(Aanbod aanbod) {
        this.aanbods.add(aanbod);
        return this;
    }

    public Ontwikkelwens removeAanbod(Aanbod aanbod) {
        this.aanbods.remove(aanbod);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ontwikkelwens)) {
            return false;
        }
        return getId() != null && getId().equals(((Ontwikkelwens) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ontwikkelwens{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", naam='" + getNaam() + "'" +
            ", omschrijving='" + getOmschrijving() + "'" +
            ", actief='" + getActief() + "'" +
            "}";
    }
}
