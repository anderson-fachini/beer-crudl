package com.fachini.beercrudl.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fachini.beercrudl.entities.Beer;
import com.fachini.beercrudl.payload.UploadFileResponse;
import com.fachini.beercrudl.repositories.BeerRepository;
import com.fachini.beercrudl.services.FileStorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "File upload/download")
@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BeerRepository beerRepository;

    @ApiOperation("Upload a file")
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("beer_id") UUID beerId) {
        String fileName = fileStorageService.storeFile(file, beerId);

        String fileDownloadUri = ServletUriComponentsBuilder//
                .fromCurrentContextPath().path("/downloadFile/")//
                .path(fileName)//
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @ApiOperation("Download a file by name")
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        UUID beerId = UUID.fromString(fileName);
        Optional<Beer> optBeer = beerRepository.findById(beerId);

        if (optBeer.isPresent()) {
            // Load file as Resource
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            String contentType = optBeer.get().getImageType();

            return ResponseEntity.ok()//
                    .contentType(MediaType.parseMediaType(contentType))//
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }
}
