package gov.gtas.services.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import gov.gtas.enumtype.SignupRequestStatus;

public class SignupRequestDTO {

	private Long id;

	@NotNull
	@NotEmpty
	private String username;
	
	private String firstName;
	private String lastName;
	
	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String supervisor;

	@NotNull
	private Long physicalLocationId;
	private String physicalLocation;

	private SignupRequestStatus status;

	private String reviewedBy;
	private Date reviewedDate;
	private String createdBy;
	private String updatedBy;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public Long getPhysicalLocationId() {
		return physicalLocationId;
	}

	public void setPhysicalLocationId(Long physicalLocationId) {
		this.physicalLocationId = physicalLocationId;
	}

	public String getPhysicalLocation() {
		return physicalLocation;
	}

	public void setPhysicalLocation(String physicalLocation) {
		this.physicalLocation = physicalLocation;
	}

	public SignupRequestStatus getStatus() {
		return status;
	}

	public void setStatus(SignupRequestStatus status) {
		this.status = status;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
}