package com.example.gallery.service;

import com.example.gallery.dto.FileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileService {

    private final Path root = Paths.get("uploads");

    public FileService() throws IOException {
        Files.createDirectories(root);
    }

    // Upload File
    public String upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                root.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
        );

        return filename;
    }

    // Get All Files
    public List<FileResponse> getAll() throws IOException {

        List<FileResponse> list = new ArrayList<>();

        try (Stream<Path> paths = Files.list(root)) {

            paths.forEach(path -> {
                try {
                    list.add(new FileResponse(
                            path.getFileName().toString(),
                            "http://localhost:8080/api/image/" + path.getFileName(),
                            Files.size(path)
                    ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return list;
    }

     // Search
    public List<FileResponse> search(String name) throws IOException {

        List<FileResponse> result = new ArrayList<>();

        try (Stream<Path> paths = Files.list(root)) {

            paths.filter(path ->
                    path.getFileName().toString().toLowerCase()
                            .contains(name.toLowerCase())
            ).forEach(path -> {
                try {
                    result.add(new FileResponse(
                            path.getFileName().toString(),
                            "http://localhost:8080/api/image/" + path.getFileName(),
                            Files.size(path)
                    ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return result;
    }

    // Delete
    public void delete(String filename) throws IOException {
        Files.deleteIfExists(root.resolve(filename));
    }

    // Sort by Name
    public List<FileResponse> sortByName() throws IOException {

        List<FileResponse> files = getAll();

        files.sort(Comparator.comparing(FileResponse::getName));

        return files;
    }

    // Sort by Size
    public List<FileResponse> sortBySize() throws IOException {

        List<FileResponse> files = getAll();

        files.sort(Comparator.comparingLong(FileResponse::getSize).reversed());

        return files;
    }
}