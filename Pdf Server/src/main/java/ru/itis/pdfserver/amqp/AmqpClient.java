package ru.itis.pdfserver.amqp;

import ru.itis.pdfserver.dto.PdfReportData;

public interface AmqpClient {

    byte[] handleJavaLabRequest(PdfReportData reportData);

    byte[] handleLinuxRequest(PdfReportData reportData);

}
