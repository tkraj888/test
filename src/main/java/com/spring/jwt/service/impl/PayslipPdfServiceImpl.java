package com.spring.jwt.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.spring.jwt.dto.PayslipDTO;
import com.spring.jwt.service.PayslipPdfService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class PayslipPdfServiceImpl implements PayslipPdfService {
    @Override
    public byte[] generatePayslipPdf(PayslipDTO dto) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36); // margins
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font headingFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font subHeadingFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // Header
            Paragraph title = new Paragraph("GTasterix IT Services Pvt. Ltd", headingFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph address = new Paragraph("Gera's Imperium Rise, Hinjawadi Phase II, Pune, India - 442203", subHeadingFont);
            address.setAlignment(Element.ALIGN_CENTER);
            address.setSpacingAfter(10);
            document.add(address);

            Paragraph payMonth = new Paragraph("Pay Slip for " + dto.getMonth() + " " + dto.getYear(), labelFont);
            payMonth.setAlignment(Element.ALIGN_CENTER);
            payMonth.setSpacingAfter(10);
            document.add(payMonth);

            // Employee info
            PdfPTable empTable = new PdfPTable(2);
            empTable.setWidthPercentage(100);
            empTable.setSpacingAfter(10);
            empTable.addCell(getCell("Name: " + dto.getName(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Employee ID: " + dto.getEmployeeId(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Designation: " + dto.getDesignation(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Bank Name: " + dto.getBankName(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Department: " + dto.getDepartment(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Account No: " + dto.getBankAccountNumber(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Location: " + dto.getLocation(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("PAN: " + dto.getPan(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("Date of Joining: " + dto.getDateOfJoining(), PdfPCell.ALIGN_LEFT));
            empTable.addCell(getCell("LOP: " + dto.getAbsentDays(), PdfPCell.ALIGN_LEFT));
            document.add(empTable);

            // Earnings table
            PdfPTable earnings = new PdfPTable(2);
            earnings.setWidthPercentage(100);
            earnings.setSpacingAfter(10);
            earnings.addCell(getBoldCell("Earnings", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getBoldCell("Amount (₹)", PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getCell("Basic", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getCell(String.format("%.2f", dto.getBasicPay()), PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getCell("HRA", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getCell(String.format("%.2f", dto.getHra()), PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getCell("DA", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getCell(String.format("%.2f", dto.getDa()), PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getCell("LTA", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getCell(String.format("%.2f", dto.getTravelAllowances()), PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getCell("Special Allowance", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getCell(String.format("%.2f", dto.getSpecialAllowances()), PdfPCell.ALIGN_RIGHT));
            earnings.addCell(getBoldCell("Total Earnings", PdfPCell.ALIGN_LEFT));
            earnings.addCell(getBoldCell(String.format("%.2f", dto.getTotalEarnings()), PdfPCell.ALIGN_RIGHT));
            document.add(earnings);

            // Deductions table
            PdfPTable deductions = new PdfPTable(2);
            deductions.setWidthPercentage(100);
            deductions.setSpacingAfter(10);
            deductions.addCell(getBoldCell("Deductions", PdfPCell.ALIGN_LEFT));
            deductions.addCell(getBoldCell("Amount (₹)", PdfPCell.ALIGN_RIGHT));
            deductions.addCell(getCell("Income Tax", PdfPCell.ALIGN_LEFT));
            deductions.addCell(getCell("0.00", PdfPCell.ALIGN_RIGHT));
            deductions.addCell(getCell("Provident Fund", PdfPCell.ALIGN_LEFT));
            deductions.addCell(getCell("0.00", PdfPCell.ALIGN_RIGHT));
            deductions.addCell(getCell("Professional Tax", PdfPCell.ALIGN_LEFT));
            deductions.addCell(getCell(String.format("%.2f", dto.getTotalDeductions()), PdfPCell.ALIGN_RIGHT));
            deductions.addCell(getBoldCell("Total Deductions", PdfPCell.ALIGN_LEFT));
            deductions.addCell(getBoldCell(String.format("%.2f", dto.getTotalDeductions()), PdfPCell.ALIGN_RIGHT));
            document.add(deductions);

            // Net Pay
            Paragraph netPay = new Paragraph("Net Pay for the Month: ₹ " + String.format("%.2f", dto.getNetSalary()), headingFont);
            netPay.setAlignment(Element.ALIGN_CENTER);
            netPay.setSpacingBefore(10);
            netPay.setSpacingAfter(10);
            document.add(netPay);

            Paragraph footer = new Paragraph("This is a system-generated payslip and does not require a signature.", valueFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    // Helpers for styling
    private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private PdfPCell getBoldCell(String text, int alignment) {
        Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, boldFont));
        cell.setPadding(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }


    //    @Override
//    public byte[] generatePayslipPdf(PayslipDTO dto) {
//        Document document = new Document();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        try {
//            PdfWriter.getInstance(document, baos);
//            document.open();
//
//            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
//
//            document.add(new Paragraph("GTasterix IT Services Pvt. Ltd", bold));
//            document.add(new Paragraph("Gera's Imperium Rise, Hinjawadi Phase II, Pune, India - 442203"));
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Pay Slip for " + dto.getMonth() + " " + dto.getYear(), bold));
//            document.add(new Paragraph(" "));
//
//            // Employee Info Table
//            PdfPTable empTable = new PdfPTable(2);
//            empTable.setWidthPercentage(100);
//            empTable.addCell("Name: " + dto.getName());
//            empTable.addCell("Employee ID: " + dto.getEmployeeId());
//            empTable.addCell("Designation: " + dto.getDesignation());
//            empTable.addCell("Bank Name: " + dto.getBankName());
//            empTable.addCell("Department: " + dto.getDepartment());
//            empTable.addCell("Account No: " + dto.getBankAccountNumber());
//            empTable.addCell("Location: " + dto.getLocation());
//            empTable.addCell("PAN: " + dto.getPan());
//            empTable.addCell("Date of Joining: " + dto.getDateOfJoining());
//            empTable.addCell("LOP: " + dto.getAbsentDays());
//
//            document.add(empTable);
//            document.add(new Paragraph(" "));
//
//            // Salary Table
//            PdfPTable salaryTable = new PdfPTable(2);
//            salaryTable.setWidthPercentage(100);
//            salaryTable.addCell(new Phrase("Earnings", bold));
//            salaryTable.addCell(new Phrase("Amount", bold));
//            salaryTable.addCell("Basic");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getBasicPay()));
//            salaryTable.addCell("HRA");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getHra()));
//            salaryTable.addCell("DA");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getDa()));
//            salaryTable.addCell("LTA");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getTravelAllowances()));
//            salaryTable.addCell("Special Allowance");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getSpecialAllowances()));
//            salaryTable.addCell("Total Earnings");
//            salaryTable.addCell("₹ " + String.format("%.2f", dto.getTotalEarnings()));
//
//            document.add(salaryTable);
//            document.add(new Paragraph(" "));
//
//            // Deductions
//            PdfPTable deductionTable = new PdfPTable(2);
//            deductionTable.setWidthPercentage(100);
//            deductionTable.addCell(new Phrase("Deductions", bold));
//            deductionTable.addCell(new Phrase("Amount", bold));
//            deductionTable.addCell("Income Tax");
//            deductionTable.addCell("₹ 0.00");
//            deductionTable.addCell("Provident Fund");
//            deductionTable.addCell("₹ 0.00");
//            deductionTable.addCell("Professional Tax");
//            deductionTable.addCell("₹ " + String.format("%.2f", dto.getTotalDeductions()));
//            deductionTable.addCell("Total Deductions");
//            deductionTable.addCell("₹ " + String.format("%.2f", dto.getTotalDeductions()));
//
//            document.add(deductionTable);
//            document.add(new Paragraph(" "));
//
//            // Net Pay
//            Paragraph netPay = new Paragraph("Net Pay for the Month: ₹ " + String.format("%.2f", dto.getNetSalary()), bold);
//            document.add(netPay);
//
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("This is a system-generated payslip and does not require a signature."));
//
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return baos.toByteArray();
//    }
@Override
public byte[] generatePayslipPdfFromHtml(String html) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.useFont(new File("src/main/resources/fonts/DejaVuSans.ttf"), "DejaVuSans");
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();
        return outputStream.toByteArray();
    } catch (Exception e) {
        throw new RuntimeException("PDF generation failed", e);
    }
}

}
