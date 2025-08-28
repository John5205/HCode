package xin.harrison.hcode.enums;

/**
 * 格式枚举
 *
 * @author Harrison
 * @version 1.0.0
 * @since 2025/8/2
 */
public enum FormatEnum {
    ;

    /**
     * 图片格式
     *
     * @author Harrison
     * @version 1.0.0
     * @since 2025/8/2
     */
     public enum Image{
          JPEG("JPEG"),
          PNG("PNG"),
          GIF("GIF"),
          BMP("BMP");

         String suffix;

         private String getSuffix(){
             return suffix;
         }

         private void setSuffix(String suffix){
             this.suffix = suffix;
         }

         Image(String suffix){
             this.suffix = suffix;
         }
     }

    /**
     * 字体
     * 
     * @author Harrison
     * @version 1.0.0
     * @since 2025/8/2
     */
     public enum Font{
          ARIAL("Arial"),
          TIMES_NEW_ROMAN("Times New Roman"),
          COURIER_NEW("Courier New");

         String name;

         private String getName(){
             return name;
         }

         private void setName(String name){
             this.name = name;
         }

         Font(String name){
             this.name = name;
         }
     }
}
