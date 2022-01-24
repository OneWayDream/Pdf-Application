package ru.itis.pdfserver.controllers;

import com.squareup.okhttp.internal.http.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.pdfserver.amqp.AmqpClient;
import ru.itis.pdfserver.dto.PdfReportData;
import ru.itis.pdfserver.exceptions.AuthorizationFaultException;
import ru.itis.pdfserver.exceptions.ResponseException;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
@RequestMapping("/get-pdf")
public class PdfRequestController {

    protected final AmqpClient client;

    @PostMapping(
            value = "/javalab"
    )
    public ResponseEntity<?> getJavaLabReport(@RequestBody PdfReportData reportData){

        try{
            byte[] contents = client.handleJavaLabRequest(reportData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "JavaLab Report.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (ResponseException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong...");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Something went wrong...");
        }

    }

    @PostMapping(
            value = "/linux"
    )
    public ResponseEntity<?> getLinuxReport(@RequestBody PdfReportData reportData){
        try{
            byte[] contents = client.handleLinuxRequest(reportData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "Linux Report.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (ResponseException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong...");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Something went wrong...");
        }

    }

}
