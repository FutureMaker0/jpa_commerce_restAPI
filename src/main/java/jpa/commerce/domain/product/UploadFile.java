package jpa.commerce.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

//@Data
@Entity
@Getter
@Setter
//@Embeddable
public class UploadFile {

    @Id
    @GeneratedValue
    @Column(name = "uploadFile_id")
    private Long id;

    //@Column(name = "upload_file_name") // 테이블 컬럼명 지정, 디폴트 값이랑 똑같다.
    private String uploadFileName;

    //@Column(name = "store_file_name") // 테이블 컬럼명 지정, 디폴트 값이랑 똑같다.
    private String storeFileName;

    protected UploadFile() {

    }

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

//    //== 핵심 비즈니스 로직 ==// UploadFile <-> MultipartFile 간 형변환
//    public UploadFile multipartFileToUploadFile(MultipartFile mfile) throws IOException {
//        UploadFile uploadFile = new UploadFile(mfile.getOriginalFilename());
//        mfile.transferTo((Path) uploadFile);
//        return uploadFile;
//    }

}
