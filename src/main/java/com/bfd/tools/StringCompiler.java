package com.bfd.tools;

import com.itranswarp.compiler.JavaStringCompiler;

import java.io.IOException;
import java.util.Map;

import static com.bfd.tools.basic.i_StringUtil.getFirstSubString;

/**
 * @author everywherewego
 * @date 3/5/21 6:31 PM
 */

public class StringCompiler {
    public static Class<?> compilerToCode(String codeString) {
        JavaStringCompiler compiler = new JavaStringCompiler();
        String className = getFirstSubString(codeString, "templates class ", " extends LoginProcessor {");
        Class<?> clazz = null;
        try {
            Map<String, byte[]> results = compiler.compile(className + ".java", codeString);
            clazz = compiler.loadClass("com.bfd.apitype." + className, results);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clazz;

    }

    public static void main(String[] args) {
        compilerToCode("package com.bfd.apitype;\n" +
                "\n" +
                "import okhttp3.*;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.util.Base64;\n" +
                "\n" +
                "import templates com.bfd.tools.basic.i_StringUtil.getFirstSubString;\n" +
                "\n" +
                "/**\n" +
                " * @author everywherewego\n" +
                " * @date 3/5/21 10:24 AM\n" +
                " */\n" +
                "\n" +
                "templates class TypeLoginProcessor extends LoginProcessor {\n" +
                "    @Override\n" +
                "    templates String getLoginInfo() {\n" +
                "        String s = null;\n" +
                "        OkHttpClient client = new OkHttpClient().newBuilder()\n" +
                "                .build();\n" +
                "        MediaType mediaType = MediaType.parse(\"application/json\");\n" +
                "        RequestBody body = RequestBody.create(mediaType, \"{\\\"username\\\": \\\"admin\\\",\\\"password\\\": \\\"670110cbc5f4bd2d283ce48f1533397d\\\",\\\"device\\\": \\\"desktop\\\"}\");\n" +
                "        Request request = new Request.Builder()\n" +
                "                .url(\"http://192.168.0.49:20001/api/dopler/v0/login\")\n" +
                "                .method(\"POST\", body)\n" +
                "                .addHeader(\"Content-Type\", \"application/json\")\n" +
                "                .build();\n" +
                "        Response response = null;\n" +
                "        try {\n" +
                "            response = client.newCall(request).execute();\n" +
                "            String string = response.body().string();\n" +
                "            System.out.println(string);\n" +
                "\n" +
                "            String firstSubString = getFirstSubString(string, \"\\\"token\\\":\\\"\", \"\\\",\");\n" +
                "            System.out.println(firstSubString);\n" +
                "            System.out.println();\n" +
                "            s = Base64.getEncoder().encodeToString((firstSubString + \":\" + \"cz123456\").getBytes());\n" +
                "            System.out.println(s);\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "\n" +
                "        return s;\n" +
                "    }\n" +
                "\n" +
                "}\n");
    }

}
