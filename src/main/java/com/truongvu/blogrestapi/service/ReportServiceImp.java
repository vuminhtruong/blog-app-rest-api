package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {
    private final PostRepository postRepository;

    @Override
    public byte[] exportJasperReport() throws IOException, JRException {
        InputStream inputStream = new ClassPathResource("Post_01.jrxml").getInputStream();

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRDataSource dataSource = new JRBeanCollectionDataSource(postRepository.findAll());

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
