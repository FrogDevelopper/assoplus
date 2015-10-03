/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "`option`")
public class Option implements Reference {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Integer id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "label", unique = false, nullable = false)
    private String label;

    @Column(name = "licence_code", nullable = false)
	String licenceCode;

    // ********************************** \\
    //            Getter & Setter         \\
    // ********************************** \\

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

	public String getLicenceCode() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}

    // ********************************** \\
    //           Overriden methods        \\
    // ********************************** \\

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("code", code)
                .append("label", label)
                .append("licenceCode", licenceCode)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        return new EqualsBuilder()
                .append(code, option.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .toHashCode();
    }
}
