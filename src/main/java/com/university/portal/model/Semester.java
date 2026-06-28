package com.university.portal.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SEMESTERS")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sem_gen")
    @SequenceGenerator(name = "sem_gen", sequenceName = "SEM_SEQ", allocationSize = 1)
    @Column(name = "SEM_ID")
    private Long semId;

    @Column(name = "SEM_NAME", nullable = false, length = 50)
    private String semName;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    // Getters and Setters
    public Long getSemId() { return semId; }
    public void setSemId(Long semId) { this.semId = semId; }
    public String getSemName() { return semName; }
    public void setSemName(String semName) { this.semName = semName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
