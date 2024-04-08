package com.truongvu.blogrestapi.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ReportService {
    byte[] exportJasperReport() throws IOException, JRException;
}
