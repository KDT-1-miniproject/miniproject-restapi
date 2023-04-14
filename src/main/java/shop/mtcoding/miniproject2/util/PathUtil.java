package shop.mtcoding.miniproject2.util;

import org.springframework.http.HttpStatus;
import shop.mtcoding.miniproject2.handler.ex.CustomException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PathUtil {

    private static String getStaticFolder() {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
    }

    public static String writeImageFile(String profile) {
        UUID uuid = UUID.randomUUID();
        String uuidImageDBName = "/images/" + uuid + "_" + profile;
        String uuidImageRealName = "images\\" + uuid + "_" + profile;
        String staticFolder = getStaticFolder();

        Path imageFilePath = Paths.get(staticFolder + uuidImageRealName);
        try {
            Files.write(imageFilePath, profile.getBytes());
        } catch (Exception e) {
            throw new CustomException("사진을 웹서버에 저장하지 못하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return uuidImageDBName;
    }
}