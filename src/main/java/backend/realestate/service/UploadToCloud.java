package backend.realestate.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.cloudinary.json.JSONObject;

import java.util.Map;

public class UploadToCloud {
    public static String uploadToCloud(String base64Img) {
        if (base64Img.startsWith("data:image")) {
            Cloudinary c = new Cloudinary("cloudinary://" + "792145993215497" + ":" + "wOROTNMRrWSh2mdUdwU5nJOx5PE" + "@" + "t-xanh-mi-n-trung");
            try {
                Map response = c.uploader().upload(base64Img, ObjectUtils.emptyMap());
                JSONObject json = new JSONObject(response);
                String url = json.getString("url");
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return base64Img;
    }
}
