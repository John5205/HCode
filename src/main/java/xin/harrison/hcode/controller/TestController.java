package xin.harrison.hcode.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.harrison.hcode.core.Barcode;
import xin.harrison.hcode.core.Captcha;
import xin.harrison.hcode.core.QrCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Harrison
 * @version 1.0.0
 */
@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping("barcode")
    public void index(HttpServletResponse response, String content) {
        // 如果没有指定内容，使用图片中的条形码号码作为默认值
        String barcodeContent = (content != null && !content.trim().isEmpty()) ? content : "6190102814526";
        BufferedImage generate = Barcode.generate(barcodeContent);
        // 设置响应类型
        response.setContentType("image/png");

        // 把 BufferedImage 写到 response 输出流
        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(generate, "png", os);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("qrcode")
    public void qrcode(HttpServletResponse response) {
        BufferedImage generate = QrCode.generate("https://github.com/Harrison-Huang");
        response.setContentType("image/png");
        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(generate, "png", os);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("captcha")
    public String captcha() {
        return Captcha.captchaBase64();
    }

    @RequestMapping("captchaImage")
    public void captchaImage(HttpServletResponse response) {
        BufferedImage captchaImage = Captcha.captchaImage(Captcha.randomText(4));
        response.setContentType("image/png");
        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(captchaImage, "png", os);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
