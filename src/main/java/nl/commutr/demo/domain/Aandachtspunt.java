package nl.commutr.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aandachtspunt.
 */
@Entity
@Table(name = "aandachtspunt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aandachtspunt implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aandachtspunt")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aandachtspunt", "ontwikkelwens", "aanbods" }, allowSetters = true)
    private Set<Subdoel> subdoels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aandachtspunt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public Aandachtspunt code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNaam() {
        return this.naam;
    }

    public Aandachtspunt naam(String naam) {
        this.setNaam(naam);
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return this.omschrijving;
    }

    public Aandachtspunt omschrijving(String omschrijving) {
        this.setOmschrijving(omschrijving);
        return this;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Boolean getActief() {
        return this.actief;
    }

    public Aandachtspunt actief(Boolean actief) {
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
            this.subdoels.forEach(i -> i.setAandachtspunt(null));
        }
        if (subdoels != null) {
            subdoels.forEach(i -> i.setAandachtspunt(this));
        }
        this.subdoels = subdoels;
    }

    public Aandachtspunt subdoels(Set<Subdoel> subdoels) {
        this.setSubdoels(subdoels);
        return this;
    }

    public Aandachtspunt addSubdoel(Subdoel subdoel) {
        this.subdoels.add(subdoel);
        subdoel.setAandachtspunt(this);
        return this;
    }

    public Aandachtspunt removeSubdoel(Subdoel subdoel) {
        this.subdoels.remove(subdoel);
        subdoel.setAandachtspunt(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aandachtspunt)) {
            return false;
        }
        return getId() != null && getId().equals(((Aandachtspunt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aandachtspunt{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", naam='" + getNaam() + "'" +
            ", omschrijving='" + getOmschrijving() + "'" +
            ", actief='" + getActief() + "'" +
            "}";
    }
}
