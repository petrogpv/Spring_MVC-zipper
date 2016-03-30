package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    private static Map<Long, ZipEntity> mapResponse = new HashMap<>();


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/{keyMap}", method = RequestMethod.GET)
    public String onKey(@PathVariable (value = "keyMap") Long keyMap, Model model) {
            if(keyMap==0){
                model.addAttribute("answer", "Choose files!!!");
            }
            else if(!mapResponse.containsKey(keyMap)) {
                model.addAttribute("answer", "Incorrect request!!!");
            }
            else
            {
                model.addAttribute("key", keyMap);
                model.addAttribute("names", mapResponse.get(keyMap).getListFilesNames());
                model.addAttribute("size", mapResponse.get(keyMap).getResponseEntity().getBody().length);
            }
        return "index";
    }

    @RequestMapping(value = "/common", method = RequestMethod.POST)
    public ResponseEntity processForm (@RequestParam("file") MultipartFile[] files, Model model) throws IOException, URISyntaxException {
        int countFiles = 0;
        for (MultipartFile f : files) {
            if (!f.isEmpty())
                countFiles++;
        }
        if (countFiles == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI("/" + 0));
            return new ResponseEntity(headers, HttpStatus.FOUND);
        }

        byte[] fileEntity;
        String fileName;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy---hh-mm-ss");
        String strTime = simpleDateFormat.format(new Date());
        String resultFileName = "attachment_" + strTime + ".zip";

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(zipBytes);

        List<String> listFilesNames = new ArrayList();

        for (MultipartFile f : files) {
            if (!f.isEmpty()) {
                fileName = f.getOriginalFilename();
                listFilesNames.add(fileName);
                fileEntity = f.getBytes();
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                zos.write(fileEntity);
                zos.closeEntry();
            }
        }
        zos.close();

        byte[] zipData = zipBytes.toByteArray();

        long keyMap = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "" + resultFileName);
        headers.setContentType(MediaType.parseMediaType("application/zip"));

        mapResponse.put(keyMap, new ZipEntity(listFilesNames, new ResponseEntity<byte[]>(zipData, headers, HttpStatus.OK)));

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setLocation(new URI("/"+keyMap));

        return new ResponseEntity(headersResponse, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable ("id") long id) throws IOException {
        ResponseEntity<byte[]> re = mapResponse.get(id).getResponseEntity();
//        mapResponse.remove(id);
        return re;
    }
}