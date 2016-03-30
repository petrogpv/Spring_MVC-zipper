package ua.kiev.prog;

import org.springframework.http.ResponseEntity;

import java.util.List;


public class ZipEntity {

    private  List<String> listFilesNames;
    private ResponseEntity<byte[]> responseEntity;

    public ZipEntity(List<String> listFilesNames, ResponseEntity<byte[]> responseEntity) {

        this.listFilesNames = listFilesNames;
        this.responseEntity = responseEntity;
    }

    public List<String> getListFilesNames() {
        return listFilesNames;
    }

    public void setListFilesNames(List<String> listFilesNames) {
        this.listFilesNames = listFilesNames;
    }

    public ResponseEntity<byte[]> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<byte[]> responseEntity) {
        this.responseEntity = responseEntity;
    }




}
