package ru.itis.pdfserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfReportData {

    protected String firstName;
    protected String surName;
    protected String patronymic;
    protected String group;

}
