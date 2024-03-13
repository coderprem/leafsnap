
package com.leafsnap;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @Expose
    private String author;
    @Expose
    private String bibliography;
    @SerializedName("common_name")
    private String commonName;
    @Expose
    private String family;
    @SerializedName("family_common_name")
    private Object familyCommonName;
    @Expose
    private String genus;
    @SerializedName("genus_id")
    private Long genusId;
    @Expose
    private Long id;
    @SerializedName("image_url")
    private String imageUrl;
    @Expose
    private Links links;
    @Expose
    private String rank;
    @SerializedName("scientific_name")
    private String scientificName;
    @Expose
    private String slug;
    @Expose
    private String status;
    @Expose
    private List<Object> synonyms;
    @Expose
    private Object year;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Object getFamilyCommonName() {
        return familyCommonName;
    }

    public void setFamilyCommonName(Object familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public Long getGenusId() {
        return genusId;
    }

    public void setGenusId(Long genusId) {
        this.genusId = genusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Object> synonyms) {
        this.synonyms = synonyms;
    }

    public Object getYear() {
        return year;
    }

    public void setYear(Object year) {
        this.year = year;
    }

}
