package nl.commutr.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subdoel.
 */
@Entity
@Table(name = "subdoel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subdoel implements Serializable {

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

    @Column(name = "actief")
    private Boolean actief;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subdoels" }, allowSetters = true)
    private Aandachtspunt aandachtspunt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subdoels" }, allowSetters = true)
    private Ontwikkelwens ontwikkelwens;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subdoels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subdoels", "activiteits" }, allowSetters = true)
    private Set<Aanbod> aanbods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subdoel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public Subdoel code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNaam() {
        return this.naam;
    }

    public Subdoel naam(String naam) {
        this.setNaam(naam);
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Boolean getActief() {
        return this.actief;
    }

    public Subdoel actief(Boolean actief) {
        this.setActief(actief);
        return this;
    }

    public void setActief(Boolean actief) {
        this.actief = actief;
    }

    public Aandachtspunt getAandachtspunt() {
        return this.aandachtspunt;
    }

    public void setAandachtspunt(Aandachtspunt aandachtspunt) {
        this.aandachtspunt = aandachtspunt;
    }

    public Subdoel aandachtspunt(Aandachtspunt aandachtspunt) {
        this.setAandachtspunt(aandachtspunt);
        return this;
    }

    public Ontwikkelwens getOntwikkelwens() {
        return this.ontwikkelwens;
    }

    public void setOntwikkelwens(Ontwikkelwens ontwikkelwens) {
        this.ontwikkelwens = ontwikkelwens;
    }

    public Subdoel ontwikkelwens(Ontwikkelwens ontwikkelwens) {
        this.setOntwikkelwens(ontwikkelwens);
        return this;
    }

    public Set<Aanbod> getAanbods() {
        return this.aanbods;
    }

    public void setAanbods(Set<Aanbod> aanbods) {
        if (this.aanbods != null) {
            this.aanbods.forEach(i -> i.removeSubdoel(this));
        }
        if (aanbods != null) {
            aanbods.forEach(i -> i.addSubdoel(this));
        }
        this.aanbods = aanbods;
    }

    public Subdoel aanbods(Set<Aanbod> aanbods) {
        this.setAanbods(aanbods);
        return this;
    }

    public Subdoel addAanbod(Aanbod aanbod) {
        this.aanbods.add(aanbod);
        aanbod.getSubdoels().add(this);
        return this;
    }

    public Subdoel removeAanbod(Aanbod aanbod) {
        this.aanbods.remove(aanbod);
        aanbod.getSubdoels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subdoel)) {
            return false;
        }
        return getId() != null && getId().equals(((Subdoel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subdoel{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", naam='" + getNaam() + "'" +
            ", actief='" + getActief() + "'" +
            "}";
    }
}
