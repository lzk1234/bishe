package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("decision_task")
public class DecisionTaskEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public DecisionTaskEntity() {
    }

    public DecisionTaskEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String taskType;
    private String sourceType;
    private String sourceKey;
    private String merchantAccount;
    private String title;
    private String summary;
    private String actionSuggestion;
    private String priority;
    private String status;
    private String evidenceSnapshot;
    private String resultNote;
    private String ignoreReason;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date generatedAt;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date dueAt;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date handledAt;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getActionSuggestion() {
        return actionSuggestion;
    }

    public void setActionSuggestion(String actionSuggestion) {
        this.actionSuggestion = actionSuggestion;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvidenceSnapshot() {
        return evidenceSnapshot;
    }

    public void setEvidenceSnapshot(String evidenceSnapshot) {
        this.evidenceSnapshot = evidenceSnapshot;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public String getIgnoreReason() {
        return ignoreReason;
    }

    public void setIgnoreReason(String ignoreReason) {
        this.ignoreReason = ignoreReason;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public Date getHandledAt() {
        return handledAt;
    }

    public void setHandledAt(Date handledAt) {
        this.handledAt = handledAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
