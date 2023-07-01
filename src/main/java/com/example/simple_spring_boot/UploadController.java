package com.example.simple_spring_boot;

import com.example.simple_spring_boot.pdf_converter.PdfConverterException;
import com.example.simple_spring_boot.pdf_converter.PdfConverterService;
import com.example.simple_spring_boot.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class UploadController {
    Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final PdfConverterService pdfConverter;

    public UploadController(PdfConverterService pdfConverter) {
        this.pdfConverter = pdfConverter;
    }

    @GetMapping("/")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/img2pdf")
    @ResponseBody
    public DocumentRec convertImage(@RequestParam("file") MultipartFile image) {
        return pdfConverter.convert(image);
    }

    @GetMapping("/pdf/{idDocument:\\d+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String idDocument) {
        long id;
        try {
            id = Long.parseLong(idDocument);
        } catch (NumberFormatException exc) {
            throw new RuntimeException("Error at parsing id to long");
        }
        Resource file = pdfConverter.loadPdfFromId(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleExc(RuntimeException exc) {
        logger.error(exc.getMessage());
        return ResponseEntity.badRequest().body(exc.getMessage());
    }

}
