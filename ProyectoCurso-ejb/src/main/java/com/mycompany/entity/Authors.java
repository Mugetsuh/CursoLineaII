/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author German
 */
@Entity
@Table(name = "authors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Authors.findAll", query = "SELECT a FROM Authors a"),
    @NamedQuery(name = "Authors.findByAuthID", query = "SELECT a FROM Authors a WHERE a.authID = :authID"),
    @NamedQuery(name = "Authors.findByAuthFN", query = "SELECT a FROM Authors a WHERE a.authFN = :authFN"),
    @NamedQuery(name = "Authors.findByAuthMN", query = "SELECT a FROM Authors a WHERE a.authMN = :authMN"),
    @NamedQuery(name = "Authors.findByAuthLN", query = "SELECT a FROM Authors a WHERE a.authLN = :authLN")})
public class Authors implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "AuthID")
    private Integer authID;
    @Size(max = 20)
    @Column(name = "AuthFN")
    private String authFN;
    @Size(max = 20)
    @Column(name = "AuthMN")
    private String authMN;
    @Size(max = 20)
    @Column(name = "AuthLN")
    private String authLN;
    @JoinTable(name = "authorbook", joinColumns = {
        @JoinColumn(name = "AuthID", referencedColumnName = "AuthID")}, inverseJoinColumns = {
        @JoinColumn(name = "BookID", referencedColumnName = "BookID")})
    @ManyToMany
    private List<Books> booksList;

    public Authors() {
    }

    public Authors(Integer authID) {
        this.authID = authID;
    }

    public Integer getAuthID() {
        return authID;
    }

    public void setAuthID(Integer authID) {
        this.authID = authID;
    }

    public String getAuthFN() {
        return authFN;
    }

    public void setAuthFN(String authFN) {
        this.authFN = authFN;
    }

    public String getAuthMN() {
        return authMN;
    }

    public void setAuthMN(String authMN) {
        this.authMN = authMN;
    }

    public String getAuthLN() {
        return authLN;
    }

    public void setAuthLN(String authLN) {
        this.authLN = authLN;
    }

    @XmlTransient
    public List<Books> getBooksList() {
        return booksList;
    }

    public void setBooksList(List<Books> booksList) {
        this.booksList = booksList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authID != null ? authID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Authors)) {
            return false;
        }
        Authors other = (Authors) object;
        if ((this.authID == null && other.authID != null) || (this.authID != null && !this.authID.equals(other.authID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.Authors[ authID=" + authID + " ]";
    }
    
}
