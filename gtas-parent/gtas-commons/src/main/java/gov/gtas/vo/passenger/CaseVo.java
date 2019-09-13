/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.vo.passenger;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.gtas.model.*;
import gov.gtas.model.lookup.RuleCat;
import gov.gtas.services.CaseDispositionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class CaseVo {
    private static final Logger logger = LoggerFactory.getLogger(CaseVo.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private Long id;
    private Long passengerId;
    private String paxName;
    private String paxType;
    private Long paxId;
    private Long flightId;
    private String hitType;
    private Date flightETADate;
    private Date flightETDDate;
    private String flightDirection;
	private String lastName;
    private String firstName;
    private String middleName;
    private String nationality;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date dob;
    private String document;
    private Long highPriorityRuleCatId;
    private String flightNumber;
    private Date createdAt;
    private String status;
    private String description;
    private Set<HitsDisposition> hitsDispositions;
    private Set<HitsDispositionVo> hitsDispositionVos;
    private Set<GeneralCaseCommentVo> generalCaseCommentVos;
    private String caseOfficerStatus;
    private Boolean oneDayLookoutFlag;
    private Date currentTime;
    private String countDownTimeDisplay;
    private Date countdownTime;
    private String disposition;


    public String getCaseOfficerStatus() {
        return caseOfficerStatus;
    }

    public void setCaseOfficerStatus(String caseOfficerStatus) {
        this.caseOfficerStatus = caseOfficerStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Set<HitsDispositionVo> getHitsDispositionVos() {
        return hitsDispositionVos;
    }

    public void setHitsDispositionVos(Set<HitsDispositionVo> hitsDispositionVos) {
        this.hitsDispositionVos = hitsDispositionVos;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getHitType() {
		return hitType;
	}
	public void setHitType(String hitType) {
		this.hitType = hitType;
	}

    public Date getFlightETADate() {
        return flightETADate;
    }

    public void setFlightETADate(Date flightETADate) {
        this.flightETADate = flightETADate;
    }

    public Date getFlightETDDate() {
        return flightETDDate;
    }

    public void setFlightETDDate(Date flightETDDate) {
        this.flightETDDate = flightETDDate;
    }

    public Long getPassengerId() {
        return passengerId;
    }
    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }
    public Long getFlightId() {
        return flightId;
    }
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getStatus() {
        return status;
    }

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public String getFlightDirection() {
		return flightDirection;
	}

    public Long getPaxId() {
        return paxId;
    }

    public void setPaxId(Long paxId) {
        this.paxId = paxId;
    }

    public Long getHighPriorityRuleCatId() {
        return highPriorityRuleCatId;
    }

    public void setHighPriorityRuleCatId(Long highPriorityRuleCatId) {
        this.highPriorityRuleCatId = highPriorityRuleCatId;
    }

    public void setFlightDirection(String flightDirection) {
        this.flightDirection = flightDirection;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<HitsDisposition> getHitsDispositions() {
        return hitsDispositions;
    }

    public void setHitsDispositions(Set<HitsDisposition> hitsDispositions) {
        this.hitsDispositions = hitsDispositions;
    }

	public Boolean getOneDayLookoutFlag() {
		return oneDayLookoutFlag;
	}

	public void setOneDayLookoutFlag(Boolean oneDayLookoutFlag) {
		this.oneDayLookoutFlag = oneDayLookoutFlag;
	}

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getCountDownTimeDisplay() {
        return countDownTimeDisplay;
    }

    public void setCountDownTimeDisplay(String countDownTimeDisplay) {
        this.countDownTimeDisplay = countDownTimeDisplay;
    }

    public Date getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(Date countdownTime) {
        this.countdownTime = countdownTime;
    }

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}


    public Set<GeneralCaseCommentVo> getGeneralCaseCommentVos() {
        return generalCaseCommentVos;
    }

    public void setGeneralCaseCommentVos(Set<GeneralCaseCommentVo> generalCaseCommentVos) {
        this.generalCaseCommentVos = generalCaseCommentVos;
    }

    public static CaseVo from(Case aCase) {
        CaseVo aCaseVo = new CaseVo();
  //      aCaseVo.setHitsDispositions(aCase.getHitsDispositions());
        aCase.getFlight().setPnrs(null);
        aCase.getFlight().setApis(null);
        aCase.getFlight().setAddress(null);
        aCase.getFlight().setBags(null);
        aCase.getFlight().setCreditCard(null);
        aCase.getFlight().setPhone(null);
        aCase.getFlight().setBookingDetails(null);
        aCaseVo.setHitsDispositionVos(returnHitsDisposition(aCase.getHitsDispositions()));
   //     aCaseVo.setGeneralCaseCommentVos(convertCommentsToVo(aCase.getCaseComments()));
        CaseDispositionServiceImpl.copyIgnoringNullValues(aCase, aCaseVo);
 //       manageHitsDispositionCommentsAttachments(aCaseVo.getHitsDispositions());
        aCaseVo.setCreatedAt(aCase.getCreatedAt());
        return aCaseVo;
    }
    private static Set<HitsDispositionVo> returnHitsDisposition(Set<HitsDisposition> _tempHitsDispositionSet) {

        Set<HitsDispositionVo> _tempReturnHitsDispSet = new HashSet<HitsDispositionVo>();
        Set<RuleCat> _tempRuleCatSet = new HashSet<RuleCat>();
        HitsDispositionVo _tempHitsDisp = new HitsDispositionVo();
        RuleCat _tempRuleCat = new RuleCat();
        Set<AttachmentVo> _tempAttachmentVoSet = new HashSet<AttachmentVo>();
        List<HitsDispositionCommentsVo> _tempHitsDispCommentsVoSet;
        HitsDispositionCommentsVo _tempDispCommentsVo = new HitsDispositionCommentsVo();

        try {
            for (HitsDisposition hitDisp : _tempHitsDispositionSet) {
                _tempHitsDisp = new HitsDispositionVo();
                _tempRuleCat = new RuleCat();
                _tempHitsDispCommentsVoSet = new ArrayList<>();
                _tempAttachmentVoSet = new HashSet<AttachmentVo>();

                CaseDispositionServiceImpl.copyIgnoringNullValues(hitDisp, _tempHitsDisp);
                _tempHitsDisp.setHit_disp_id(hitDisp.getId());
                if (hitDisp.getRuleCat() != null) {
                    CaseDispositionServiceImpl.copyIgnoringNullValues(hitDisp.getRuleCat(), _tempRuleCat);
                    // _tempRuleCat.setHitsDispositions(null);
                }
                _tempRuleCatSet.add(_tempRuleCat);
                _tempHitsDisp.setCategory(_tempRuleCat.getCategory());
                _tempHitsDisp.setRuleCatSet(_tempRuleCatSet);

                // begin to retrieve attachments
                if (hitDisp.getDispComments() != null) {
                    Set<HitsDispositionComments> _tempDispCommentsSet = hitDisp.getDispComments();
                    for (HitsDispositionComments _tempComments : _tempDispCommentsSet) {
                        _tempDispCommentsVo = new HitsDispositionCommentsVo();
                        _tempAttachmentVoSet = new HashSet<AttachmentVo>();
                        CaseDispositionServiceImpl.copyIgnoringNullValues(_tempComments, _tempDispCommentsVo);
                        _tempHitsDispCommentsVoSet.add(_tempDispCommentsVo);

                        if (_tempComments.getAttachmentSet() != null) {

                            for (Attachment a : _tempComments.getAttachmentSet()) {
                                AttachmentVo attVo = new AttachmentVo();
                                // Turn blob into byte[], as input stream is not serializable
                                attVo.setContent(a.getContent().getBytes(1, (int) a.getContent().length()));
                                attVo.setId(a.getId());
                                attVo.setContentType(a.getContentType());
                                attVo.setDescription(a.getDescription());
                                attVo.setFilename(a.getFilename());
                                // Drop blob from being held in memory after each set
                                a.getContent().free();
                                // Add to attVoList to be returned to front-end
                                a.setPassenger(null);
                                _tempAttachmentVoSet.add(attVo);
                            }

                        }
                        _tempDispCommentsVo.setAttachmentSet(_tempAttachmentVoSet);
                    }
                    _tempHitsDispCommentsVoSet.sort(comparing(HitsDispositionCommentsVo::getCreatedAt).reversed());
                    _tempHitsDisp.setDispCommentsVo(new LinkedHashSet<>(_tempHitsDispCommentsVoSet));
                } // end

                _tempReturnHitsDispSet.add(_tempHitsDisp);
            }
        } catch (Exception ex) {
            logger.error("error returning hits disposition.", ex);
        }
        // _tempReturnHitsDispSet =
        // _tempReturnHitsDispSet.stream().sorted(Comparator.comparing(HitsDispositionVo::getHit_disp_id)).collect(Collectors.toSet());
        List<HitsDispositionVo> _tempArrList = _tempReturnHitsDispSet.stream()
                .sorted(Comparator.comparing(HitsDispositionVo::getHit_disp_id)).collect(Collectors.toList());
        return new HashSet<>(_tempReturnHitsDispSet.stream()
                .sorted(Comparator.comparing(HitsDispositionVo::getHit_disp_id)).collect(Collectors.toList()));
        // return _tempReturnHitsDispSet;
    }
    private static Set<GeneralCaseCommentVo> convertCommentsToVo(Set<CaseComment> caseComments) {
        List<GeneralCaseCommentVo> generalCaseCommentVoSet = new ArrayList<>();
        for (CaseComment cc : caseComments) {
            GeneralCaseCommentVo generalCaseCommentVo = new GeneralCaseCommentVo();
            CaseDispositionServiceImpl.copyIgnoringNullValues(cc, generalCaseCommentVo);
            generalCaseCommentVoSet.add(generalCaseCommentVo);
        }
        generalCaseCommentVoSet.sort(comparing(GeneralCaseCommentVo::getCreatedAt).reversed());
        return new LinkedHashSet<>(generalCaseCommentVoSet);
    }

    private static Set<HitsDispositionVo> manageHitsDispositionCommentsAttachments(
            Set<HitsDisposition> _tempHitsDispositionSet) {

        Set<HitsDispositionVo> _tempReturnHitsDispSet = new HashSet<HitsDispositionVo>();
        Set<Attachment> _tempAttachmentSet = new HashSet<Attachment>();
        HitsDispositionVo _tempHitsDisp = new HitsDispositionVo();
        RuleCat _tempRuleCat = new RuleCat();
        try {
            for (HitsDisposition hitDisp : _tempHitsDispositionSet) {
                _tempHitsDisp = new HitsDispositionVo();
                if (hitDisp.getDispComments() != null) {
                    Set<HitsDispositionComments> _tempDispCommentsSet = hitDisp.getDispComments();
                    for (HitsDispositionComments _tempComments : _tempDispCommentsSet) {
                        if (_tempComments.getAttachmentSet() != null) {

                            for (Attachment _tempAttach : _tempComments.getAttachmentSet()) {
                                _tempAttach.setPassenger(null);
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            logger.error("Error in manage hits disposition comments.", ex);
        }
        return _tempReturnHitsDispSet;
    }
}
