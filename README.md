# **SafeKeyboard**
Android自定义安全软键盘，jni方式MD5存储加密，ui支持可扩展，使用方便简单安全，防录制，去除内容回显等。

- 使用方式

```java
 safeEdit.setTag("number");
 editList.add(safeEdit);
 editList.add(safeEdit2);
 editList.add(safeEdit3);
 safeKeyboard = new SafeKeyboard(MainActivity.this, keyboardContainer, editList,
                true, false, false);
```
- editList：需要用到安全键盘的EditText，可以添加进此List，
- 后面三个布尔值分别表示是否支持数字是否随机，字符是否随机，符号是否随机。
-  safeEdit.setTag("number"); 添加此tag，表示此EditText只支持数字输入。不设置表示支持数字，字母，符号三种输入方式。
- 图片 
        
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190409180331288.gif)
                    



