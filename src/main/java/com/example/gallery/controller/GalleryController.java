package com.example.gallery.controller;

import com.example.gallery.dto.FileResponse;
import com.example.gallery.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class GalleryController {

    private final FileService service;

    public GalleryController(FileService service) {
        this.service = service;
    }

    // Upload
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {

        String filename = service.upload(file);

        return "http://localhost:8080/api/image/" + filename;
    }

    // Get All Photos
    @GetMapping("/photos")
    public List<FileResponse> all() throws Exception {
        return service.getAll();
    }

    // Search
    @GetMapping("/photos/search")
    public List<FileResponse> search(@RequestParam String name) throws Exception {
        return service.search(name);
    }

    // Sort by Name
    @GetMapping("/photos/sort/name")
    public List<FileResponse> sortName() throws Exception {
        return service.sortByName();
    }

    // Sort by Size
    @GetMapping("/photos/sort/size")
    public List<FileResponse> sortSize() throws Exception {
        return service.sortBySize();
    }

    // View Image
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> image(@PathVariable String filename)
            throws MalformedURLException {

        Path path = Paths.get("uploads").resolve(filename);

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    // Delete Image
    @DeleteMapping("/image/{filename:.+}")
    public String delete(@PathVariable String filename) throws Exception {

        service.delete(filename);

        return "Deleted Successfully";
    }
}