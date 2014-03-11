package org.springframework.samples.petclinic.web;

/**
 * Created by akash on 3/11/14.
 */

import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.web.abstractview.AbstractPdfView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

public class VetPDFView extends AbstractPdfView {

    protected void buildPdfDocument(
            Map<String, Object> model,
            Document document,
            PdfWriter writer,
            HttpServletRequest req,
            HttpServletResponse resp)
            throws Exception {

        // Get data "vets" from model
        @SuppressWarnings("unchecked")
        Vets vets = (Vets) model.get("vets");
        List<Vet> vetList = vets.getVetList();
        // Fonts
        Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
        Font fontTag = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

        for (Vet vet : vetList) {

            // 1.Name
            document.add(new Chunk("Name: "));
            Chunk title = new Chunk(vet.getFirstName() + " " + vet.getLastName(), fontTitle);
            document.add(title);
            document.add(new Chunk(" "));

            // -- newline
            document.add(Chunk.NEWLINE);

            // 2.Specialities
            Chunk spl = null;
            document.add(new Chunk("Specialties: "));
            for (Specialty speciality : vet.getSpecialties()) {
                spl = new Chunk(speciality.getName(), fontTag);
                spl.setBackground(new BaseColor(72, 121, 145), 1f, 0.5f, 1f, 1.5f);
                document.add(spl);
                document.add(new Chunk(" "));

            }

            // -- newline
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

        }

    }
}
