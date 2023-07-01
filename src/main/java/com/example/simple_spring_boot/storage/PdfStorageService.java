package com.example.simple_spring_boot.storage;

import com.example.simple_spring_boot.pdf_converter.PdfConverterException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PdfStorageService {

    final Path rootDir;
    final private AtomicLong id = new AtomicLong();
    private HashMap<Long, Path> pdfPaths;

    public PdfStorageService(StorageProperties properties) {
        rootDir = Path.of(properties.getRootDir());
        pdfPaths = new HashMap<>();
    }
    public void init() {
        try {
            Files.createDirectory(rootDir);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    public long storePdf(PDDocument doc, String pdfName) {
        long id = this.id.getAndIncrement();
        Path path = rootDir.resolve(pdfName);
        pdfPaths.put(id, path);
        try {
            doc.save(path.toString());
        } catch (IOException e) {
            throw new StorageException("Could not save created pdf", e);
        }
        return id;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootDir.toFile());
    }
    public Resource loadAsResource(long id) {
        Path path = pdfPaths.get(id);
        if (path == null) {
            throw new StorageException("No file exists with the provided id");
        }
        try {
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file: " + path.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + path.getFileName());
        }
    }
}
