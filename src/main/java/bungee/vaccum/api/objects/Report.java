/*
 * Developed by Paul Artjomow
 * Last update 10.06.20, 12:44
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.objects;

import bungee.vaccum.api.utils.ReportReason;

import java.sql.Timestamp;

public class Report {

    private String caseId;
    private String issuer;
    private String suspect;
    private String editor;
    private Timestamp creationTime;
    private ReportReason reportReason;

    public Report(String caseId, String suspect, String issuer, ReportReason reportReason, String editor, Timestamp creationTime) {
        this.caseId = caseId;
        this.suspect = suspect;
        this.issuer = issuer;
        this.reportReason = reportReason;
        this.editor = editor;
        this.creationTime = creationTime;
    }

    public String getEditor() { return editor; }

    public Timestamp getCreationTime() { return creationTime; }

    public String getCaseId() { return this.caseId; }

    public String getIssuer() {
        return this.issuer;
    }

    public String getSuspect() {
        return this.suspect;
    }

    public ReportReason getReportReason() {
        return this.reportReason;
    }

}
