package com.team10.ojbattle.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/31 9:48
 * @version: 1.0
 */
public enum LanguageConfigEnum implements IEnum<String> {
    /**
     *
     */
    C_LANG_CONFIG(0, "{\n" +
            "    \"compile\": {\n" +
            "        \"src_name\": \"main.c\",\n" +
            "        \"exe_name\": \"main\",\n" +
            "        \"max_cpu_time\": 3000,\n" +
            "        \"max_real_time\": 5000,\n" +
            "        \"max_memory\": 128 * 1024 * 1024,\n" +
            "        \"compile_command\": \"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}\",\n" +
            "    },\n" +
            "    \"run\": {\n" +
            "        \"command\": \"{exe_path}\",\n" +
            "        \"seccomp_rule\": \"c_cpp\",\n" +
            "        \"env\": default_env\n" +
            "    }\n" +
            "}"),

    /**
     *
     */
    C_LANG_SPJ_COMPILE(1, "{\n" +
            "    \"src_name\": \"spj-{spj_version}.c\",\n" +
            "    \"exe_name\": \"spj-{spj_version}\",\n" +
            "    \"max_cpu_time\": \"3000\",\n" +
            "    \"max_real_time\": \"5000\",\n" +
            "    \"max_memory\": \"1073741824\",\n" +
            "    \"compile_command\": \"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}\"\n" +
            "}"),

    /**
     *
     */
    C_LANG_SPJ_CONFIG(2, "{\n" +
            "    \"exe_name\": \"spj-{spj_version}\",\n" +
            "    \"command\": \"{exe_path} {in_file_path} {user_out_file_path}\",\n" +
            "    \"seccomp_rule\": \"c_cpp\"\n" +
            "}"),

    /**
     *
     */
    CPP_LANG_CONFIG(3, "{\n" +
            "    \"compile\": {\n" +
            "        \"src_name\": \"main.cpp\",\n" +
            "        \"exe_name\": \"main\",\n" +
            "        \"max_cpu_time\": \"3000\",\n" +
            "        \"max_real_time\": \"5000\",\n" +
            "        \"max_memory\": \"134217728\",\n" +
            "        \"compile_command\": \"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}\",\n" +
            "    },\n" +
            "    \"run\": {\n" +
            "        \"command\": \"{exe_path}\",\n" +
            "        \"seccomp_rule\": \"c_cpp\",\n" +
            "        \"env\": [\"LANG=en_US.UTF-8\", \"LANGUAGE=en_US:en\", \"LC_ALL=en_US.UTF-8\"]\n" +
            "    }\n" +
            "}"),
    /**
     *
     */
    JAVA_LANG_CONFIG(4, "{\n" +
            "    \"name\": \"java\",\n" +
            "    \"compile\": {\n" +
            "        \"src_name\": \"Main.java\",\n" +
            "        \"exe_name\": \"Main\",\n" +
            "        \"max_cpu_time\": \"3000\",\n" +
            "        \"max_real_time\": \"5000\",\n" +
            "        \"max_memory\": \"-1\",\n" +
            "        \"compile_command\": \"/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8\"\n" +
            "    },\n" +
            "    \"run\": {\n" +
            "        \"command\": \"/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main\",\n" +
            "        \"seccomp_rule\": \"None\",\n" +
            "        \"env\": [\"LANG=en_US.UTF-8\", \"LANGUAGE=en_US:en\", \"LC_ALL=en_US.UTF-8\"],\n" +
            "        \"memory_limit_check_only\": \"1\"\n" +
            "    }\n" +
            "}"),

    /**
     *
     */
    PY2_LANG_CONFIG(5, "{\n" +
            "    \"compile\": {\n" +
            "        \"src_name\": \"solution.py\",\n" +
            "        \"exe_name\": \"solution.pyc\",\n" +
            "        \"max_cpu_time\": \"3000\",\n" +
            "        \"max_real_time\": \"5000\",\n" +
            "        \"max_memory\": \"134217728\",\n" +
            "        \"compile_command\": \"/usr/bin/python -m py_compile {src_path}\",\n" +
            "    },\n" +
            "    \"run\": {\n" +
            "        \"command\": \"/usr/bin/python {exe_path}\",\n" +
            "        \"seccomp_rule\": \"general\",\n" +
            "        \"env\": [\"LANG=en_US.UTF-8\", \"LANGUAGE=en_US:en\", \"LC_ALL=en_US.UTF-8\"]\n" +
            "    }\n" +
            "}"),

    /**
     *
     */
    PY3_LANG_CONFIG(6, "{\n" +
            "    \"compile\": {\n" +
            "        \"src_name\": \"solution.py\",\n" +
            "        \"exe_name\": \"__pycache__/solution.cpython-35.pyc\",\n" +
            "        \"max_cpu_time\": \"3000\",\n" +
            "        \"max_real_time\": \"5000\",\n" +
            "        \"max_memory\": \"134217728\",\n" +
            "        \"compile_command\": \"/usr/bin/python3 -m py_compile {src_path}\",\n" +
            "    },\n" +
            "    \"run\": {\n" +
            "        \"command\": \"/usr/bin/python3 {exe_path}\",\n" +
            "        \"seccomp_rule\": \"general\",\n" +
            "        \"env\": [\"PYTHONIOENCODING=UTF-8\",\"LANG=en_US.UTF-8\", \"LANGUAGE=en_US:en\", \"LC_ALL=en_US.UTF-8\"]\n" +
            "    }\n" +
            "}");

    private final int code;

    private final String value;

    LanguageConfigEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
