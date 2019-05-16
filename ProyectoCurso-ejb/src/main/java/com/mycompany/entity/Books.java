/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author German
 */
@Entity
@Table(name = "books")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b"),
    @NamedQuery(name = "Books.findByBookID", query = "SELECT b FROM Books b WHERE b.bookID = :bookID"),
    @NamedQuery(name = "Books.findByBookTitle", query = "SELECT b FROM Books b WHERE b.bookTitle = :bookTitle"),
    @NamedQuery(name = "Books.findByCopyright", query = "SELECT b FROM Books b WHERE b.copyright = :copyright")})
public class Books implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BookID")
    private Integer bookID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "BookTitle")
    private String bookTitle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Copyright")
    @Temporal(TemporalType.TIMESTAMP)
    private Date copyright;
    @ManyToMany(mappedBy = "booksList")
    private List<Authors> authorsList;

    public Books() {
    }

    public Books(Integer bookID) {
        this.bookID = bookID;
    }

    public Books(Integer bookID, String bookTitle, Date copyright) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.copyright = copyright;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Date getCopyright() {
        return copyright;
    }

    public void setCopyright(Date copyright) {
        this.copyright = copyright;
    }

    @XmlTransient
    public List<Authors> getAuthorsList() {
        return authorsList;
    }

    public void setAuthorsList(List<Authors> authorsList) {
        this.authorsList = authorsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookID != null ? bookID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.bookID == null && other.bookID != null) || (this.bookID != null && !this.bookID.equals(other.bookID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.Books[ bookID=" + bookID + " ]";
    }
    
}
