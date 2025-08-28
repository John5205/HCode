package xin.harrison.hcode.core;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 条码生成器
 * 
 * @author Harrison
 * @version 1.0.0
 * @since 2025/8/2
 */
public class Barcode {

    // EAN-13 条形码编码表
    private static final String[][] EAN13_PATTERNS = {
        // 左侧A组编码 (奇数位)
        {"0001101", "0011001", "0010011", "0111101", "0100011", "0110001", "0101111", "0111011", "0110111", "0001011"},
        // 左侧B组编码 (偶数位)
        {"0100111", "0110011", "0011011", "0100001", "0011101", "0111001", "0000101", "0010001", "0001001", "0010111"},
        // 右侧C组编码
        {"1110010", "1100110", "1101100", "1000010", "1011100", "1001110", "1010000", "1000100", "1001000", "1110100"}
    };

    // 第一位数字决定左侧6位数字的编码模式 (A=0, B=1)
    private static final String[] FIRST_DIGIT_PATTERNS = {
        "AAAAAA", // 0
        "AABABB", // 1  
        "AABBAB", // 2
        "AABBBA", // 3
        "ABAABB", // 4
        "ABBAAB", // 5
        "ABBBAA", // 6
        "ABABAB", // 7
        "ABABBA", // 8
        "ABBABA"  // 9
    };

    /**
     * 生成条码图片
     *
     * @param content 条码内容
     * @return 条码图片
     */
    public static BufferedImage generate(String content) {
        // 如果输入不是13位，则使用原来的简单模式
        if (content.length() != 13 || !content.matches("\\d+")) {
            return generateSimple(content);
        }
        
        return generateEAN13(content);
    }

    /**
     * 生成EAN-13条形码
     */
    private static BufferedImage generateEAN13(String content) {
        int barWidth = 1; // 每个条的基础宽度
        int barHeight = 50; // 条形码高度
        int guardHeight = 55; // 保护条高度
        int textHeight = 12; // 减少文字区域高度
        int margin = 2; // 减少边距
        
        // EAN-13 总共有95个条（3+42+5+42+3）
        int totalBars = 95;
        int firstDigitWidth = 12; // 第一位数字的空间
        int width = totalBars * barWidth + margin * 2 + firstDigitWidth;
        int height = guardHeight + textHeight + 3; // 减少总高度

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 抗锯齿和字体渲染优化
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 背景白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 生成条形码数据
        String binaryData = generateEAN13Binary(content);
        
        // 画条形码
        g.setColor(Color.BLACK);
        int x = margin + firstDigitWidth; // 为第一位数字留出空间
        
        for (int i = 0; i < binaryData.length(); i++) {
            if (binaryData.charAt(i) == '1') {
                // 起始符、中间符、结束符使用全高度
                int currentHeight = barHeight;
                int yOffset = 2;
                
                // 保护条（起始符、中间符、结束符）稍微高一点
                if (i < 3 || (i >= 45 && i < 50) || i >= 92) {
                    currentHeight = guardHeight;
                    yOffset = 2;
                }
                
                g.fillRect(x, yOffset, barWidth, currentHeight);
            }
            x += barWidth;
        }

        // 画数字 - 数字紧贴条形码
        g.setFont(new Font("Arial", Font.BOLD, 10)); // 使用粗体字体，稍微增大字号
        FontMetrics fm = g.getFontMetrics();
        int textY = guardHeight + 5; // 数字紧贴条形码底部
        
        // 第一位数字（左侧单独显示）
        String firstDigit = content.substring(0, 1);
        g.drawString(firstDigit, margin + 2, textY);
        
        // 左侧6位数字 (位置1-6)
        String leftDigits = content.substring(1, 7);
        int leftStart = margin + firstDigitWidth + 3 * barWidth; // 起始符后开始
        int leftWidth = 42 * barWidth; // 左侧数据区域宽度
        int leftTextX = leftStart + (leftWidth - fm.stringWidth(leftDigits)) / 2;
        g.drawString(leftDigits, leftTextX, textY);
        
        // 右侧6位数字 (位置7-12)
        String rightDigits = content.substring(7, 13);
        int rightStart = margin + firstDigitWidth + 3 * barWidth + 42 * barWidth + 5 * barWidth; // 中间分隔符后
        int rightWidth = 42 * barWidth; // 右侧数据区域宽度
        int rightTextX = rightStart + (rightWidth - fm.stringWidth(rightDigits)) / 2;
        g.drawString(rightDigits, rightTextX, textY);

        g.dispose();
        return image;
    }

    /**
     * 生成EAN-13二进制数据
     */
    private static String generateEAN13Binary(String content) {
        StringBuilder binary = new StringBuilder();
        
        // 起始符 (3位)
        binary.append("101");
        
        // 第一位数字决定左侧编码模式
        int firstDigit = Integer.parseInt(content.substring(0, 1));
        String pattern = FIRST_DIGIT_PATTERNS[firstDigit];
        
        // 左侧6位数字 (每位7位，共42位)
        for (int i = 1; i <= 6; i++) {
            int digit = Integer.parseInt(content.substring(i, i + 1));
            char patternType = pattern.charAt(i - 1); // A或B
            int patternIndex = (patternType == 'A') ? 0 : 1;
            binary.append(EAN13_PATTERNS[patternIndex][digit]);
        }
        
        // 中间分隔符 (5位)
        binary.append("01010");
        
        // 右侧6位数字 (每位7位，共42位) - 使用C组编码
        for (int i = 7; i <= 12; i++) {
            int digit = Integer.parseInt(content.substring(i, i + 1));
            binary.append(EAN13_PATTERNS[2][digit]); // 右侧C组编码
        }
        
        // 结束符 (3位)
        binary.append("101");
        
        return binary.toString();
    }

    /**
     * 生成简单条码（原来的逻辑）
     */
    private static BufferedImage generateSimple(String content) {
        int width = content.length() * 10 + 40;
        int height = 80;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 画条码
        g.setColor(Color.BLACK);
        int x = 10;
        for (char c : content.toCharArray()) {
            int barWidth = (c % 7 + 1) * 2; // 模拟条码宽度
            g.fillRect(x, 10, barWidth, 40);
            x += barWidth + 2;
        }

        // 画数字（在条码下方居中）
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(content);
        int textX = (width - textWidth) / 2;
        int textY = 65;
        g.drawString(content, textX, textY);

        g.dispose();
        return image;
    }
}