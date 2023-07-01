package com.example.simple_spring_boot.pdf_converter;

import com.example.simple_spring_boot.DocumentRec;
import com.example.simple_spring_boot.storage.PdfStorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class PdfConverterService {
    private final PdfStorageService storage;
    @Autowired
    public PdfConverterService(PdfStorageService storage) {
        this.storage = storage;
    }

    private long storePdf(String pdfFileName, BufferedImage bufferedImage) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);

            try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                float xScale = page.getBleedBox().getWidth() / pdImage.getWidth();
                float yScale = page.getBleedBox().getHeight() / pdImage.getHeight();
                float scale = xScale < yScale ? xScale : yScale;
                float yCoord = page.getBleedBox().getHeight() - pdImage.getHeight() * scale;
                contents.drawImage(pdImage, 0, yCoord, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            }
            long generated_id = storage.storePdf(doc, pdfFileName);
            return generated_id;
        } catch (IOException e) {
            throw new PdfConverterException("Couldn't create pdf document", e);
        }
    }
    public DocumentRec convert(MultipartFile imgFile) {
        if (imgFile.isEmpty() || !imgFile.getContentType().contains("image")) {
            throw new PdfConverterException("The file uploaded is invalid");
        }
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(imgFile.getInputStream());
        } catch (IOException e) {
            throw new PdfConverterException("Conversion to BufferedImage failed", e);
        }
        String imgName = imgFile.getOriginalFilename();
        int lastDot = imgName.lastIndexOf('.');
        if (lastDot != -1) {
            imgName = imgName.substring(0, lastDot);
        }
        long generatedId = storePdf(imgName + ".pdf", bufferedImage);
        return new DocumentRec(generatedId);
    }

    public Resource loadPdfFromId(long id) {
        return storage.loadAsResource(id);
    }
}
